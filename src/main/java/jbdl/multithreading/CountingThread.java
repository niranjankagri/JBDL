package jbdl.multithreading;

import java.util.List;

/** Way 2: extend Thread and override run(). Call start() (new thread), never run() (same thread). */
public class CountingThread extends Thread {

    private final int count;
    private final List<String> log;

    public CountingThread(String name, int count, List<String> log) {
        super(name);
        this.count = count;
        this.log = log;
    }

    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            log.add(getName() + ":" + i);
        }
    }
}
