package jbdl.session04.hashing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashingTest {

    @Test
    @DisplayName("Our String hash formula equals String.hashCode")
    void stringHashFormula() {
        for (String text : new String[] {"", "a", "Java", "Niranjan Kumar Agri"}) {
            assertThat(HashingFacts.stringHash(text)).isEqualTo(text.hashCode());
        }
        assertThat("Aa".hashCode()).isEqualTo("BB".hashCode()); // a real collision: different strings, same hash
    }

    @Test
    @DisplayName("Bucket index = spread hash & (capacity - 1)")
    void bucketIndex() {
        int hash = SimpleHashMap.hash("Java");

        assertThat(SimpleHashMap.indexFor(hash, 16)).isEqualTo(Math.floorMod(hash, 16)).isBetween(0, 15);
    }

    @Test
    @DisplayName("put/get/replace work; the table doubles past the 0.75 load factor")
    void putGetResize() {
        SimpleHashMap<String, Integer> map = new SimpleHashMap<>();
        for (int i = 0; i < 13; i++) {
            map.put("key" + i, i);
        }

        assertThat(map.capacity()).isEqualTo(32);          // 13 > 16 * 0.75 = 12 -> resized once
        assertThat(map.resizes()).isEqualTo(1);
        assertThat(map.get("key7")).isEqualTo(7);
        assertThat(map.put("key7", 70)).isEqualTo(7);       // replace returns the old value
        assertThat(map.get("key7")).isEqualTo(70);
        assertThat(map.size()).isEqualTo(13);
        assertThat(map.get("missing")).isNull();
        assertThat(map.longestChain()).isLessThanOrEqualTo(3);
    }

    @Test
    @DisplayName("A constant hashCode is still correct, but every key lands in one chain")
    void badHashCodeCollides() {
        SimpleHashMap<HashingFacts.BadKey, Integer> map = new SimpleHashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(new HashingFacts.BadKey("k" + i), i);
        }

        assertThat(map.get(new HashingFacts.BadKey("k42"))).isEqualTo(42); // equals still finds it
        assertThat(map.longestChain()).isEqualTo(100);                     // but lookups are now O(n)
    }

    @Test
    @DisplayName("Changing a key after inserting it makes the entry unreachable")
    void mutableKeyIsLost() {
        Map<HashingFacts.MutableKey, String> map = new HashMap<>();
        HashingFacts.MutableKey key = new HashingFacts.MutableKey("before");
        map.put(key, "value");

        key.rename("after");

        assertThat(map.get(key)).isNull();          // looks in the bucket of the NEW hash
        assertThat(map.containsValue("value")).isTrue(); // the entry is still there, lost
    }

    @Test
    @DisplayName("SHA-256 gives the known 64-hex-digit digest")
    void sha256() {
        assertThat(HashingFacts.sha256Hex("abc"))
                .isEqualTo("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad");
        assertThat(HashingFacts.sha256Hex("abd")).isNotEqualTo(HashingFacts.sha256Hex("abc"));
    }
}
