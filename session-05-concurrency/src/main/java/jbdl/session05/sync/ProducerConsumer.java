package jbdl.session05.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Producer-consumer with a BlockingQueue: the queue does the waiting (put blocks when full, take
 * blocks when empty), so no wait/notify code is needed. A "poison pill" tells the consumer to stop.
 */
public final class ProducerConsumer {

    private static final int POISON_PILL = -1;

    private ProducerConsumer() {
    }

    /** Produces 1..items through a queue of the given capacity; returns what the consumer made of them. */
    public static List<Integer> run(int items, int capacity) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(capacity);
        List<Integer> consumed = new ArrayList<>();

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= items; i++) {
                    queue.put(i);          // waits while the queue is full
                }
                queue.put(POISON_PILL);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "producer");

        Thread consumer = new Thread(() -> {
            try {
                for (int item = queue.take(); item != POISON_PILL; item = queue.take()) { // waits while empty
                    consumed.add(item * 10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "consumer");

        producer.start();
        consumer.start();
        producer.join();
        consumer.join(); // after join, the consumer's writes to `consumed` are visible here
        return consumed;
    }
}
