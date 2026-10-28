# JBDL (Java Backend Development Live): Java fundamentals

[![Tests](https://github.com/niranjankagri/JBDL/actions/workflows/tests.yml/badge.svg)](https://github.com/niranjankagri/JBDL/actions/workflows/tests.yml)

My Java practice exercises from the 2024 JBDL course, rebuilt as a small Maven project: every
example returns its result instead of only printing it, so each concept is checked by a JUnit test.

The original 2024 code is kept unchanged in the history: commit
[`4b8754b`](https://github.com/niranjankagri/JBDL/tree/4b8754b) ("Add 2024 Java course practice code").

## Topics

| Package | What it shows | Run the demo |
|---|---|---|
| `jbdl.basics` | Builder pattern for a seven-property `Car`; quiz traps: byte overflow, the `Integer` cache and `==`, post-increment with `\|\|` | `JavaQuiz` |
| `jbdl.inheritance` | Overloaded + chained constructors, `extends`, overriding with `super`, dynamic dispatch, reference assignment, factory pattern | `InheritanceDemo` |
| `jbdl.collections` | List / Set / Map / Queue implementations given the same input: order, duplicates, sorting, FIFO vs priority | `CollectionExamples` |
| `jbdl.functional` | Custom `@FunctionalInterface` with a default method; named class vs anonymous class vs lambda; `Function` (`andThen`, `compose`, `identity`), `Predicate` (`and`, `or`, `negate`), `Consumer` (`andThen`) | `FunctionalExamples` |
| `jbdl.multithreading` | `Runnable` vs extending `Thread`, `start()` vs `run()`, concurrent threads vs `join()` one by one, a thread-safe shared list | `ThreadExamples` |

## Run it

Needs Java 17 or newer. Maven itself is not needed: the Maven Wrapper downloads a checked version.

```bash
./mvnw verify                                   # compile (warnings are errors) + 29 tests
java -cp target/classes jbdl.basics.JavaQuiz    # any class from the table above
```

On Windows use `mvnw.cmd verify`.

## Project layout

```
src/main/java/jbdl/   basics, inheritance, collections, functional, multithreading
src/test/java/jbdl/   one test class per package
                      testdata/TestData.java: shared inputs used by every test class
.github/workflows/    CI: build + tests on JDK 17 and 25 for every push and pull request
```

## What changed from the 2024 version

| 2024 file(s) | Now | Why |
|---|---|---|
| `Car.java` (7-argument constructor) | `basics/Car` with a Builder | Named values instead of seven positional arguments |
| `Quiz.java` (two questions commented out) | `basics/JavaQuiz` | All three questions answered and tested |
| `Main.java`, `inheritance/*` | `inheritance/InheritanceDemo`, `Car`, `SuperCar`, `CarFactory` | Private fields + getters; `SuperCar.getInfo()` returned a placeholder, now builds on `super.getInfo()` |
| `collections/MyList`, `MySet`, `MyMap`, `MyQueue` | `collections/CollectionExamples` | The old code ignored every return value (`size()`, `peek()`), so it showed nothing |
| `functionalinterface/FunctionalInterface` | `functional/Greeting` | The old name hid `java.lang.FunctionalInterface` |
| `LanbdaExpression.java` | lambda in `FunctionalExamples` | File name typo |
| `task/MetroEngine`, `MetroEngine2` | `functional/MetroEngine` | The two interfaces were identical |
| `multithreading/ThreadJoin`, `MultiThreading` | `multithreading/ThreadExamples` | **Bug:** the worker created an inner thread but never started it, so nothing ran |
| `multithreading/RunnableInterface` | `multithreading/CountingTask` | **Bug:** its `Runnable` class was never used; `main` started the `Thread` subclass instead |
| `Thread.getId()` | thread names | Deprecated since Java 19 |

## License

MIT, see [LICENSE](LICENSE).
