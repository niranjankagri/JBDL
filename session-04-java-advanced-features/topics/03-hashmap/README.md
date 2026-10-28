# Topic 3: Working with HashMap

`HashMap<K, V>` stores key → value pairs with O(1) average `put`, `get` and `remove`. Keys are unique;
one `null` key and any number of `null` values are allowed. Iteration order is **not** defined
(use `LinkedHashMap` for insertion order, `TreeMap` for sorted keys). How it finds a key in O(1) is
topic 4.

## Methods that replace boilerplate

| Instead of | Write | Example ([`HashMapRecipes`](../../src/main/java/jbdl/session04/hashmap/HashMapRecipes.java)) |
|---|---|---|
| `if (!map.containsKey(w)) map.put(w, 1); else map.put(w, map.get(w) + 1);` | `map.merge(w, 1, Integer::sum)` | `wordCounts("To be, or not to be!")` → `{be=2, not=1, or=1, to=2}` |
| `List l = map.get(k); if (l == null) { l = new ArrayList<>(); map.put(k, l); } l.add(x);` | `map.computeIfAbsent(k, key -> new ArrayList<>()).add(x)` | `byFirstLetter` → `{j=[java, jdbc], k=[kafka, kotlin], r=[redis]}` |
| `map.containsKey(k) ? map.get(k) : 0` | `map.getOrDefault(k, 0)` | `stockOf` (doesn't insert the key) |
| `if (!map.containsKey(k)) map.put(k, v);` | `map.putIfAbsent(k, v)` | `registerOnce`: the first owner wins |

Also useful: `compute`, `computeIfPresent`, `replaceAll`, `entrySet()` for iterating keys and values
together (`for (Map.Entry<K, V> e : map.entrySet())`), and `Map.of(...)` for small immutable maps.

## LRU cache with LinkedHashMap

```java
new LinkedHashMap<>(16, 0.75f, true) {                    // true = access order
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;                          // evict the least recently used
    }
};
```

Every `get` moves the entry to the end; when full, the first (least recently used) entry goes. The
test fills a 2-entry cache with `a`, `b`, reads `a`, adds `c`: `b` is evicted. Session 15 replaces
this with Redis.

## HashMap vs the others

| | `HashMap` | `LinkedHashMap` | `TreeMap` | `ConcurrentHashMap` | `Hashtable` (legacy) |
|---|---|---|---|---|---|
| Order | none | insertion / access | sorted keys | none | none |
| `get`/`put` | O(1) | O(1) | O(log n) | O(1) | O(1) |
| `null` key | 1 allowed | 1 allowed | no | no | no |
| Thread-safe | no | no | no | **yes** (Session 5) | yes, but locks everything |

## The code

| File | What it shows |
|---|---|
| [`HashMapRecipes.java`](../../src/main/java/jbdl/session04/hashmap/HashMapRecipes.java) | `merge`, `computeIfAbsent`, `getOrDefault`, `putIfAbsent`, nulls, LRU cache |
| [`HashMapRecipesTest.java`](../../src/test/java/jbdl/session04/hashmap/HashMapRecipesTest.java) | Each recipe, and the LRU eviction order |

## Interview questions

- **`HashMap` vs `Hashtable`?** Not synchronized vs synchronized; `HashMap` allows `null`. Use `ConcurrentHashMap` instead of `Hashtable`.
- **`HashMap` vs `TreeMap`?** O(1) unordered vs O(log n) sorted with navigation methods.
- **How do you iterate a map?** `entrySet()` (key and value together), `keySet()`, `values()`, or `forEach((k, v) -> ...)`.
- **What happens on `put` with an existing key?** The value is replaced and the old one returned.
