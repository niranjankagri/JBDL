# Topic 2: Built-in functional interfaces

`java.util.function` has ready-made interfaces for the common shapes of a function, so you rarely
write your own.

| Interface | Method | In → out | Example in the code |
|---|---|---|---|
| `Function<T, R>` | `apply` | T → R | `ASCII_TO_CHAR`: 100 → `'d'` |
| `BiFunction<T, U, R>` | `apply` | T, U → R | `PERCENT_OF`: (10, 2500) → 250 |
| `UnaryOperator<T>` | `apply` | T → T | `TRIM_AND_LOWER`: `"  Hello "` → `"hello"` |
| `BinaryOperator<T>` | `apply` | T, T → T | `LARGER` (what `reduce` takes) |
| `Predicate<T>` | `test` | T → boolean | `LESS_THAN_10`, `OUTSIDE_10_TO_20` |
| `Consumer<T>` | `accept` | T → nothing (side effect) | `splitName` adds to a list |
| `Supplier<T>` | `get` | nothing → T | `DEFAULT_CURRENCY`, lazy defaults |

## Combining them

| Interface | Combinators |
|---|---|
| `Function` | `andThen` (run after), `compose` (run before), `Function.identity()` |
| `Predicate` | `and`, `or`, `negate`, `Predicate.not(...)`, `Predicate.isEqual(...)` |
| `Consumer` | `andThen` |
| `BinaryOperator` | `minBy(comparator)`, `maxBy(comparator)` |

`HALF.andThen(x -> x * 3).apply(10)` = (10 / 2) × 3 = **15.0**; `HALF.compose(x -> x * 3).apply(5)` = (5 × 3) / 2 = **7.5**.

## Primitive specialisations

`Predicate<Integer>` boxes every `int` into an `Integer` object. For number-heavy code use
`IntPredicate`, `IntFunction<R>`, `ToIntFunction<T>`, `IntUnaryOperator`, `IntBinaryOperator`,
`IntSupplier` (and the `Long`/`Double` versions). `IntStream.rangeClosed(1, 10).filter(EVEN).sum()`
never creates an object per number.

## Supplier is lazy

```java
valueOrDefault(value, () -> buildExpensiveDefault())   // the lambda runs ONLY if value is null
```

The test counts calls: with a value present, the supplier never runs. `Optional.orElseGet` and
`Map.computeIfAbsent` use the same idea.

## The code

| File | What it shows |
|---|---|
| [`FunctionalExamples.java`](../../src/main/java/jbdl/session03/functional/FunctionalExamples.java) | (2024 code, upgraded) `Function` with `andThen`/`compose`/`identity`, `Predicate` with `and`/`or`/`negate`, chained `Consumer`s |
| [`BuiltInInterfaces.java`](../../src/main/java/jbdl/session03/functional/BuiltInInterfaces.java) | `Supplier`, `BiFunction`, `UnaryOperator`, `BinaryOperator`, `IntPredicate`, `ToIntFunction`, lazy defaults, `reduce` |
| [`LambdaAndBuiltInsTest.java`](../../src/test/java/jbdl/session03/functional/LambdaAndBuiltInsTest.java), [`FunctionalExamplesTest.java`](../../src/test/java/jbdl/session03/functional/FunctionalExamplesTest.java) | Every interface and combinator, the supplier call count |

## Run it

```bash
java -cp session-03-functional-programming-collections/target/classes jbdl.session03.functional.FunctionalExamples
```

## Interview questions

- **`Function` vs `UnaryOperator`?** `UnaryOperator<T>` is a `Function<T, T>`.
- **`andThen` vs `compose`?** `f.andThen(g)` = g(f(x)); `f.compose(g)` = f(g(x)).
- **Which interface does `forEach` take? `filter`? `map`? `reduce`?** `Consumer`, `Predicate`, `Function`, `BinaryOperator`.
- **Why `IntPredicate`?** To avoid boxing every `int` into an `Integer`.
- **`orElse` vs `orElseGet`?** `orElse(x)` evaluates `x` always; `orElseGet(supplier)` only when empty.
