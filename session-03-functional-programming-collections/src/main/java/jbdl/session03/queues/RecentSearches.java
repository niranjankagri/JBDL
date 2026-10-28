package jbdl.session03.queues;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * A bounded Deque: newest search first, at most `capacity` entries, no duplicates. Adds at the front
 * and drops from the back, the two operations a Deque makes O(1).
 */
public class RecentSearches {

    private final int capacity;
    private final Deque<String> searches = new ArrayDeque<>();

    public RecentSearches(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.capacity = capacity;
    }

    /** Newest at the front; O(n) remove for the duplicate check, O(1) at both ends. */
    public void add(String query) {
        searches.remove(query);   // a repeated search moves to the front instead of appearing twice
        searches.addFirst(query);
        if (searches.size() > capacity) {
            searches.removeLast();
        }
    }

    /** A copy, so callers can't change the history from outside. */
    public List<String> newestFirst() {
        return List.copyOf(searches);
    }
}
