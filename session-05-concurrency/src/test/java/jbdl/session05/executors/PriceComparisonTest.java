package jbdl.session05.executors;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Topic 4: three 300 ms calls run in parallel, failures come back through Future.get, invokeAll
 * and invokeAny, and a CompletableFuture flow that combines, transforms and recovers.
 */
@Timeout(20)
class PriceComparisonTest {

    private static final Map<String, Long> PRICES = Map.of("ShopA", 1_000L, "ShopB", 950L, "ShopC", 1_200L);
    private static final long DELAY_MS = 300;

    // a new pool per test (JUnit creates a new test instance per method), shut down after each
    private final ExecutorService pool = Executors.newFixedThreadPool(4);
    private final PriceComparison comparison = new PriceComparison(pool, PRICES, DELAY_MS);

    @AfterEach
    void stopPool() throws InterruptedException {
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }

    @Test
    @DisplayName("Three 300 ms calls in parallel take about 300 ms, not 900 ms")
    void runsInParallel() throws InterruptedException {
        long start = System.nanoTime();
        List<String> quotes = comparison.allQuotes(List.of("ShopA", "ShopB", "ShopC"));
        long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);

        assertThat(quotes).containsExactly("ShopA: 1000", "ShopB: 950", "ShopC: 1200");
        assertThat(millis).isLessThan(3 * DELAY_MS); // one after another would take at least 900 ms
    }

    @Test
    @DisplayName("A failed task comes back from Future.get as ExecutionException with the cause")
    void failureFromFuture() throws InterruptedException {
        assertThat(comparison.allQuotes(List.of("ShopA", "ClosedShop")))
                .containsExactly("ShopA: 1000", "failed: ClosedShop is unavailable");
    }

    @Test
    @DisplayName("invokeAll waits for all; invokeAny returns the first success")
    void invokeAllAndAny() throws Exception {
        assertThat(comparison.cheapest(List.of("ShopA", "ShopB", "ShopC")).shop()).isEqualTo("ShopB");
        assertThat(comparison.fastestAnswer(List.of("ClosedShop", "ShopC")).shop()).isEqualTo("ShopC");
    }

    @Test
    @DisplayName("CompletableFuture: combine two async calls, transform, recover from failure")
    void completableFuture() {
        assertThat(comparison.bestPriceWithTax("ShopA", "ShopB").join()).isEqualTo("ShopB incl. GST: 1121");
        assertThat(comparison.bestPriceWithTax("ShopA", "ClosedShop").join())
                .isEqualTo("no price available (ClosedShop is unavailable)");
    }
}
