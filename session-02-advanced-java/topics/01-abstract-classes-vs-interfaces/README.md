# Topic 1: Abstract classes vs interfaces

Both describe something callers can use without knowing the concrete class. The difference is what
they may contain and how many a class can have.

| | Abstract class | Interface |
|---|---|---|
| Keyword | `abstract class X`, used with `extends` | `interface X`, used with `implements` |
| How many per class | **One** (single inheritance) | **Many** |
| Instance fields (state) | Yes | No; fields are always `public static final` constants |
| Constructors | Yes (called by subclasses with `super(...)`) | No |
| Method kinds | Abstract, concrete, `final`, `static`, any access level | Abstract (implicitly `public`), `default` (Java 8), `static` (Java 8), `private` (Java 9) |
| Expresses | IS-A with shared state and code ("a Developer is an Employee") | CAN-DO, a capability ("an Invoice is Payable") |
| Unrelated classes | No, they share a parent | Yes, any class can implement it |

**Rule of thumb:** start with an interface. Add an abstract class when several implementations
share state or a fixed algorithm (often the abstract class implements the interface, as `Employee`
implements `Payable` here).

## The code

```
            «interface» Payable                 «interface» Taxable
            monthlyPayInRupees()                taxableIncomeInRupees()
            default yearlyPayInRupees()         default taxInRupees() + private helper
            static totalMonthly(list)           constant TAX_FREE_LIMIT_IN_RUPEES
                 ▲            ▲                        ▲
                 │            │                        │
      «abstract» Employee   Invoice (record)           │
      state: name, salary                              │
      final monthlyPayInRupees()  ← template           │
      abstract bonusInRupees()    ← step               │
          ▲            ▲                               │
      Developer      Manager ──────────────────────────┘
```

| File | What it shows |
|---|---|
| [`Payable.java`](../../src/main/java/jbdl/session02/abstraction/Payable.java) | Interface with an abstract, a `default` and a `static` method |
| [`Taxable.java`](../../src/main/java/jbdl/session02/abstraction/Taxable.java) | Interface constant, `default` method using a `private` interface method |
| [`Employee.java`](../../src/main/java/jbdl/session02/abstraction/Employee.java) | Abstract class with state, constructor and a **template method** (`final monthlyPayInRupees` calls the abstract `bonusInRupees`) |
| [`Developer.java`](../../src/main/java/jbdl/session02/abstraction/Developer.java), [`Manager.java`](../../src/main/java/jbdl/session02/abstraction/Manager.java) | Fill in the step; `Manager` extends one class and implements a second interface |
| [`Invoice.java`](../../src/main/java/jbdl/session02/abstraction/Invoice.java) | An unrelated `record` that is also `Payable` and overrides a default method |
| [`SmartPhone.java`](../../src/main/java/jbdl/session02/abstraction/SmartPhone.java) | The **diamond problem** with default methods: two interfaces define `describe()`, the class must override and picks with `Camera.super.describe()` |
| [`PaymentMethod.java`](../../src/main/java/jbdl/session02/abstraction/PaymentMethod.java) | **Sealed** interface (Java 17): only `Card`, `Upi` and `Cash` may implement it |
| [`AbstractionTest.java`](../../src/test/java/jbdl/session02/abstraction/AbstractionTest.java) | Each row of the table above, checked |

## Run it

```bash
java -cp session-02-advanced-java/target/classes jbdl.session02.abstraction.AbstractionDemo
```

```
Developer(Asha) -> monthly 105000
Manager(Ravi) -> monthly 206000
Invoice[supplier=Cloud hosting, amountInRupees=15000] -> monthly 15000
Total monthly: 326000
Manager tax: 210000
Smartphone: takes photos + plays music
UPI niranjan@upi
```

## Interview questions

- **Why were default methods added in Java 8?** To add methods (like `forEach` on `Iterable`) to existing interfaces without breaking every class that implements them.
- **Two interfaces with the same default method?** Compile error until the class overrides it; it can call `A.super.m()` or `B.super.m()`.
- **Can an interface have a constructor or instance fields?** No. Fields are constants.
- **Abstract class with no abstract methods?** Allowed: it just can't be instantiated.
- **Functional interface?** An interface with exactly one abstract method (default/static ones don't count): it can be a lambda (Session 3).
- **Sealed vs final?** `final` allows no subclasses; `sealed` allows exactly the `permits` list.
