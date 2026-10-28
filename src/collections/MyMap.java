package collections;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyMap {

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = new HashMap<>();        //Unordered map
        hashMap.put(1, 1);
        hashMap.remove(1);

        Map<Integer, Integer> linkedHashMap = new LinkedHashMap<>();        // Ordered Map
        linkedHashMap.put(1, 1);
        linkedHashMap.remove(1);

        Map<Integer, Integer> treeMap = new TreeMap<>();        // Sorted map
        treeMap.put(1, 1);
        treeMap.remove(1);
    }
}
