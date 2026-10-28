package jbdl.session05.threads;

import java.util.List;

/** Way 2: extend Thread and override run(). Call start() (new thread), never run() (same thread). */
public class CountingThread extends Thread {

    private final int count;
    private final List<String> log;

    /** The name is the thread's own name (getName()), which is what the log entries use. */
    public CountingThread(String name, int count, List<String> log) {
        super(name);
        this.count = count;
        this.log = log;
    }

    /** Runs on the new thread after start(). */
    @Override
    public void run() {
        for (int i = 0; i < count; i++) {
            log.add(getName() + ":" + i);
        }
    }
}
