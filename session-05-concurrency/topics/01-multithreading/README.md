# Topic 1: Introduction to multithreading

A **thread** is an independent path of execution inside one program (one JVM process). All threads
share the heap (objects) but each has its own stack. A backend server uses threads to handle many
requests at once and to keep the CPU busy while one request waits for a database.

**Concurrency** = several tasks in progress at the same time (they may take turns on one core).
**Parallelism** = several tasks *running* at the same instant on different cores.

## Two ways to define the work

| | `implements Runnable` | `extends Thread` |
|---|---|---|
| Code | `new Thread(new CountingTask(...)).start()` | `new CountingThread(...).start()` |
| The class can still extend another class | Yes | No (single inheritance) |
| Reusable with thread pools | Yes (`executor.execute(runnable)`) | No |
| Verdict | **Preferred** (or a lambda) | Only for small demos |

`start()` creates a new thread that calls `run()`. Calling `run()` directly just runs the code on the
**current** thread: no new thread at all.

## Thread lifecycle (observed in the tests)

```
NEW ──start()──▶ RUNNABLE ──▶ TERMINATED
                    │  ▲
     sleep/wait/    │  │  time up / notified /
     join/lock      ▼  │  lock acquired
         BLOCKED · WAITING · TIMED_WAITING
```

[`ThreadLifecycle`](../../src/main/java/jbdl/session05/threads/ThreadLifecycle.java) records a real
thread going `NEW → WAITING` (parked on a latch) `→ TERMINATED`.

## Stopping a thread politely

There is no safe way to *kill* a thread (`Thread.stop()` is removed). Instead call `interrupt()`:
a sleeping or waiting thread wakes up with `InterruptedException`; a running thread checks
`Thread.currentThread().isInterrupted()`. The test interrupts a thread sleeping for 60 s; it reports
"interrupted while sleeping" at once.

**Daemon threads** (`setDaemon(true)`) don't keep the JVM alive: background helpers such as the GC.

## Virtual threads (Java 21)

`Thread.ofVirtual().start(task)` or `Executors.newVirtualThreadPerTaskExecutor()` create very cheap
threads managed by the JVM, ideal for many blocking I/O tasks (one per request). This module targets
Java 17, so they're described here rather than coded; Spring Boot enables them with
`spring.threads.virtual.enabled=true`.

## The code

| File | What it shows |
|---|---|
| [`CountingTask.java`](../../src/main/java/jbdl/session05/threads/CountingTask.java), [`CountingThread.java`](../../src/main/java/jbdl/session05/threads/CountingThread.java), [`ThreadExamples.java`](../../src/main/java/jbdl/session05/threads/ThreadExamples.java) | (2024 code, fixed) `Runnable` vs `Thread`, concurrent vs one-after-another with `join` |
| [`ThreadLifecycle.java`](../../src/main/java/jbdl/session05/threads/ThreadLifecycle.java) | States, `interrupt`, daemon flag |
| [`ThreadExamplesTest.java`](../../src/test/java/jbdl/session05/threads/ThreadExamplesTest.java), [`ThreadLifecycleTest.java`](../../src/test/java/jbdl/session05/threads/ThreadLifecycleTest.java) | Every behaviour above |

## Interview questions

- **Process vs thread?** A process has its own memory; threads in a process share the heap.
- **`start()` vs `run()`?** `start()` creates a new thread; `run()` is a normal method call.
- **Can you start a thread twice?** No: `IllegalThreadStateException`.
- **`sleep` vs `wait`?** `sleep` keeps any locks; `wait` releases the object's monitor and must be called inside `synchronized`.
- **How do you stop a thread?** Interrupt it and let it finish cleanly.
