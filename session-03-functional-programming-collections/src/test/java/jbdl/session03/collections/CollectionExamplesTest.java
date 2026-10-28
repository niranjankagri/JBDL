package jbdl.session03.collections;

import static jbdl.session03.collections.CollectionExamples.drainQueue;
import static jbdl.session03.collections.CollectionExamples.fillList;
import static jbdl.session03.collections.CollectionExamples.fillMap;
import static jbdl.session03.collections.CollectionExamples.fillSet;
import static jbdl.session03.testdata.TestData.NUMBERS;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.TreeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 3: the 2024 collection examples. Every implementation gets the same input [30, 10, 20, 10],
 * so the differences in order, duplicates and sorting are visible in the expected values.
 */
class CollectionExamplesTest {

    @Test
    @DisplayName("Lists keep insertion order and duplicates")
    void listsKeepOrderAndDuplicates() {
        assertThat(fillList(ArrayList::new, NUMBERS)).containsExactly(30, 10, 20, 10);
        assertThat(fillList(LinkedList::new, NUMBERS)).containsExactly(30, 10, 20, 10);
    }

    @Test
    @DisplayName("Sets drop duplicates; LinkedHashSet keeps insertion order, TreeSet sorts")
    void setsDropDuplicates() {
        assertThat(fillSet(HashSet::new, NUMBERS)).containsExactlyInAnyOrder(10, 20, 30);
        assertThat(fillSet(LinkedHashSet::new, NUMBERS)).containsExactly(30, 10, 20);
        assertThat(fillSet(TreeSet::new, NUMBERS)).containsExactly(10, 20, 30);
    }

    @Test
    @DisplayName("Maps keep one entry per key; LinkedHashMap insertion order, TreeMap sorted keys")
    void mapsKeyOrder() {
        assertThat(fillMap(HashMap::new, NUMBERS)).hasSize(3).containsEntry(10, "value 10");
        assertThat(fillMap(LinkedHashMap::new, NUMBERS).keySet()).containsExactly(30, 10, 20);
        assertThat(fillMap(TreeMap::new, NUMBERS).keySet()).containsExactly(10, 20, 30);
    }

    @Test
    @DisplayName("Queues: FIFO for LinkedList/ArrayDeque, smallest first for PriorityQueue")
    void queueOrder() {
        assertThat(drainQueue(LinkedList::new, NUMBERS)).containsExactly(30, 10, 20, 10);
        assertThat(drainQueue(ArrayDeque::new, NUMBERS)).containsExactly(30, 10, 20, 10);
        assertThat(drainQueue(PriorityQueue::new, NUMBERS)).containsExactly(10, 10, 20, 30);
    }
}
