package jbdl.session03.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * The 2024 MyList/MySet/MyMap/MyQueue classes called add, size and peek and threw the results away,
 * so they showed nothing. Here every implementation gets the same input and returns what it keeps,
 * which makes the difference between them visible (and testable).
 */
public final class CollectionExamples {

    private CollectionExamples() {
    }

    /**
     * ArrayList (array) and LinkedList (linked nodes) both keep insertion order and duplicates.
     * The Supplier parameter lets the caller choose the list type: fillList(ArrayList::new, ...) means
     * "create a new ArrayList" (a constructor reference, Session 3 topic 1). Without it we'd need one
     * method per type: fillArrayList(...), fillLinkedList(...)...
     */
    public static List<Integer> fillList(Supplier<List<Integer>> listType, int... values) {
        return fill(listType.get(), values);
    }

    /**
     * Sets drop duplicates. HashSet promises no order, LinkedHashSet keeps insertion order,
     * TreeSet keeps the values sorted.
     */
    public static Set<Integer> fillSet(Supplier<Set<Integer>> setType, int... values) {
        return fill(setType.get(), values);
    }

    /** Maps the same way: HashMap no order, LinkedHashMap insertion order, TreeMap sorted keys. */
    public static Map<Integer, String> fillMap(Supplier<Map<Integer, String>> mapType, int... keys) {
        Map<Integer, String> map = mapType.get();
        for (int key : keys) {
            map.put(key, "value " + key);
        }
        return map;
    }

    /**
     * Polls the queue empty and returns the order elements came out: FIFO for LinkedList and
     * ArrayDeque, smallest first for PriorityQueue.
     */
    public static List<Integer> drainQueue(Supplier<Queue<Integer>> queueType, int... values) {
        Queue<Integer> queue = fill(queueType.get(), values);
        List<Integer> removed = new ArrayList<>();
        while (!queue.isEmpty()) {
            removed.add(queue.poll());
        }
        return removed;
    }

    private static <C extends Collection<Integer>> C fill(C collection, int... values) {
        for (int value : values) {
            collection.add(value);
        }
        return collection;
    }

    public static void main(String[] args) {
        int[] input = {30, 10, 20, 10};
        System.out.println("Input:         " + Arrays.toString(input));
        System.out.println("ArrayList:     " + fillList(ArrayList::new, input));
        System.out.println("LinkedList:    " + fillList(LinkedList::new, input));
        System.out.println("HashSet:       " + fillSet(HashSet::new, input));
        System.out.println("LinkedHashSet: " + fillSet(LinkedHashSet::new, input));
        System.out.println("TreeSet:       " + fillSet(TreeSet::new, input));
        System.out.println("HashMap keys:  " + fillMap(HashMap::new, input).keySet());
        System.out.println("LinkedHashMap: " + fillMap(LinkedHashMap::new, input).keySet());
        System.out.println("TreeMap keys:  " + fillMap(TreeMap::new, input).keySet());
        System.out.println("LinkedList queue polls:    " + drainQueue(LinkedList::new, input));
        System.out.println("ArrayDeque polls:          " + drainQueue(ArrayDeque::new, input));
        System.out.println("PriorityQueue polls:       " + drainQueue(PriorityQueue::new, input));
    }
}
