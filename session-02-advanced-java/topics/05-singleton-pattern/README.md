# Topic 5: The Singleton design pattern

A class that has **exactly one instance** and a global way to get it: configuration, a registry, a
connection pool. Three ingredients: a `private` constructor, a `static` field holding the one
instance, and a `static` method (or enum constant) that returns it.

## Four ways to write it

| Kind | Created | Thread-safe | Reflection / serialization safe | File |
|---|---|---|---|---|
| **Eager** | When the class loads | Yes (class loading is) | No | [`EagerConfig`](../../src/main/java/jbdl/session02/singleton/EagerConfig.java) |
| **Lazy, double-checked locking** | On first `getInstance()` | Yes, needs `volatile` | No | [`LazyConfig`](../../src/main/java/jbdl/session02/singleton/LazyConfig.java) |
| **Holder idiom** | On first `getInstance()` | Yes, no locks (class loading again) | No | [`HolderConfig`](../../src/main/java/jbdl/session02/singleton/HolderConfig.java) |
| **Enum** | When the enum loads | Yes | **Yes** | [`AppRegistry`](../../src/main/java/jbdl/session02/singleton/AppRegistry.java) |

### Why double-checked locking needs `volatile`

```java
if (instance == null) {                 // 1. fast check without a lock
    synchronized (LazyConfig.class) {
        if (instance == null) {         // 2. check again: another thread may have created it meanwhile
            instance = new LazyConfig();
        }
    }
}
```

`new LazyConfig()` is several steps (allocate, run the constructor, assign). Without `volatile`, the
CPU or JIT may reorder them, so another thread could see a non-null `instance` whose constructor
hasn't finished. `volatile` forbids that reordering. (Plain lazy `if (instance == null) instance = new ...`
without any lock creates two instances when two threads arrive together.)

### Breaking a singleton

- **Reflection:** `getDeclaredConstructor().setAccessible(true).newInstance()` calls the private
  constructor and creates a second object (the test shows it for `EagerConfig`).
- **Serialization:** deserializing creates a new object unless the class adds `readResolve()`.
- **Enum** singletons are immune to both: the JVM refuses to create enum constants by reflection,
  and enums serialize by name. That's why *Effective Java* recommends them.

## Singletons in Spring

Spring beans are singletons **per application context** by default (Session 8): one object, created
and injected by the container, without a static `getInstance()`. That is easier to test (you can
inject a mock) than a hand-written singleton.

## The code

| File | What it shows |
|---|---|
| The four classes above | One file per kind, each with comments on why it works |
| [`SingletonTest.java`](../../src/test/java/jbdl/session02/singleton/SingletonTest.java) | Same instance for every kind; 64 threads racing `LazyConfig.getInstance()` get one object, constructor ran once; reflection breaks `EagerConfig` but not the enum |

## Interview questions

- **How do you make a singleton thread-safe?** Eager, holder idiom, enum, or double-checked locking with `volatile`.
- **Why is `volatile` needed in double-checked locking?** To stop reordering so no thread sees a half-built object.
- **How can a singleton be broken?** Reflection, serialization, cloning, multiple class loaders.
- **Is a singleton an anti-pattern?** It's global state: hard to test and hides dependencies. Prefer dependency injection (Spring) for application code.
