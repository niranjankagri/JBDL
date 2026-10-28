package jbdl.session05.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The classic race condition and four fixes. count++ is three steps (read, add, write); two threads
 * can read the same value and one increment is lost.
 */
public final class Counters {

    private Counters() {
    }

    /** A counter that can be incremented. */
    public interface Counter {
        void increment();

        int get();
    }

    /** No protection: loses increments under contention. Demo only. */
    public static final class UnsafeCounter implements Counter {
        private int count;

        @Override
        public void increment() {
            count++;
        }

        @Override
        public int get() {
            return count;
        }
    }

    /** synchronized: only one thread at a time runs the method (holds this object's monitor). */
    public static final class SynchronizedCounter implements Counter {
        private int count;

        @Override
        public synchronized void increment() {
            count++;
        }

        @Override
        public synchronized int get() {
            return count;
        }
    }

    /** AtomicInteger: a lock-free compare-and-swap in hardware; fastest for a single number. */
    public static final class AtomicCounter implements Counter {
        private final AtomicInteger count = new AtomicInteger();

        @Override
        public void increment() {
            count.incrementAndGet();
        }

        @Override
        public int get() {
            return count.get();
        }
    }

    /** ReentrantLock: like synchronized, plus tryLock, timeouts and fairness; always unlock in finally. */
    public static final class LockCounter implements Counter {
        private final ReentrantLock lock = new ReentrantLock();
        private int count;

        @Override
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public int get() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }

    /** Runs `threads` x `perThread` increments at the same time and returns the final count. */
    public static int hammer(Counter counter, int threads, int perThread) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        for (int t = 0; t < threads; t++) {
            pool.execute(() -> {
                for (int i = 0; i < perThread; i++) {
                    counter.increment();
                }
            });
        }
        pool.shutdown();
        if (!pool.awaitTermination(30, TimeUnit.SECONDS)) {
            throw new IllegalStateException("increments did not finish");
        }
        return counter.get();
    }

    public static void main(String[] args) throws InterruptedException {
        int threads = 8;
        int perThread = 100_000;
        System.out.println("Expected:     " + threads * perThread);
        for (Counter counter : new Counter[] {new UnsafeCounter(), new SynchronizedCounter(), new AtomicCounter(), new LockCounter()}) {
            System.out.printf("%-20s %d%n", counter.getClass().getSimpleName() + ":", hammer(counter, threads, perThread));
        }
    }
}
