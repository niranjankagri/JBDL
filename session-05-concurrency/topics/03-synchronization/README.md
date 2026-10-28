# Topic 3: Synchronization mechanisms

## The problem: race conditions

`count++` looks like one step but is three: **read** count, **add** 1, **write** it back. Two threads
can both read 41 and both write 42: one increment is lost. Real output of
[`Counters`](../../src/main/java/jbdl/session05/sync/Counters.java), 8 threads × 100,000 increments:

```
Expected:     800000
UnsafeCounter:       117107      <- 85 % of the increments lost
SynchronizedCounter: 800000
AtomicCounter:       800000
LockCounter:         800000
```

(The unsafe number changes every run, which is exactly the problem; the tests check only the safe ones.)

Two things go wrong without synchronization: **atomicity** (steps interleave) and **visibility** (a
thread may keep reading a stale cached value).

## The tools

| Tool | How | When |
|---|---|---|
| `synchronized` method/block | One thread at a time holds the object's **monitor**; also makes writes visible to the next holder | Simple mutual exclusion |
| `volatile` field | Every read sees the latest write; **no** atomicity (`volatile int x; x++` is still a race) | Flags (`running = false`), double-checked locking (Session 2) |
| `AtomicInteger`, `AtomicLong`, `LongAdder` | Lock-free compare-and-swap | Counters, sequences |
| `ReentrantLock` | `lock()` / `unlock()` in `finally`; `tryLock(timeout)`, fairness, several `Condition`s | When `synchronized` isn't flexible enough |
| `ReadWriteLock` | Many readers or one writer | Read-heavy shared data |
| Concurrent collections | `ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue` | Shared collections without your own locks |
| Coordinators | `CountDownLatch`, `Semaphore`, `CyclicBarrier` | Wait for N events; limit N concurrent users |

## Deadlock

Thread 1 locks account A then waits for B; thread 2 locks B then waits for A: both wait forever.
[`Accounts.transfer`](../../src/main/java/jbdl/session05/sync/Accounts.java) always locks the account with the
**lower id first**, whatever the transfer direction. The test runs 10,000 transfers in both directions
on 8 threads: it finishes, and the total money is unchanged.

Four conditions are needed for a deadlock (mutual exclusion, hold and wait, no preemption, circular
wait); a global lock order breaks the circular wait.

## Producer-consumer without wait/notify

[`ProducerConsumer`](../../src/main/java/jbdl/session05/sync/ProducerConsumer.java) uses an
`ArrayBlockingQueue` of capacity 2: `put` waits while it's full, `take` waits while it's empty, and a
**poison pill** (-1) tells the consumer to stop. All 100 items arrive in order. The low-level version
uses `synchronized` + `wait()` / `notifyAll()` in a `while` loop; `BlockingQueue` does that for you.

## The code

| File | What it shows |
|---|---|
| [`Counters.java`](../../src/main/java/jbdl/session05/sync/Counters.java) | The race and three fixes, with a demo `main` |
| [`Accounts.java`](../../src/main/java/jbdl/session05/sync/Accounts.java) | Two locks, lock ordering against deadlock |
| [`ProducerConsumer.java`](../../src/main/java/jbdl/session05/sync/ProducerConsumer.java) | `BlockingQueue` hand-off with a poison pill |
| [`SynchronizationTest.java`](../../src/test/java/jbdl/session05/sync/SynchronizationTest.java) | Exact counts, no deadlock and conserved money, refused overdraft, ordered hand-off |

## Interview questions

- **`synchronized` vs `ReentrantLock`?** Same basic guarantee; the lock adds `tryLock`, timeouts, fairness and conditions, but you must unlock in `finally`.
- **`volatile` vs `synchronized`?** Visibility only vs visibility + mutual exclusion.
- **Why is `count++` not thread-safe?** It's read-modify-write.
- **How do you prevent deadlock?** Lock ordering, `tryLock` with timeout, or hold one lock at a time.
- **What does "reentrant" mean?** A thread holding a lock can acquire it again (e.g. a synchronized method calling another).
