package jbdl.session04.hashing;

import java.util.Objects;

/**
 * A small hash map written from scratch to show how java.util.HashMap works inside:
 * - an array of buckets whose length is always a power of two,
 * - hash(key) = hashCode with its high bits mixed into the low bits,
 * - bucket index = hash & (length - 1)   (a fast modulo for powers of two),
 * - collisions chained in a linked list per bucket (HashMap turns long chains into trees),
 * - the array doubles when size exceeds length * 0.75 (the load factor) and every entry is rehashed.
 */
public class SimpleHashMap<K, V> {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    /** One entry; `next` links entries that landed in the same bucket (a collision chain). */
    private static final class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;

        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets = newBuckets(INITIAL_CAPACITY);
    private int size;
    private int resizes;

    @SuppressWarnings({"unchecked", "rawtypes"}) // generic arrays can't be created directly
    private static <K, V> Node<K, V>[] newBuckets(int capacity) {
        return (Node<K, V>[]) new Node[capacity];
    }

    /** Same spreading step as java.util.HashMap: XOR the top 16 bits into the bottom 16. */
    public static int hash(Object key) {
        int h = Objects.hashCode(key);
        return h ^ (h >>> 16);
    }

    /** Same as hash mod capacity, because capacity is always a power of two. */
    public static int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }

    /** Adds or replaces; returns the old value or null, like java.util.HashMap.put. */
    public V put(K key, V value) {
        int hash = hash(key);
        int index = indexFor(hash, buckets.length);
        for (Node<K, V> node = buckets[index]; node != null; node = node.next) {
            // cheap hash comparison first, equals only when hashes match
            if (node.hash == hash && Objects.equals(node.key, key)) {
                V old = node.value;
                node.value = value;
                return old;
            }
        }
        // new key: put it at the head of the bucket's chain, then grow the table if it is too full
        buckets[index] = new Node<>(hash, key, value, buckets[index]);
        if (++size > buckets.length * LOAD_FACTOR) {
            resize();
        }
        return null;
    }

    /** Looks only in the one bucket the key's hash points to. */
    public V get(K key) {
        int hash = hash(key);
        for (Node<K, V> node = buckets[indexFor(hash, buckets.length)]; node != null; node = node.next) {
            if (node.hash == hash && Objects.equals(node.key, key)) {
                return node.value;
            }
        }
        return null;
    }

    /** Doubles the table and puts every entry into its bucket in the bigger table (rehashing). */
    private void resize() {
        Node<K, V>[] old = buckets;
        buckets = newBuckets(old.length * 2);
        for (Node<K, V> head : old) {
            for (Node<K, V> node = head; node != null; node = node.next) {
                int index = indexFor(node.hash, buckets.length);
                buckets[index] = new Node<>(node.hash, node.key, node.value, buckets[index]);
            }
        }
        resizes++;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    public int resizes() {
        return resizes;
    }

    /** Length of the longest chain: 1-2 with a good hashCode, n with a terrible one. */
    public int longestChain() {
        int longest = 0;
        for (Node<K, V> head : buckets) {
            int length = 0;
            for (Node<K, V> node = head; node != null; node = node.next) {
                length++;
            }
            longest = Math.max(longest, length);
        }
        return longest;
    }
}
