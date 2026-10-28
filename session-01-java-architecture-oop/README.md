# Session 1: Java Architecture and Basics of OOPs

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Introduction to Java architecture | [`architecture`](src/main/java/jbdl/session01/architecture) | [01-java-architecture](topics/01-java-architecture/README.md) |
| 2 | Basics of Object-Oriented Programming | [`oopbasics`](src/main/java/jbdl/session01/oopbasics) | [02-oop-basics](topics/02-oop-basics/README.md) |
| 3 | Core OOP principles | [`principles`](src/main/java/jbdl/session01/principles) | [03-core-oop-principles](topics/03-core-oop-principles/README.md) |

## What you can do after this session

- Explain JDK, JRE and JVM, what bytecode is and how the class loader, memory areas, JIT and garbage collector work together.
- Explain why Java is always pass-by-value, using the stack and the heap.
- Write a class with private state, overloaded and chained constructors, static and instance members, and correct `equals`/`hashCode` (or a `record`).
- Name the four OOP pillars and point to an example of each: encapsulation, inheritance, polymorphism (overloading vs overriding), abstraction.

## Run it

```bash
./mvnw -q -pl session-01-java-architecture-oop verify          # compile (warnings are errors) + tests
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.architecture.ClassLoaders
```

Every class with a `main` method is a runnable demo; each topic README lists them with sample output.

## Layout

```
session-01-java-architecture-oop/
  topics/<topic>/README.md          explanation of each topic, linking to its code
  src/main/java/jbdl/session01/     architecture, oopbasics, principles
  src/test/java/jbdl/session01/     one test class per idea + testdata/TestData.java
```
