# Session 3: Java 8 Functional Programming & Collections Overview

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Lambda expressions and functional interfaces | [`functional`](src/main/java/jbdl/session03/functional) | [01-lambdas-and-functional-interfaces](topics/01-lambdas-and-functional-interfaces/README.md) |
| 2 | Built-in functional interfaces | [`functional`](src/main/java/jbdl/session03/functional) | [02-built-in-functional-interfaces](topics/02-built-in-functional-interfaces/README.md) |
| 3 | Overview of the Java Collections Framework | [`collections`](src/main/java/jbdl/session03/collections) | [03-collections-framework](topics/03-collections-framework/README.md) |
| 4 | Introduction to queues | [`queues`](src/main/java/jbdl/session03/queues) | [04-queues](topics/04-queues/README.md) |

## What you can do after this session

- Write lambdas and the four kinds of method reference, and explain effectively final capture.
- Pick the right built-in functional interface and combine them (`andThen`, `compose`, `and`, `or`, `negate`).
- Choose a List, Set, Map or Queue implementation by the behaviour you need, sort with `Comparable` or `Comparator`, and avoid `ConcurrentModificationException`.
- Use `PriorityQueue` and `ArrayDeque` (as a queue and as a stack).

## Run it

```bash
./mvnw -q -pl session-03-functional-programming-collections verify
java -cp session-03-functional-programming-collections/target/classes jbdl.session03.functional.FunctionalExamples
java -cp session-03-functional-programming-collections/target/classes jbdl.session03.collections.CollectionExamples
```

The 2024 course exercises (`Greeting`, `FormalGreeting`, `MetroEngine`, `FunctionalExamples`,
`CollectionExamples`) are part of topics 1-4.
