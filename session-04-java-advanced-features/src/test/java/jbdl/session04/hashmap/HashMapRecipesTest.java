package jbdl.session04.hashmap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HashMapRecipesTest {

    @Test
    @DisplayName("merge counts words")
    void wordCounts() {
        assertThat(HashMapRecipes.wordCounts("To be, or not to be!"))
                .containsExactly(entry("be", 2), entry("not", 1), entry("or", 1), entry("to", 2));
    }

    @Test
    @DisplayName("computeIfAbsent groups values under a new list per key")
    void grouping() {
        assertThat(HashMapRecipes.byFirstLetter(List.of("kafka", "java", "jdbc", "kotlin", "redis")))
                .containsExactly(entry('j', List.of("java", "jdbc")), entry('k', List.of("kafka", "kotlin")),
                        entry('r', List.of("redis")));
    }

    @Test
    @DisplayName("getOrDefault doesn't insert; putIfAbsent keeps the first value")
    void defaultsAndPutIfAbsent() {
        Map<String, Integer> stock = new HashMap<>(Map.of("pen", 5));
        Map<String, String> owners = new HashMap<>();

        assertThat(HashMapRecipes.stockOf(stock, "pencil")).isZero();
        assertThat(stock).doesNotContainKey("pencil");
        assertThat(HashMapRecipes.registerOnce(owners, "nk", "Niranjan")).isEqualTo("Niranjan");
        assertThat(HashMapRecipes.registerOnce(owners, "nk", "Someone else")).isEqualTo("Niranjan");
    }

    @Test
    @DisplayName("HashMap allows one null key and null values")
    void nulls() {
        Map<String, String> map = HashMapRecipes.withNullKey();

        assertThat(map.get(null)).isEqualTo("no key");
        assertThat(map).containsKey("k");
        assertThat(map.get("k")).isNull();
    }

    @Test
    @DisplayName("LinkedHashMap in access order makes an LRU cache")
    void lruCache() {
        Map<String, String> cache = HashMapRecipes.lruCache(2);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.get("a");        // a is now the most recently used
        cache.put("c", "3");   // full: evicts b, the least recently used

        assertThat(cache.keySet()).containsExactly("a", "c");
    }
}
