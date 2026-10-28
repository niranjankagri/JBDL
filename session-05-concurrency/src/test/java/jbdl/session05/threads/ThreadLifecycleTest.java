package jbdl.session05.threads;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Topic 1: a real thread's states, interrupt waking a sleeping thread, and the daemon default.
 */
@Timeout(10)
class ThreadLifecycleTest {

    @Test
    @DisplayName("A thread goes NEW -> WAITING (parked on a latch) -> TERMINATED")
    void states() throws InterruptedException {
        assertThat(ThreadLifecycle.observeStates())
                .containsExactly(Thread.State.NEW, Thread.State.WAITING, Thread.State.TERMINATED);
    }

    @Test
    @DisplayName("interrupt() wakes a sleeping thread with InterruptedException")
    void interrupt() throws InterruptedException {
        assertThat(ThreadLifecycle.interruptSleepingThread()).isEqualTo("interrupted while sleeping");
    }

    @Test
    @DisplayName("New threads inherit non-daemon from the main thread")
    void notDaemon() {
        assertThat(ThreadLifecycle.isDaemonByDefault()).isFalse();
    }
}
