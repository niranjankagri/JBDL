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
        List<Callable<Quote>> tasks = shops.stream().map(this::askShop).toList();
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
        return pool.invokeAny(shops.stream().map(this::askShop).toList());
    }

    /**
     * CompletableFuture: describe the flow (run async, transform, combine, recover) instead of
     * blocking on each Future. Price with 18 % GST from the cheaper of two shops, or a fallback.
     */
    public CompletableFuture<String> bestPriceWithTax(String shopA, String shopB) {
        CompletableFuture<Quote> a = CompletableFuture.supplyAsync(() -> call(shopA), pool);
        CompletableFuture<Quote> b = CompletableFuture.supplyAsync(() -> call(shopB), pool);
        return a.thenCombine(b, (x, y) -> x.priceInRupees() <= y.priceInRupees() ? x : y)
                .thenApply(q -> q.shop() + " incl. GST: " + q.priceInRupees() * 118 / 100)
                .exceptionally(error -> "no price available (" + error.getCause().getMessage() + ")");
    }

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
