# Topic 2: Thread pools and join

## join

`thread.join()` makes the calling thread wait until `thread` has finished. Two effects:

1. **Ordering:** start t1, join, start t2, join → t2 begins only after t1 ends (`runOneAfterAnother`).
2. **Visibility:** everything the thread wrote before ending is visible after `join` returns.
   `squaresWithJoin` fills an array from 6 threads; after joining all of them, every slot is
   guaranteed to be seen. That's a *happens-before* rule of the Java Memory Model.

`join(millis)` waits at most that long.

## Why thread pools

Creating a thread costs memory (a stack, ~1 MB reserved) and time. One thread per request means a
traffic spike can create thousands and crash the server. A **pool** keeps a fixed set of threads alive
and gives them tasks from a queue:

```
submit(task) ─▶ [ task | task | task | ... ]  work queue
                     │       │       │
                  thread-1 thread-2 thread-3   (reused for every task)
```

The test submits 20 tasks to a pool of 3: all 20 run, and only 3 thread names ever appear.

## Kinds of pools (`Executors` factory methods)

| Factory | Threads | Queue | Use for |
|---|---|---|---|
| `newFixedThreadPool(n)` | exactly n | unbounded `LinkedBlockingQueue` | Steady CPU or mixed work |
| `newCachedThreadPool()` | 0 … unlimited, idle ones die after 60 s | none (`SynchronousQueue`) | Many short tasks; risky under load |
| `newSingleThreadExecutor()` | 1 | unbounded | Tasks that must run in order |
| `newScheduledThreadPool(n)` | n | delay queue | Run later / periodically |
| `newVirtualThreadPerTaskExecutor()` (Java 21) | one virtual thread per task | none | Many blocking I/O tasks |

In production, prefer `new ThreadPoolExecutor(core, max, keepAlive, unit, new ArrayBlockingQueue<>(capacity), rejectionPolicy)`:
a **bounded** queue and an explicit rejection policy, so overload is refused instead of filling memory.
Size: about the number of cores for CPU work, more for I/O-bound work.

## Shutting down

```java
pool.shutdown();                                   // no new tasks; queued ones still run
if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
    pool.shutdownNow();                            // interrupt running tasks, drop the queue
}
```

A pool that is never shut down keeps its (non-daemon) threads alive, and the JVM doesn't exit.

## The code

| File | What it shows |
|---|---|
| [`ThreadPools.java`](../../src/main/java/jbdl/session05/pools/ThreadPools.java) | Thread reuse in a fixed pool, a `CountDownLatch` as "join for tasks", the pool's parts, `join` with results, the shutdown recipe |
| [`ThreadPoolsTest.java`](../../src/test/java/jbdl/session05/pools/ThreadPoolsTest.java) | ≤ 3 threads for 20 tasks; core/max/queue of a fixed pool; join results |

## Interview questions

- **What does `join` do?** Waits for a thread to finish; afterwards its writes are visible.
- **Why use a thread pool?** Reuse threads, bound concurrency, queue extra work.
- **`shutdown` vs `shutdownNow`?** Finish queued tasks vs interrupt and return the queued ones.
- **Danger of `newFixedThreadPool`?** Its queue is unbounded: under overload it grows until memory runs out.
- **How many threads?** ≈ cores for CPU-bound; cores × (1 + wait time / compute time) for I/O-bound.
