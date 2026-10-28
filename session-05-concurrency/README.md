# Session 5: Java Concurrency Essentials

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Introduction to multithreading | [`threads`](src/main/java/jbdl/session05/threads) | [01-multithreading](topics/01-multithreading/README.md) |
| 2 | Thread pool concepts and join operations | [`pools`](src/main/java/jbdl/session05/pools) | [02-thread-pools-and-join](topics/02-thread-pools-and-join/README.md) |
| 3 | Synchronization mechanisms | [`sync`](src/main/java/jbdl/session05/sync) | [03-synchronization](topics/03-synchronization/README.md) |
| 4 | The ExecutorService framework | [`executors`](src/main/java/jbdl/session05/executors) | [04-executorservice](topics/04-executorservice/README.md) |

## What you can do after this session

- Create threads the right way, follow their lifecycle and stop them with `interrupt`.
- Use `join` for ordering and visibility, and pick and shut down a thread pool.
- Find a race condition and fix it with `synchronized`, atomics or locks; avoid deadlock with lock ordering; hand work between threads with a `BlockingQueue`.
- Run tasks with `ExecutorService`, read results and failures from `Future`, and compose async work with `CompletableFuture`.

## Run it

```bash
./mvnw -q -pl session-05-concurrency verify
java -cp session-05-concurrency/target/classes jbdl.session05.sync.Counters
java -cp session-05-concurrency/target/classes jbdl.session05.threads.ThreadExamples
```

Every concurrency test has a `@Timeout`, so a deadlock fails the build instead of hanging it.
The 2024 course exercises (`CountingTask`, `CountingThread`, `ThreadExamples`) are part of topic 1.
