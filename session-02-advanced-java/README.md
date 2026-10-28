# Session 2: Advanced Java Concepts

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Compare abstract classes and interfaces | [`abstraction`](src/main/java/jbdl/session02/abstraction) | [01-abstract-classes-vs-interfaces](topics/01-abstract-classes-vs-interfaces/README.md) |
| 2 | The Java exception hierarchy | [`exceptions`](src/main/java/jbdl/session02/exceptions) | [02-exception-hierarchy](topics/02-exception-hierarchy/README.md) |
| 3 | Role of the `finally` block and the `throw` keyword | [`exceptions`](src/main/java/jbdl/session02/exceptions) | [03-finally-and-throw](topics/03-finally-and-throw/README.md) |
| 4 | Exception propagation | [`propagation`](src/main/java/jbdl/session02/propagation) | [04-exception-propagation](topics/04-exception-propagation/README.md) |
| 5 | Introduction to the Singleton design pattern | [`singleton`](src/main/java/jbdl/session02/singleton) | [05-singleton-pattern](topics/05-singleton-pattern/README.md) |

## What you can do after this session

- Choose between an abstract class and an interface, and use default, static, private and sealed interface features.
- Place any exception in the hierarchy (checked, unchecked, error) and write custom exceptions of the right kind.
- Predict exactly when `finally` runs, use try-with-resources, and read suppressed exceptions.
- Let exceptions propagate through layers, translating checked ones while keeping the cause.
- Write a thread-safe singleton four ways and explain how each can be broken.

## Run it

```bash
./mvnw -q -pl session-02-advanced-java verify
java -cp session-02-advanced-java/target/classes jbdl.session02.abstraction.AbstractionDemo
java -cp session-02-advanced-java/target/classes jbdl.session02.propagation.OrderController
```
