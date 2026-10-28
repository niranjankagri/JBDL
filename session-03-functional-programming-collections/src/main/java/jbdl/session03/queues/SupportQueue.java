package jbdl.session03.queues;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * PriorityQueue with a Comparator: support tickets come out most severe first; tickets of equal
 * severity keep their arrival order (a PriorityQueue alone does NOT guarantee that, hence the
 * sequence number as a tie-breaker).
 */
public final class SupportQueue {

    public enum Severity { CRITICAL, HIGH, LOW } // declaration order = priority order

    public record Ticket(String title, Severity severity, long sequence) {
    }

    // By severity first; for the same severity, the lower sequence number (earlier ticket) first.
    // Same as: int c = a.severity().compareTo(b.severity()); return c != 0 ? c : Long.compare(a.sequence(), b.sequence());
    private static final Comparator<Ticket> MOST_URGENT_FIRST =
            Comparator.comparing(Ticket::severity).thenComparingLong(Ticket::sequence);

    private final Queue<Ticket> tickets = new PriorityQueue<>(MOST_URGENT_FIRST);
    private long nextSequence;

    /** offer: adds; returns false instead of throwing if a bounded queue is full (add would throw). */
    public void submit(String title, Severity severity) {
        tickets.offer(new Ticket(title, severity, nextSequence++));
    }

    /** peek: looks at the head without removing it; null when empty (element() would throw). */
    public Ticket next() {
        return tickets.peek();
    }

    /** poll: removes and returns the head; null when empty (remove() would throw). */
    public Ticket take() {
        return tickets.poll();
    }

    /** Polls until empty, so the titles come out in priority order (iteration would not be sorted). */
    public List<String> drainTitles() {
        List<String> titles = new ArrayList<>();
        for (Ticket ticket = tickets.poll(); ticket != null; ticket = tickets.poll()) {
            titles.add(ticket.title());
        }
        return titles;
    }

    public int size() {
        return tickets.size();
    }
}
