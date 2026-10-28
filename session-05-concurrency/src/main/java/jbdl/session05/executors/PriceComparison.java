package jbdl.session05.executors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The ExecutorService framework on a backend-shaped task: ask several (slow) shops for a price at
 * the same time instead of one after another.
 */
public final class PriceComparison {

    /** One shop's answer. */
    public record Quote(String shop, long priceInRupees) {
    }

    private final ExecutorService pool;
    private final Map<String, Long> prices;
    private final long delayMillis;

    /**
     * @param pool        the threads that run the shop calls (owned and shut down by the caller)
     * @param prices      the price each shop answers with; a shop not in the map fails
     * @param delayMillis how long each simulated call takes
     */
    public PriceComparison(ExecutorService pool, Map<String, Long> prices, long delayMillis) {
        this.pool = pool;
        this.prices = prices;
        this.delayMillis = delayMillis;
    }

    /** Callable: like Runnable, but returns a value and may throw a checked exception. */
    private Callable<Quote> askShop(String shop) {
        return () -> {
            TimeUnit.MILLISECONDS.sleep(delayMillis); // pretend this is an HTTP call
            Long price = prices.get(shop);
            if (price == null) {
                throw new IllegalStateException(shop + " is unavailable");
            }
            return new Quote(shop, price);
        };
    }

    /** submit + Future.get: start all calls, then collect; failures come back as ExecutionException. */
    public List<String> allQuotes(List<String> shops) throws InterruptedException {
        List<Future<Quote>> futures = new ArrayList<>();
        for (String shop : shops) {
            futures.add(pool.submit(askShop(shop)));
        }
        List<String> lines = new ArrayList<>();
        for (Future<Quote> future : futures) {
            try {
                Quote quote = future.get(5, TimeUnit.SECONDS);
                lines.add(quote.shop() + ": " + quote.priceInRupees());
            } catch (ExecutionException e) {
                lines.add("failed: " + e.getCause().getMessage()); // the task's exception is the cause
            } catch (TimeoutException e) {
                future.cancel(true);
                lines.add("timed out");
            }
        }
        return lines;
    }

    /** invokeAll: submit a batch and wait for all of them. */
    public Quote cheapest(List<String> shops) throws InterruptedException, ExecutionException {
        List<Callable<Quote>> tasks = new ArrayList<>();
        for (String shop : shops) {
            tasks.add(askShop(shop));
        }
        Quote best = null;
        for (Future<Quote> future : pool.invokeAll(tasks)) {
            Quote quote = future.get();
            if (best == null || quote.priceInRupees() < best.priceInRupees()) {
                best = quote;
            }
        }
        return best;
    }

    /** invokeAny: the first task to SUCCEED wins; the others are cancelled. */
    public Quote fastestAnswer(List<String> shops) throws InterruptedException, ExecutionException {
        List<Callable<Quote>> tasks = new ArrayList<>();
        for (String shop : shops) {
            tasks.add(askShop(shop));
        }
        return pool.invokeAny(tasks);
    }

    /**
     * CompletableFuture: describe the flow (run async, transform, combine, recover) instead of
     * blocking on each Future. Price with 18 % GST from the cheaper of two shops, or a fallback.
     */
    public CompletableFuture<String> bestPriceWithTax(String shopA, String shopB) {
        // The most advanced code of the session. What each line means:
        //   supplyAsync(...)     start call(shopA) on a pool thread, get a "future" result right away
        //   thenCombine(b, ...)  when BOTH are done, keep the cheaper quote
        //   thenApply(...)       when that is ready, turn it into the text with 18 % GST
        //   exceptionally(...)   if any step failed, use this text instead
        // The simple, blocking version of the same logic:
        //   try {
        //       Quote x = call(shopA);  Quote y = call(shopB);          // one after the other: slower
        //       Quote best = x.priceInRupees() <= y.priceInRupees() ? x : y;
        //       return best.shop() + " incl. GST: " + best.priceInRupees() * 118 / 100;
        //   } catch (RuntimeException e) { return "no price available (" + e.getMessage() + ")"; }
        CompletableFuture<Quote> a = CompletableFuture.supplyAsync(() -> call(shopA), pool);
        CompletableFuture<Quote> b = CompletableFuture.supplyAsync(() -> call(shopB), pool);
        return a.thenCombine(b, (x, y) -> x.priceInRupees() <= y.priceInRupees() ? x : y)
                .thenApply(q -> q.shop() + " incl. GST: " + q.priceInRupees() * 118 / 100)
                .exceptionally(error -> "no price available (" + error.getCause().getMessage() + ")");
    }

    /** Runs a shop call on the current thread, as supplyAsync needs a Supplier, which can't throw checked exceptions. */
    private Quote call(String shop) {
        try {
            return askShop(shop).call();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
