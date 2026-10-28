package jbdl.session05.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Running threads side by side vs one after another with join().
 *
 * Fixed from 2024: ThreadJoin's worker created an inner Thread but never started it, so the threads
 * printed nothing and join() had nothing to wait for. Here the work runs directly in the started thread.
 */
public final class ThreadExamples {

    private ThreadExamples() {
    }

    /** Starts all threads first, then waits for each. Their log lines can interleave in any order. */
    public static List<String> runConcurrently(List<Thread> threads, List<String> log) throws InterruptedException {
        threads.forEach(Thread::start);
        for (Thread thread : threads) {
            thread.join();
        }
        return log;
    }

    /** Starts each thread and joins it before starting the next, so the log is in a fixed order. */
    public static List<String> runOneAfterAnother(List<Thread> threads, List<String> log) throws InterruptedException {
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
        return log;
    }

    /** A list that several threads can add to safely (a plain ArrayList can lose entries). */
    public static List<String> newSharedLog() {
        return Collections.synchronizedList(new ArrayList<>());
    }

    public static void main(String[] args) throws InterruptedException {
        List<String> concurrentLog = newSharedLog();
        runConcurrently(List.of(
                new CountingThread("thread-A", 5, concurrentLog),
                new Thread(new CountingTask("task-B", 5, concurrentLog))), concurrentLog);
        System.out.println("Concurrent (order varies): " + concurrentLog);

        List<String> sequentialLog = newSharedLog();
        runOneAfterAnother(List.of(
                new Thread(new CountingTask("t1", 2, sequentialLog)),
                new Thread(new CountingTask("t2", 2, sequentialLog)),
                new Thread(new CountingTask("t3", 2, sequentialLog))), sequentialLog);
        System.out.println("With join (fixed order):   " + sequentialLog);
    }
}
