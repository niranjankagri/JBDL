package collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class MySet {

    public static void main(String[] args) {
        Set<Integer> hashSet = new HashSet<>();     // Unordered set
        hashSet.add(1);
        hashSet.remove(1);

        Set<Integer> linkedHashSet = new LinkedHashSet<>();     // Ordered set
        linkedHashSet.add(1);
        linkedHashSet.remove(1);

        Set<Integer> treeSet = new TreeSet<>();     // Sorted set
        treeSet.add(1);
        treeSet.remove(1);

    }
}
