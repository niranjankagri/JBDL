package jbdl.session03.queues;

import static org.assertj.core.api.Assertions.assertThat;

import jbdl.session03.queues.SupportQueue.Severity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 4: PriorityQueue order with a tie-breaker, null instead of an exception on an empty queue,
 * ArrayDeque as two stacks, and a bounded most-recent-first deque.
 */
class QueuesTest {

    @Test
    @DisplayName("PriorityQueue: most severe first, equal severity in arrival order")
    void priorityWithTieBreaker() {
        SupportQueue queue = new SupportQueue();
        queue.submit("typo on page", Severity.LOW);
        queue.submit("payments down", Severity.CRITICAL);
        queue.submit("slow search", Severity.HIGH);
        queue.submit("login down", Severity.CRITICAL);

        assertThat(queue.next().title()).isEqualTo("payments down"); // peek does not remove
        assertThat(queue.size()).isEqualTo(4);
        assertThat(queue.drainTitles()).containsExactly("payments down", "login down", "slow search", "typo on page");
    }

    @Test
    @DisplayName("peek and poll return null on an empty queue instead of throwing")
    void emptyQueue() {
        SupportQueue queue = new SupportQueue();

        assertThat(queue.next()).isNull();
        assertThat(queue.take()).isNull();
    }

    @Test
    @DisplayName("Deque as two stacks: back, forward, and a new visit clears forward")
    void browserHistory() {
        BrowserHistory history = new BrowserHistory("home");
        history.visit("search");
        history.visit("result");

        assertThat(history.back()).contains("search");
        assertThat(history.back()).contains("home");
        assertThat(history.back()).isEmpty();
        assertThat(history.forward()).contains("search");

        history.visit("other");
        assertThat(history.forward()).isEmpty();
        assertThat(history.current()).isEqualTo("other");
    }

    @Test
    @DisplayName("Bounded Deque: newest first, repeats move to the front, oldest dropped")
    void recentSearches() {
        RecentSearches recent = new RecentSearches(3);
        recent.add("java");
        recent.add("spring");
        recent.add("kafka");
        recent.add("java");
        recent.add("redis");

        assertThat(recent.newestFirst()).containsExactly("redis", "java", "kafka");
    }
}
