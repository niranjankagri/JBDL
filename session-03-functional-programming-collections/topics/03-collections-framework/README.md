# Topic 3: Overview of the Java Collections Framework

```
Iterable
└── Collection
    ├── List      ordered, index-based, duplicates OK      ArrayList, LinkedList
    ├── Set       no duplicates                            HashSet, LinkedHashSet, TreeSet (SortedSet)
    └── Queue     process in an order                      PriorityQueue, ArrayDeque, LinkedList (topic 4)
        └── Deque     both ends

Map (not a Collection): key → value, unique keys           HashMap, LinkedHashMap, TreeMap (SortedMap)
```

## Which one?

| Need | Use | Why |
|---|---|---|
| A list, mostly read by index or appended | `ArrayList` | Array inside: O(1) `get(i)`, cache friendly |
| Many inserts/removes at the ends | `ArrayDeque` | O(1) at both ends; `LinkedList` is rarely the best choice |
| Unique items, order doesn't matter | `HashSet` | O(1) `contains` |
| Unique items in insertion order | `LinkedHashSet` | Hash set + linked list of entries |
| Unique items kept sorted | `TreeSet` | Red-black tree: O(log n), `first()`, `headSet()` |
| Key → value lookup | `HashMap` | O(1) average (Session 4 explains hashing) |
| Same, in insertion (or access) order | `LinkedHashMap` | Also the basis of an LRU cache |
| Same, sorted by key | `TreeMap` | O(log n), `floorKey`, `subMap` |

The tests feed `[30, 10, 20, 10]` to every implementation and check what each keeps:

| Implementation | Result |
|---|---|
| `ArrayList`, `LinkedList` | `[30, 10, 20, 10]` (order + duplicates) |
| `HashSet` | `{10, 20, 30}` in no promised order |
| `LinkedHashSet` | `[30, 10, 20]` |
| `TreeSet` | `[10, 20, 30]` |

## Ordering: Comparable vs Comparator

| | `Comparable<T>` | `Comparator<T>` |
|---|---|---|
| Method | `compareTo(T other)` in the class | `compare(T a, T b)` outside the class |
| How many orders | One "natural" order | As many as you like |
| Used by | `Collections.sort(list)`, `TreeSet`, `TreeMap` by default | `list.sort(cmp)`, `new TreeSet<>(cmp)` |
| Example | `Student` by roll number | `comparingDouble(Student::marks).reversed().thenComparing(Student::name)` |

## Iterating and removing

- `for (x : list)` uses an `Iterator`. Removing through the **list** while iterating makes the
  iterator throw `ConcurrentModificationException` on its next step ("fail-fast").
- **Quirk the tests show:** removing the **second-to-last** element does *not* throw. The size
  drops, `hasNext()` returns false, the loop ends, and the last element is never checked. A silent
  bug, so never rely on the exception.
- Safe: `iterator.remove()` or simply `list.removeIf(predicate)`.

## Immutable collections

`List.of`, `Set.of`, `Map.of` (Java 9) and `List.copyOf` give unmodifiable collections: `add` throws
`UnsupportedOperationException`, and `null` elements aren't allowed. `Collections.unmodifiableList`
is only a read-only *view*: changes to the original list still show through it.

## The code

| File | What it shows |
|---|---|
| [`CollectionExamples.java`](../../src/main/java/jbdl/session03/collections/CollectionExamples.java) | (2024 code, upgraded) every List / Set / Map / Queue implementation on the same input |
| [`CollectionsOverview.java`](../../src/main/java/jbdl/session03/collections/CollectionsOverview.java) | `Comparable` vs `Comparator` chains, the for-each removal trap and the safe ways |
| [`CollectionExamplesTest.java`](../../src/test/java/jbdl/session03/collections/CollectionExamplesTest.java), [`CollectionsOverviewTest.java`](../../src/test/java/jbdl/session03/collections/CollectionsOverviewTest.java) | Ordering, de-duplication, sorting, the exception, the second-to-last quirk, immutability |

## Run it

```bash
java -cp session-03-functional-programming-collections/target/classes jbdl.session03.collections.CollectionExamples
```

## Interview questions

- **`ArrayList` vs `LinkedList`?** Array vs doubly linked nodes. `ArrayList` wins for almost everything; `LinkedList` only for many removals through an iterator.
- **`HashSet` vs `TreeSet`?** O(1) unordered vs O(log n) sorted.
- **`Collection` vs `Collections`?** The root interface vs a class of static helpers (`sort`, `unmodifiableList`, `synchronizedList`).
- **Fail-fast vs fail-safe iterators?** Fail-fast (`ArrayList`) throws on concurrent change; fail-safe (`CopyOnWriteArrayList`, `ConcurrentHashMap`) iterate over a snapshot or tolerate changes.
- **Why isn't `Map` a `Collection`?** It holds pairs, not single elements; `keySet()`, `values()` and `entrySet()` are the collection views.
