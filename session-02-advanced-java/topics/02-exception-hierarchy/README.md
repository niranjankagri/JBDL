# Topic 2: The Java exception hierarchy

```
Throwable
├── Error                          serious JVM problems; don't catch them
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception                      CHECKED: catch it or declare it with `throws`
    ├── IOException
    │   └── FileNotFoundException
    ├── SQLException
    ├── InsufficientBalanceException   (ours)
    └── RuntimeException           UNCHECKED: no compiler check
        ├── NullPointerException
        ├── ArithmeticException
        └── IllegalArgumentException
            └── InvalidAmountException (ours)
```

| | Checked | Unchecked | Error |
|---|---|---|---|
| Parent | `Exception` (not `RuntimeException`) | `RuntimeException` | `Error` |
| Compiler forces catch or `throws` | Yes | No | No |
| Means | An expected problem the caller can handle (file missing, network down, not enough money) | A programming mistake (null, bad index, bad argument) | The JVM can't continue normally |
| Should you catch it? | Yes, where you can do something useful | Usually fix the code; catch at the top layer | No |

## Catching

- **Order matters:** the most specific type first. A `catch (IOException e)` before
  `catch (FileNotFoundException e)` doesn't compile: the second block could never run.
- **Multi-catch** (Java 7): `catch (IllegalArgumentException | IllegalStateException e)` for
  unrelated types handled the same way.
- Don't catch `Exception` or `Throwable` "just in case": it hides bugs and swallows `InterruptedException`.

## Custom exceptions

| Exception | Kind | Why |
|---|---|---|
| [`InsufficientBalanceException`](../../src/main/java/jbdl/session02/exceptions/InsufficientBalanceException.java) | checked | A business outcome the caller must handle; carries data (`getShortByInPaise()`) |
| [`InvalidAmountException`](../../src/main/java/jbdl/session02/exceptions/InvalidAmountException.java) | unchecked | A caller bug (negative amount); extends `IllegalArgumentException` |

## The code

| File | What it shows |
|---|---|
| [`ExceptionKinds.java`](../../src/main/java/jbdl/session02/exceptions/ExceptionKinds.java) | Classifies any `Throwable` as checked / unchecked / error; catch order and multi-catch |
| [`Wallet.java`](../../src/main/java/jbdl/session02/exceptions/Wallet.java) | Throws both custom exceptions |
| [`ExceptionsTest.java`](../../src/test/java/jbdl/session02/exceptions/ExceptionsTest.java) | 8 throwables classified (parameterized test), catch order, custom exceptions |

## Interview questions

- **Checked vs unchecked?** Checked: compiler-enforced, recoverable conditions. Unchecked: programming errors, no enforcement.
- **Is `NullPointerException` checked?** No, it extends `RuntimeException`.
- **Should a custom exception be checked?** Only if callers can reasonably recover; most modern APIs (Spring) prefer unchecked.
- **Error vs Exception?** `Error` = the JVM is in trouble; applications shouldn't catch it.
- **Why add `serialVersionUID` to an exception?** `Throwable` is `Serializable`; a fixed id keeps old serialized forms readable.
