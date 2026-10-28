# Topic 3: The `finally` block and the `throw` keyword

## `throw` vs `throws`

| | `throw` | `throws` |
|---|---|---|
| Where | Inside a method body | In the method signature |
| What | Raises one exception object: `throw new InvalidAmountException(amount);` | Declares which checked exceptions may leave the method: `void pay(long amount) throws InsufficientBalanceException` |
| Needed for | Any exception | Checked exceptions only (unchecked may be listed for documentation) |

```java
public void pay(long amountInPaise) throws InsufficientBalanceException {   // throws: contract
    if (amountInPaise <= 0) {
        throw new InvalidAmountException(amountInPaise);                      // unchecked: not declared
    }
    if (amountInPaise > balanceInPaise) {
        throw new InsufficientBalanceException(amountInPaise - balanceInPaise); // checked: declared above
    }
    balanceInPaise -= amountInPaise;
}
```

## When `finally` runs

`finally` runs after `try` (and `catch`, if one ran) **in every case**: normal end, caught exception,
uncaught exception, `return`, `break`, `continue`. Only `System.exit()`, a JVM crash or a killed
process skip it. It's where cleanup goes (close a file, release a lock).

| Scenario (from `FinallyDemo`) | Order of events |
|---|---|
| No exception | `try` → `try finished` → `finally` |
| Exception caught | `try` → `catch boom` → `finally` |
| `return` inside `try` | `try returns 1` → `finally sets value to 2` → method returns **1** |

The last row: the return value is evaluated and saved **before** `finally` runs, so changing the
variable in `finally` doesn't change what is returned. Never `return` from `finally` itself: it
silently discards any exception thrown in `try`.

## try-with-resources (Java 7)

Anything `AutoCloseable` declared in `try (...)` is closed automatically, **in reverse order**, before
`catch`/`finally` run:

```
open db → open file → use db and file → close file → close db → finally
```

If the body throws and `close()` also throws, the body's exception is the one you get; the close
exception is attached with `addSuppressed` (`getSuppressed()`). With a hand-written `finally`, the
close exception would replace the original and the real cause would be lost.

## The code

| File | What it shows |
|---|---|
| [`FinallyDemo.java`](../../src/main/java/jbdl/session02/exceptions/FinallyDemo.java) | Every scenario above records its events in a list |
| [`Wallet.java`](../../src/main/java/jbdl/session02/exceptions/Wallet.java) | `throw` and `throws` |
| [`ExceptionsTest.java`](../../src/test/java/jbdl/session02/exceptions/ExceptionsTest.java) | Event order, the returned value, close order, suppressed exceptions |

## Interview questions

- **Does `finally` run if `try` returns?** Yes, after the return value is saved.
- **When doesn't it run?** `System.exit()`, JVM crash, the thread killed, infinite loop in `try`.
- **`final` vs `finally` vs `finalize`?** Keyword for constants/no override; cleanup block; deprecated GC hook (removed for use, never rely on it).
- **What are suppressed exceptions?** Exceptions from `close()` attached to the main exception in try-with-resources.
- **Can you rethrow?** Yes: `catch (IOException e) { throw new MyException("...", e); }`; keep the cause (topic 4).
