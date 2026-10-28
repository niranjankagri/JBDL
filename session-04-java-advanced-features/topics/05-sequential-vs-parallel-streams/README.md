# Topic 5: Sequential vs parallel streams

`list.stream()` runs every step on the calling thread. `list.parallelStream()` (or `.parallel()`)
**splits** the source into parts, runs them on the **ForkJoinPool common pool** (by default one
thread fewer than the CPU cores; the caller thread helps), and **combines** the partial results.

## Same answer, when the operations allow it

| Works the same in parallel | Breaks or behaves differently |
|---|---|
| Stateless operations: `map`, `filter` | Lambdas that change shared state (`list.add` from `forEach`) |
| Associative reductions: `sum`, `max`, `reduce(0, Integer::sum)` | Non-associative reductions (subtraction, string concatenation with side effects) |
| `collect`, `toList()` (each thread builds its own part, then they're merged) | `forEach` order: any order; use `forEachOrdered` if order matters |
| `findAny`, `anyMatch` | `findFirst`, `limit`, `sorted` on ordered streams: correct but expensive |

## Real output on this machine (12 cores)

```bash
java -cp session-04-java-advanced-features/target/classes jbdl.session04.parallel.ParallelStreams
```

```
sequential sum=2666668666667000000 in 17 ms, threads=1
parallel   sum=2666668666667000000 in 21 ms, threads=12
Unsafe parallel add of 100000 items kept: 12697 (-1 = crashed)
Common pool parallelism: 11
```

- Same sum, but parallel was **slower**: squaring 2 million numbers is too little work per element
  to pay for splitting, scheduling and merging.
- The unsafe version lost 87 % of the items: 12 threads called `ArrayList.add` at once. Each run
  gives a different number (or an `ArrayIndexOutOfBoundsException`). That's why the test suite checks
  only the safe version, and why the demo prints this instead of asserting it.

## When to use parallel

Use it only when **all** are true:

- a lot of data (≥ 10,000s of elements) **and** real CPU work per element,
- a source that splits well (`ArrayList`, arrays, `IntStream.range`; not `LinkedList`, not I/O),
- stateless, non-blocking operations,
- you measured it and it's faster.

Avoid in web servers by default: every parallel stream shares the one common pool, so one slow
request can starve others; blocking calls (database, HTTP) inside a parallel stream block pool threads.

## The code

| File | What it shows |
|---|---|
| [`ParallelStreams.java`](../../src/main/java/jbdl/session04/parallel/ParallelStreams.java) | Same sum both ways, threads used, `forEachOrdered`, safe `toList`, the unsafe shared `ArrayList`, timing demo |
| [`ParallelStreamsTest.java`](../../src/test/java/jbdl/session04/parallel/ParallelStreamsTest.java) | Equal sums, one thread sequentially, common-pool threads in parallel, order kept by `forEachOrdered` and `toList` |

## Interview questions

- **Which pool runs parallel streams?** `ForkJoinPool.commonPool()`; size = cores - 1 (setting: `java.util.concurrent.ForkJoinPool.common.parallelism`).
- **Is parallel always faster?** No. Overhead dominates for small or cheap workloads.
- **Why is `forEach` with `list.add` wrong in parallel?** `ArrayList` isn't thread-safe; use `collect`/`toList`.
- **`findFirst` vs `findAny` in parallel?** `findAny` returns whichever part finishes first: cheaper.
- **How does splitting work?** The source's `Spliterator` divides it; arrays and `ArrayList` split evenly, linked structures don't.
