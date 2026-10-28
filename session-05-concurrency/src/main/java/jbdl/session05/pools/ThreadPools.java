package jbdl.session05.pools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Why pools: creating a thread per task is expensive and unbounded. A pool keeps a few threads alive
 * and feeds them tasks from a queue. Also: join vs a latch for "wait until all are done".
 */
public final class ThreadPools {

    private ThreadPools() {
    }

    /** 20 tasks on a pool of 3 threads: every task runs, but only 3 distinct threads ever exist. */
    public static Set<String> threadsUsedByFixedPool(int poolSize, int tasks) throws InterruptedException {
        Set<String> names = ConcurrentHashMap.newKeySet();
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);
        try {
            CountDownLatch done = new CountDownLatch(tasks);
            for (int i = 0; i < tasks; i++) {
                pool.execute(() -> {
                    names.add(Thread.currentThread().getName());
                    done.countDown();
                });
            }
            done.await(); // like join, but for tasks in a pool (pool threads never "end")
        } finally {
            shutdownAndWait(pool);
        }
        return names;
    }

    /** The parts every pool is made of, visible on a ThreadPoolExecutor. */
    public static String describeFixedPool(int size) {
        ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(size);
        try {
            return "core=" + pool.getCorePoolSize() + ", max=" + pool.getMaximumPoolSize()
                    + ", queue=" + pool.getQueue().getClass().getSimpleName();
        } finally {
            pool.shutdown();
        }
    }

    /** join() on plain threads: the caller waits for each one; results are collected in order. */
    public static List<Integer> squaresWithJoin(int n) throws InterruptedException {
        int[] results = new int[n];
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int index = i;
            // the lambda is the thread's run() method; it may use `index` because index is effectively
            // final (a new variable per loop turn), while the loop variable i changes and couldn't be used
            Thread thread = new Thread(() -> results[index] = index * index);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join(); // also guarantees the writes to results[] are visible after this line
        }
        List<Integer> list = new ArrayList<>();
        for (int value : results) {
            list.add(value);
        }
        return list;
    }

    /** The standard way to stop a pool: no new tasks, wait a while, then interrupt what's left. */
    public static void shutdownAndWait(ExecutorService pool) throws InterruptedException {
        pool.shutdown();
        if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
            pool.shutdownNow();
        }
    }
}
