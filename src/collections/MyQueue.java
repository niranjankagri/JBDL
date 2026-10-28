package collections;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MyQueue {

    public static void main(String[] args) {
        Queue<Integer> linkedQueue = new LinkedList<>();
        linkedQueue.add(1);
        linkedQueue.offer(1);
        linkedQueue.size();
        linkedQueue.isEmpty();
        linkedQueue.poll();
        linkedQueue.peek();

        Queue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(1);
        priorityQueue.offer(1);
        priorityQueue.size();
        priorityQueue.isEmpty();
        priorityQueue.poll();
        priorityQueue.peek();

        Queue<Integer> arrayDeque = new ArrayDeque<>();
        arrayDeque.add(1);
        arrayDeque.offer(1);
        arrayDeque.size();
        arrayDeque.isEmpty();
        arrayDeque.poll();
        arrayDeque.peek();

    }
}
