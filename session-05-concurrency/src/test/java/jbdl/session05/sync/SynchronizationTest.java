package jbdl.session05.sync;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import jbdl.session05.sync.Accounts.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Topic 3: the three safe counters never lose an increment, two-way transfers finish without
 * deadlock and keep the total, and a BlockingQueue hands over every item in order.
 * The unsafe counter is not asserted: its result is random (see Counters.main).
 */
@Timeout(30)
class SynchronizationTest {

    private static final int THREADS = 8;
    private static final int PER_THREAD = 50_000;

    @ParameterizedTest(name = "{0} loses no increments")
    @ValueSource(strings = {"SynchronizedCounter", "AtomicCounter", "LockCounter"})
    void safeCountersAreExact(String kind) throws InterruptedException {
        Counters.Counter counter = switch (kind) {
            case "SynchronizedCounter" -> new Counters.SynchronizedCounter();
            case "AtomicCounter" -> new Counters.AtomicCounter();
            default -> new Counters.LockCounter();
        };

        assertThat(Counters.hammer(counter, THREADS, PER_THREAD)).isEqualTo(THREADS * PER_THREAD);
    }

    @Test
    @DisplayName("Transfers in both directions at once: no deadlock, money is conserved")
    void transfersWithoutDeadlock() throws InterruptedException {
        Account a = new Account(1, 1_000_000);
        Account b = new Account(2, 1_000_000);
        ExecutorService pool = Executors.newFixedThreadPool(8);

        // half the transfers go A -> B, half B -> A, at the same time: the classic deadlock setup
        IntStream.range(0, 10_000).forEach(i -> pool.execute(() -> {
            if (i % 2 == 0) {
                Accounts.transfer(a, b, 1);
            } else {
                Accounts.transfer(b, a, 1);
            }
        }));
        pool.shutdown();

        assertThat(pool.awaitTermination(20, TimeUnit.SECONDS)).as("finished, no deadlock").isTrue();
        assertThat(a.getBalance() + b.getBalance()).isEqualTo(2_000_000);
        assertThat(a.getBalance()).isEqualTo(1_000_000); // 5,000 each way
    }

    @Test
    @DisplayName("A transfer larger than the balance is refused and changes nothing")
    void insufficientFunds() {
        Account a = new Account(1, 100);
        Account b = new Account(2, 0);

        assertThatThrownBy(() -> Accounts.transfer(a, b, 500)).isInstanceOf(IllegalStateException.class);
        assertThat(a.getBalance()).isEqualTo(100);
        assertThat(b.getBalance()).isZero();
    }

    @Test
    @DisplayName("BlockingQueue producer-consumer: every item, in order, through a queue of 2")
    void producerConsumer() throws InterruptedException {
        assertThat(ProducerConsumer.run(100, 2))
                .hasSize(100)
                .startsWith(10, 20, 30)
                .endsWith(1_000);
    }
}
