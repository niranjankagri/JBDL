# Topic 2: Basics of Object-Oriented Programming

Object-oriented programming groups **data** (fields) and the **behaviour** that works on it (methods)
into classes. A **class** is the blueprint; an **object** is one thing built from it with `new`.

```java
BankAccount first  = new BankAccount("Niranjan", 50_000);   // object 1, its own balance
BankAccount second = new BankAccount("Asha");               // object 2, its own balance
```

## Building blocks

| Term | Meaning | In the code |
|---|---|---|
| **Field** | Data an object holds | `owner`, `balanceInPaise` |
| **Method** | Behaviour; usually reads or changes fields | `deposit`, `withdraw` |
| **Constructor** | Runs once on `new`; puts the object in a valid state | `BankAccount(String owner, long openingBalanceInPaise)` |
| **Overloaded constructors** | Same name, different parameters | `BankAccount(String)` and `BankAccount(String, long)` |
| **Constructor chaining** | `this(...)` calls another constructor of the same class (must be the first line) | `this(owner, 0)` |
| **`this`** | The current object; `this.owner = owner` sets the field from the parameter | constructor |
| **Instance member** | One copy per object | `balanceInPaise` |
| **`static` member** | One copy per class, shared by all objects; usable without an object | `accountsOpened`, `BankAccount.accountsOpened()` |
| **`final` field** | Assigned once, never changed | `accountNumber`, `owner` |

### Access modifiers

| Modifier | Visible from |
|---|---|
| `private` | The same class only |
| *(none)*, "package-private" | The same package |
| `protected` | The same package + subclasses |
| `public` | Everywhere |

Rule of thumb: fields `private`, and only the methods other classes need `public`. `BankAccount` has
no setter for the balance: the only way to change it is `deposit`/`withdraw`, which refuse invalid
amounts. That is **encapsulation** (more in [topic 3](../03-core-oop-principles/README.md)).

### Identity vs equality

```java
Point a = new Point(1, 2);
Point b = new Point(1, 2);
a == b        // false: two different objects on the heap
a.equals(b)   // true:  same values (Point overrides equals)
```

`Object.equals` is `==` by default. A value class overrides **`equals` and `hashCode` together**:
equal objects must have equal hash codes, otherwise `HashSet`/`HashMap` put them in different
buckets and treat them as different. A **`record`** (Java 16+) generates the constructor,
accessors, `equals`, `hashCode` and `toString` for you.

## The code

| File | What it shows |
|---|---|
| [`BankAccount.java`](../../src/main/java/jbdl/session01/oopbasics/BankAccount.java) | Class vs object, private fields, constructors + `this(...)`, `this.field`, static vs instance, validation, `toString`; money in paise as `long` (never `double` for money) |
| [`Point.java`](../../src/main/java/jbdl/session01/oopbasics/Point.java) | `==` vs `equals`, the `equals`/`hashCode` contract, the same as a one-line `record` |
| [`Car.java`](../../src/main/java/jbdl/session01/oopbasics/Car.java) | (2024 code, upgraded) **Builder pattern**: seven properties set by name instead of a seven-argument constructor |
| [`OopBasicsTest.java`](../../src/test/java/jbdl/session01/oopbasics/OopBasicsTest.java), [`CarBuilderTest.java`](../../src/test/java/jbdl/session01/oopbasics/CarBuilderTest.java) | Each point above, checked |

## Run it

```bash
./mvnw -q -pl session-01-java-architecture-oop verify
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.oopbasics.BankAccount
```

```
ACC-1 (Niranjan): 425.50
ACC-2 (Asha): 1.00
Accounts opened: 2
Refused: insufficient funds: balance 100, asked 1000
```

Note: `accountsOpened++` is not thread-safe. Two threads opening accounts at once could get the same
number; [Session 5](../../../session-05-concurrency/) covers how to fix that.

## Interview questions

- **Class vs object?** Class = blueprint (compiled once); object = instance on the heap (many per class).
- **Can a constructor be `static`, `final` or return a value?** No. It has no return type, not even `void`.
- **What if you write no constructor?** The compiler adds a public no-argument one; it disappears as soon as you write any constructor.
- **`static` method calling an instance field?** Not allowed: there is no `this` in a static context.
- **Why override `hashCode` when overriding `equals`?** Hash-based collections look up the bucket by `hashCode` first, then compare with `equals`.
- **Why not `double` for money?** Binary floating point can't represent 0.1 exactly; use `long` minor units or `BigDecimal`.
