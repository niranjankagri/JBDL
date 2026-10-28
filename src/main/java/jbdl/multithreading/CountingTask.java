package jbdl.multithreading;

import java.util.List;

/**
 * Way 1 to define work for a thread: implement Runnable and hand it to a Thread. Preferred over
 * extending Thread, because the class can still extend something else.
 * Each step is written to a shared log as "name:i" so the order of execution can be checked.
 */
public class CountingTask implements Runnable {

    private final String name;
    private final int count;
    private final List<String> log;

    /** The log is written from several threads, so pass a thread-safe list. */
    public CountingTask(String name, int count, List<String> log) {
        this.name = name;
        this.count = count;
        this.log = log;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            log.add(name + ":" + i);
        }
    }
}
