package jbdl.multithreading;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(10) // a thread that never finishes would otherwise hang the build
class ThreadExamplesTest {

    @Test
    @DisplayName("join() one by one gives a fixed order")
    void joinGivesFixedOrder() throws InterruptedException {
        List<String> log = ThreadExamples.newSharedLog();

        ThreadExamples.runOneAfterAnother(List.of(
                new Thread(new CountingTask("t1", 2, log)),
                new Thread(new CountingTask("t2", 2, log)),
                new Thread(new CountingTask("t3", 2, log))), log);

        assertThat(log).containsExactly("t1:0", "t1:1", "t2:0", "t2:1", "t3:0", "t3:1");
    }

    @Test
    @DisplayName("Concurrent threads: every step is logged, each thread's own steps stay in order")
    void concurrentThreadsLogEverything() throws InterruptedException {
        List<String> log = ThreadExamples.newSharedLog();

        ThreadExamples.runConcurrently(List.of(
                new CountingThread("A", 500, log),
                new Thread(new CountingTask("B", 500, log))), log);

        assertThat(log).hasSize(1000);
        assertThat(stepsOf("A", log)).isEqualTo(expectedSteps("A", 500));
        assertThat(stepsOf("B", log)).isEqualTo(expectedSteps("B", 500));
    }

    private static List<String> stepsOf(String name, List<String> log) {
        return log.stream().filter(entry -> entry.startsWith(name + ":")).toList();
    }

    private static List<String> expectedSteps(String name, int count) {
        return IntStream.range(0, count).mapToObj(i -> name + ":" + i).toList();
    }

    @Test
    @DisplayName("Extending Thread runs the work in a new thread, not the caller's")
    void runsInNewThread() throws InterruptedException {
        List<String> log = ThreadExamples.newSharedLog();
        CountingThread thread = new CountingThread("worker", 1, log);

        thread.start();
        thread.join();

        assertThat(log).containsExactly("worker:0");
        assertThat(thread.getName()).isNotEqualTo(Thread.currentThread().getName());
    }
}
