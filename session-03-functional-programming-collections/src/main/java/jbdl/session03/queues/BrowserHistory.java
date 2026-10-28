package jbdl.session03.queues;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

/**
 * Deque (double-ended queue) used as two stacks: back and forward navigation. ArrayDeque is the
 * recommended stack in Java; the old Stack class is synchronized and extends Vector.
 */
public class BrowserHistory {

    private final Deque<String> back = new ArrayDeque<>();
    private final Deque<String> forward = new ArrayDeque<>();
    private String current;

    public BrowserHistory(String homePage) {
        this.current = homePage;
    }

    public void visit(String page) {
        back.push(current); // push = addFirst: the stack top is the front
        current = page;
        forward.clear();    // a new visit drops the forward history, like a real browser
    }

    public Optional<String> back() {
        if (back.isEmpty()) {
            return Optional.empty();
        }
        forward.push(current);
        current = back.pop();
        return Optional.of(current);
    }

    public Optional<String> forward() {
        if (forward.isEmpty()) {
            return Optional.empty();
        }
        back.push(current);
        current = forward.pop();
        return Optional.of(current);
    }

    public String current() {
        return current;
    }
}
