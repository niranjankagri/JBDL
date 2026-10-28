# Session 4: Java Advanced Features

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Generics in Java | [`generics`](src/main/java/jbdl/session04/generics) | [01-generics](topics/01-generics/README.md) |
| 2 | The Streams API | [`streams`](src/main/java/jbdl/session04/streams) | [02-streams-api](topics/02-streams-api/README.md) |
| 3 | Working with HashMap | [`hashmap`](src/main/java/jbdl/session04/hashmap) | [03-hashmap](topics/03-hashmap/README.md) |
| 4 | Hashing algorithms | [`hashing`](src/main/java/jbdl/session04/hashing) | [04-hashing](topics/04-hashing/README.md) |
| 5 | Sequential vs parallel streams | [`parallel`](src/main/java/jbdl/session04/parallel) | [05-sequential-vs-parallel-streams](topics/05-sequential-vs-parallel-streams/README.md) |

## What you can do after this session

- Write generic classes and methods with bounds and wildcards (PECS), and explain type erasure.
- Build reports with streams: `filter`, `map`, `flatMap`, `groupingBy`, `partitioningBy`, `joining`, and explain laziness.
- Use `merge`, `computeIfAbsent`, `getOrDefault` and build an LRU cache with `LinkedHashMap`.
- Explain how `HashMap` finds a key (hash spreading, buckets, chains, resize) and why keys must be immutable.
- Decide when a parallel stream helps, and avoid shared mutable state in one.

## Run it

```bash
./mvnw -q -pl session-04-java-advanced-features verify
java -cp session-04-java-advanced-features/target/classes jbdl.session04.parallel.ParallelStreams
```
