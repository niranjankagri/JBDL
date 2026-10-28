package jbdl.session05.threads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * The states a thread goes through, observed for real: NEW before start(), RUNNABLE while running,
 * WAITING/TIMED_WAITING while blocked on a latch or sleep, TERMINATED after run() returns.
 */
public final class ThreadLifecycle {

    private ThreadLifecycle() {
    }

    public static List<Thread.State> observeStates() throws InterruptedException {
        List<Thread.State> states = new ArrayList<>();
        CountDownLatch release = new CountDownLatch(1);
        Thread worker = new Thread(() -> {
            try {
                release.await(); // parks here until the main thread counts down
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "lifecycle-worker");

        states.add(worker.getState());               // NEW
        worker.start();
        while (worker.getState() != Thread.State.WAITING) {
            Thread.onSpinWait();                     // wait until it is parked in await()
        }
        states.add(worker.getState());               // WAITING
        release.countDown();
        worker.join();
        states.add(worker.getState());               // TERMINATED
        return states;
    }

    /**
     * Interrupting a sleeping thread wakes it with InterruptedException: the polite way to ask a
     * thread to stop. Returns what the worker reported.
     */
    public static String interruptSleepingThread() throws InterruptedException {
        StringBuilder report = new StringBuilder();
        Thread sleeper = new Thread(() -> {
            try {
                Thread.sleep(60_000);
                report.append("woke up normally");
            } catch (InterruptedException e) {
                report.append("interrupted while sleeping");
            }
        });
        sleeper.start();
        while (sleeper.getState() != Thread.State.TIMED_WAITING) {
            Thread.onSpinWait();
        }
        sleeper.interrupt();
        sleeper.join(); // join also makes the worker's write to `report` visible here
        return report.toString();
    }

    /** A daemon thread doesn't keep the JVM alive; the JVM exits when only daemons are left. */
    public static boolean isDaemonByDefault() {
        return new Thread(() -> { }).isDaemon();
    }
}
