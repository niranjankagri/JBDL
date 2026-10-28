package jbdl.session05.pools;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Topic 2: a fixed pool reuses its threads, its parts (core, max, queue), and join collecting results.
 */
@Timeout(20)
class ThreadPoolsTest {

    @Test
    @DisplayName("20 tasks on a fixed pool of 3: at most 3 threads, reused")
    void fixedPoolReusesThreads() throws InterruptedException {
        // at most 3 names even for 20 tasks: the same threads were reused (fewer if some were fast)
        assertThat(ThreadPools.threadsUsedByFixedPool(3, 20))
                .hasSizeBetween(1, 3)
                .allMatch(name -> name.startsWith("pool-"));
    }

    @Test
    @DisplayName("A fixed pool: core = max = size, unbounded LinkedBlockingQueue")
    void fixedPoolParts() {
        assertThat(ThreadPools.describeFixedPool(4)).isEqualTo("core=4, max=4, queue=LinkedBlockingQueue");
    }

    @Test
    @DisplayName("join waits for every thread, so all results are present and visible")
    void joinCollectsResults() throws InterruptedException {
        assertThat(ThreadPools.squaresWithJoin(6)).containsExactly(0, 1, 4, 9, 16, 25);
    }
}
