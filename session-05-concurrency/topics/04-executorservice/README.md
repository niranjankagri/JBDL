# Topic 4: The ExecutorService framework

`ExecutorService` separates **what** to run (tasks) from **how** (threads, pool, queue). You submit
tasks and get **futures** back; the service manages the threads.

```
Executor              execute(Runnable)
└── ExecutorService   submit(Callable/Runnable) → Future, invokeAll, invokeAny, shutdown
    └── ScheduledExecutorService   schedule, scheduleAtFixedRate
```

## Runnable vs Callable, Future

| | `Runnable` | `Callable<V>` |
|---|---|---|
| Method | `void run()` | `V call() throws Exception` |
| Returns a value | No | Yes |
| Checked exceptions | No | Yes |

`submit(callable)` returns a `Future<V>`:

- `get()` waits for the result; `get(5, SECONDS)` gives up with `TimeoutException`.
- If the task threw, `get()` throws **`ExecutionException`** whose `getCause()` is the task's exception.
- `cancel(true)` interrupts the task; `isDone()` checks without waiting.

## The example: comparing prices from slow shops

[`PriceComparison`](../../src/main/java/jbdl/session05/executors/PriceComparison.java) asks shops for a
price; each call takes 300 ms (simulating HTTP).

| Method | API | Result in the tests |
|---|---|---|
| `allQuotes` | `submit` each, then `Future.get` | 3 shops in ≈ 300 ms instead of 900 ms; a closed shop gives `failed: ClosedShop is unavailable` |
| `cheapest` | `invokeAll` (submit a batch, wait for all) | ShopB at 950 |
| `fastestAnswer` | `invokeAny` (first **successful** result, others cancelled) | ShopC, although ClosedShop was listed first |
| `bestPriceWithTax` | `CompletableFuture` | `ShopB incl. GST: 1121`, or a fallback message |

## CompletableFuture: composing instead of blocking

```java
CompletableFuture<Quote> a = CompletableFuture.supplyAsync(() -> call(shopA), pool);
CompletableFuture<Quote> b = CompletableFuture.supplyAsync(() -> call(shopB), pool);
a.thenCombine(b, (x, y) -> cheaper(x, y))          // when both are done
 .thenApply(q -> q.shop() + " incl. GST: " + ...)  // transform the result
 .exceptionally(error -> "no price available (...)"); // recover from any failure above
```

Other useful steps: `thenCompose` (chain another async call), `allOf`/`anyOf`, `orTimeout`,
`completeOnTimeout`. Always pass your own executor for blocking work; the default is the common
ForkJoinPool (Session 4, topic 5).

## The code

| File | What it shows |
|---|---|
| [`PriceComparison.java`](../../src/main/java/jbdl/session05/executors/PriceComparison.java) | `Callable`, `submit`, `Future.get` with timeout, `ExecutionException`, `invokeAll`, `invokeAny`, `CompletableFuture` |
| [`PriceComparisonTest.java`](../../src/test/java/jbdl/session05/executors/PriceComparisonTest.java) | Parallel timing, failure via the future, batch and first-wins, combined async flow with recovery |

## Interview questions

- **`execute` vs `submit`?** `execute(Runnable)` returns nothing and exceptions go to the thread's handler; `submit` returns a `Future` that holds the result or exception.
- **What does `Future.get` throw if the task failed?** `ExecutionException` with the original as the cause.
- **`invokeAll` vs `invokeAny`?** All results vs the first successful one.
- **`Future` vs `CompletableFuture`?** `Future` can only be waited on; `CompletableFuture` can be chained, combined and completed manually.
- **`thenApply` vs `thenCompose`?** Map the value vs chain another future (like `map` vs `flatMap`).
