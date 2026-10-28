# Topic 1: Generics in Java

Generics let a class or method work with **any type** while the compiler still checks the types.
Before Java 5 a `List` held `Object`s: you cast on every `get` and found mistakes only at run time
(`ClassCastException`). With `List<String>` the mistake is a compile error.

## The pieces

| Feature | Syntax | In the code |
|---|---|---|
| Generic class / record | `class Box<T>`, `record Pair<K, V>` | [`Pair`](../../src/main/java/jbdl/session04/generics/Pair.java) |
| Generic method | `static <A, B> Pair<B, A> swap(Pair<A, B> p)` | `Pair.swap` (types inferred from the argument) |
| Bounded type | `<T extends Comparable<? super T>>` | `GenericMethods.max`: works for `Integer`, `String`, `LocalDate` |
| Upper-bounded wildcard | `List<? extends Number>` | `sum`: read numbers from a `List<Integer>` or `List<Double>` |
| Lower-bounded wildcard | `List<? super T>` | `copy`: write `Integer`s into a `List<Number>` or `List<Object>` |
| Generic interface in practice | `InMemoryRepository<T, ID>` | The shape of Spring Data's `CrudRepository<T, ID>` |

## Generics are invariant: why wildcards exist

`Integer` is a `Number`, but `List<Integer>` is **not** a `List<Number>`. If it were, you could add a
`Double` to a list of `Integer`s through the `List<Number>` reference. So:

- `sum(List<Number>)` would reject a `List<Integer>`; `sum(List<? extends Number>)` accepts it, but
  you can't add to it (the exact type is unknown).
- **PECS: Producer Extends, Consumer Super.** A source you read from is `? extends T`; a target you
  write into is `? super T`. `Collections.copy(List<? super T> dest, List<? extends T> src)` follows it.

## Type erasure

Generics exist only at compile time. The compiler checks the types, inserts the casts, then **erases**
`<String>` to its bound (`Object`). At run time `new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()`
(the test checks it). Consequences:

- No `new T()`, no `new T[10]`, no `instanceof List<String>`, no primitive type arguments (`List<int>`).
- Generic arrays need an unchecked cast (`SimpleHashMap` in topic 4 does it).
- Overloads that differ only in type arguments (`m(List<String>)`, `m(List<Integer>)`) clash.

## The code

| File | What it shows |
|---|---|
| [`Pair.java`](../../src/main/java/jbdl/session04/generics/Pair.java) | Generic record + generic method |
| [`GenericMethods.java`](../../src/main/java/jbdl/session04/generics/GenericMethods.java) | Bounded `max`, `? extends` sum, PECS `copy`, erasure |
| [`InMemoryRepository.java`](../../src/main/java/jbdl/session04/generics/InMemoryRepository.java) | A generic repository: any entity, any id type |
| [`GenericsTest.java`](../../src/test/java/jbdl/session04/generics/GenericsTest.java) | No casts needed; `max` over three types; wildcards; erasure; the repository with two entity types |

## Interview questions

- **Why generics?** Compile-time type safety and no casts.
- **What is type erasure?** Type arguments are removed after compilation; the JVM sees raw types.
- **`List<?>` vs `List<Object>`?** `List<?>` accepts a list of anything but you can't add (except `null`); `List<Object>` accepts only a `List<Object>`, and you can add anything.
- **Explain PECS.** Read from `? extends T`, write to `? super T`.
- **Can you create a generic array?** Not directly (`new T[n]`); use `List<T>` or an unchecked cast.
