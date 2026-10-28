# Topic 4: Exception propagation

When a method throws and doesn't catch, the exception leaves the method and goes to its caller, then
the caller's caller, up the **call stack**, until a `catch` handles it. If nobody does, the thread
ends and the stack trace is printed.

```
OrderController.get(id)             catches and turns failures into responses (200 / 400 / 503)
      │ calls                        ▲
OrderService.orderSummary(id)       │ OrderLookupException (checked, cause = SQLException)
      │ calls                        │ IllegalArgumentException passes through untouched
OrderRepository.findOrder(id)  ─────┘ throws SQLException (checked) or IllegalArgumentException (unchecked)
```

## Checked vs unchecked while propagating

- **Unchecked** exceptions propagate on their own: no code in the layers in between.
  `IllegalArgumentException` from the repository reaches the controller as is.
- **Checked** exceptions must be caught or declared with `throws` at **every** layer they pass.
  Declaring `throws SQLException` all the way up would leak database details into the controller,
  so the service **translates** it.

## Wrap, don't swallow

```java
} catch (SQLException e) {
    throw new OrderLookupException("could not load order " + id, e);   // e becomes getCause()
}
```

- The caller gets an exception that fits its layer (`OrderLookupException`).
- The original stays as the **cause**: the stack trace shows `Caused by: java.sql.SQLException: connection refused`.
- Bad: `catch (SQLException e) { }` (swallowed) or `throw new OrderLookupException("failed")` (cause lost).

## The code

| File | What it shows |
|---|---|
| [`OrderRepository.java`](../../src/main/java/jbdl/session02/propagation/OrderRepository.java) | Throws a checked `SQLException` or an unchecked `IllegalArgumentException` |
| [`OrderService.java`](../../src/main/java/jbdl/session02/propagation/OrderService.java), [`OrderLookupException.java`](../../src/main/java/jbdl/session02/propagation/OrderLookupException.java) | Translates the checked exception and keeps the cause; lets the unchecked one pass |
| [`OrderController.java`](../../src/main/java/jbdl/session02/propagation/OrderController.java) | The one place that handles failures, like a REST controller (Session 8 does this with `@ControllerAdvice`) |
| [`PropagationTest.java`](../../src/test/java/jbdl/session02/propagation/PropagationTest.java) | Cause chain, untouched propagation (stack trace starts in the repository), responses |

## Run it

```bash
java -cp session-02-advanced-java/target/classes jbdl.session02.propagation.OrderController
```

```
200 Summary of order-7
400 order id must be positive: -1
503 could not load order 7 (cause: connection refused)
```

## Interview questions

- **How does an exception propagate?** Up the call stack, frame by frame, until a matching `catch`.
- **What happens if no one catches it?** The thread's uncaught-exception handler prints the stack trace and the thread ends (for `main`, the program exits with an error).
- **Why wrap exceptions?** Each layer speaks its own language; the cause keeps the full story for debugging.
- **Checked exceptions and lambdas?** `Function`/`Consumer` can't throw checked exceptions, one reason modern code prefers unchecked ones.
