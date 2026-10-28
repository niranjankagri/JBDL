package jbdl.session02.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * When finally runs, and try-with-resources. Every method records the order of events in a list so
 * the tests can check it.
 */
public final class FinallyDemo {

    private FinallyDemo() {
    }

    /** finally runs whether try completes normally or throws. */
    public static List<String> tryCatchFinally(boolean fail) {
        List<String> events = new ArrayList<>();
        try {
            events.add("try");
            if (fail) {
                throw new IllegalStateException("boom");
            }
            events.add("try finished");
        } catch (IllegalStateException e) {
            events.add("catch " + e.getMessage());
        } finally {
            events.add("finally");
        }
        return events;
    }

    /** finally runs even after `return`: the value to return is computed first, then finally runs. */
    public static int returnFromTry(List<String> events) {
        int value = 1;
        try {
            events.add("try returns " + value);
            return value;
        } finally {
            value = 2; // too late: the return value (1) was already saved
            events.add("finally sets value to " + value);
        }
    }

    /**
     * A resource whose close() is recorded. try-with-resources closes resources automatically, in
     * REVERSE order of opening, before catch/finally run.
     */
    public static final class Resource implements AutoCloseable {
        private final String name;
        private final List<String> events;
        private final boolean failOnClose;

        public Resource(String name, List<String> events, boolean failOnClose) {
            this.name = name;
            this.events = events;
            this.failOnClose = failOnClose;
            events.add("open " + name);
        }

        @Override
        public void close() {
            events.add("close " + name);
            if (failOnClose) {
                throw new IllegalStateException("close failed: " + name);
            }
        }
    }

    public static List<String> tryWithResources() {
        List<String> events = new ArrayList<>();
        try (Resource db = new Resource("db", events, false);
             Resource file = new Resource("file", events, false)) {
            events.add("use " + db.name + " and " + file.name);
        } finally {
            events.add("finally");
        }
        return events;
    }

    /**
     * If the body throws AND close() throws, the body's exception wins and the close exception is
     * attached to it as "suppressed" (with a plain finally, the close exception would hide the first).
     */
    public static IllegalStateException bodyAndCloseBothFail() {
        List<String> events = new ArrayList<>();
        try (Resource resource = new Resource("socket", events, true)) {
            throw new IllegalStateException("body failed on " + resource.name);
        } catch (IllegalStateException e) {
            return e;
        }
    }
}
