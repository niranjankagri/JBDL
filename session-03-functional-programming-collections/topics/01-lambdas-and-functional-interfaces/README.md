# Topic 1: Lambda expressions and functional interfaces

A **functional interface** has exactly one abstract method (default and static methods don't count).
A **lambda** is a short way to write an object of such an interface: just the parameters and the
body of that one method. `@FunctionalInterface` is optional, but makes the compiler check the rule.

```java
@FunctionalInterface
public interface Greeting {
    String greet(String name);                       // the one abstract method
    default String greetLoudly(String name) { ... }  // allowed: not abstract
}

Greeting hi = name -> "Hi, " + name;                // the lambda IS the greet method
```

## From anonymous class to method reference

The same `Comparator<String>` (shortest word first), each step dropping what the compiler can infer:

| Step | Code |
|---|---|
| Anonymous class (pre Java 8) | `new Comparator<>() { public int compare(String a, String b) { return Integer.compare(a.length(), b.length()); } }` |
| Lambda, typed parameters, block body | `(String a, String b) -> { return Integer.compare(a.length(), b.length()); }` |
| Inferred types, expression body | `(a, b) -> Integer.compare(a.length(), b.length())` |
| Method reference helper | `Comparator.comparingInt(String::length)` |

Syntax rules: one parameter needs no parentheses (`x -> x * 2`); none needs `()` (`() -> "INR"`); an
expression body has no `return` and no braces.

## Four kinds of method reference

| Kind | Example | Same as |
|---|---|---|
| Static method | `Integer::parseInt` | `s -> Integer.parseInt(s)` |
| Method of a particular object | `prefix::concat` | `s -> prefix.concat(s)` |
| Method of an arbitrary object of a type | `String::toUpperCase` | `s -> s.toUpperCase()` |
| Constructor | `ArrayList::new` | `() -> new ArrayList<>()` |

## Capturing variables

A lambda may use local variables from the enclosing method only if they are **effectively final**
(assigned once). `addTax(18)` captures `tax`; adding `tax++` anywhere would not compile. The reason:
the lambda may run later or on another thread, so it gets a copy of the value, and a copy of a
variable that keeps changing would be confusing. Fields have no such rule.

## Lambda vs anonymous class

| | Lambda | Anonymous class |
|---|---|---|
| Interfaces | Functional interfaces only | Any interface or class |
| `this` means | The enclosing object | The anonymous object |
| Own state (fields) | No | Yes |
| Compiled to | `invokedynamic`, no extra `.class` file | A separate `Outer$1.class` |

## The code

| File | What it shows |
|---|---|
| [`Greeting.java`](../../src/main/java/jbdl/session03/functional/Greeting.java), [`FormalGreeting.java`](../../src/main/java/jbdl/session03/functional/FormalGreeting.java), [`MetroEngine.java`](../../src/main/java/jbdl/session03/functional/MetroEngine.java) | (2024 code, upgraded) custom functional interfaces; named class vs anonymous class vs lambda |
| [`LambdaSyntax.java`](../../src/main/java/jbdl/session03/functional/LambdaSyntax.java) | The four comparator forms, the four method reference kinds, capture |
| [`LambdaAndBuiltInsTest.java`](../../src/test/java/jbdl/session03/functional/LambdaAndBuiltInsTest.java), [`FunctionalExamplesTest.java`](../../src/test/java/jbdl/session03/functional/FunctionalExamplesTest.java) | All forms produce the same order; each reference kind; capture |

## Interview questions

- **What makes an interface functional?** Exactly one abstract method. `Comparator` has `equals` too, but that's from `Object`, so it doesn't count.
- **Can a lambda throw a checked exception?** Only if the interface method declares it (`Callable` can, `Runnable` can't).
- **Why effectively final?** The lambda captures a copy of the value; changing the original would make the copy stale.
- **Lambda or method reference?** Method reference when it just calls one existing method; otherwise a lambda.
