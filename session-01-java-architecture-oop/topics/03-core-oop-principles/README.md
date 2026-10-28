# Topic 3: Core OOP principles

The four pillars, each with the class in this session that shows it.

| Principle | In one sentence | Where in the code |
|---|---|---|
| **Encapsulation** | Keep the data private; let the object guard its own rules | `BankAccount` (topic 2), the validating `Circle`/`Rectangle` constructors |
| **Inheritance** | A subclass IS-A parent and reuses its code (`extends`) | `Square extends Rectangle extends Shape`; `SuperCar extends Car` |
| **Polymorphism** | One call, many behaviours | `Shapes.totalArea` (overriding), `Shapes.label(...)` (overloading) |
| **Abstraction** | Show what an object does, hide how | `abstract class Shape` with `abstract double area()` |

## Encapsulation

Fields are `private`; the only way in is through methods that check the input. A `Circle` with radius
0 or a `BankAccount` with a negative balance can't be created, so every object that exists is valid.

## Inheritance

```
Shape (abstract)          Car
 ├── Circle                └── SuperCar
 └── Rectangle
      └── Square
```

- `extends` gives the subclass every non-private member of the parent; `super(...)` calls the
  parent constructor (must be the first line).
- `@Override` replaces a parent method; `super.getInfo()` still calls the parent version
  (`SuperCar.getInfo()` extends `Car`'s text instead of repeating it).
- Java has **single inheritance** for classes (one parent); a class can implement many interfaces
  (Session 2).
- Use inheritance only for a true IS-A relationship. `Square extends Rectangle` is safe here because
  both are immutable; with `setWidth`/`setHeight` a square could be stretched into a non-square
  (Liskov substitution principle). Prefer **composition** (HAS-A, a field) when in doubt.

## Polymorphism

| | Compile-time (static) | Run-time (dynamic) |
|---|---|---|
| Mechanism | **Overloading**: same name, different parameters | **Overriding**: subclass replaces a parent method |
| Decided by | The compiler, using the **declared** type | The JVM, using the **actual** object |
| Example | `label(Shape)`, `label(Shape, int)`, `label(Circle)` | `shape.area()` in `totalArea` |

```java
Shape someShape = new Circle(1);
label(someShape);        // "shape: Circle"        <- overload picked by declared type Shape
label(new Circle(1));    // "round shape: Circle"  <- overload picked by declared type Circle
someShape.area();        // Circle's area()        <- override picked by the actual object
```

## Abstraction

`Shape` is `abstract`: it can't be created with `new Shape(...)`, and it forces every subclass to
implement `area()`. `describe()` is written once in `Shape` and calls the abstract `area()`, so each
shape describes itself correctly (the template method idea). Session 2 compares abstract classes
with interfaces.

## Design patterns used

- **Factory** ([`CarFactory`](../../src/main/java/jbdl/session01/principles/CarFactory.java)): callers ask for
  a kind of car and get a `Car`, without knowing which subclass was built.
- **Builder** ([`oopbasics.Car`](../../src/main/java/jbdl/session01/oopbasics/Car.java), topic 2).

## The code

| File | What it shows |
|---|---|
| [`Shape.java`](../../src/main/java/jbdl/session01/principles/Shape.java), [`Circle.java`](../../src/main/java/jbdl/session01/principles/Circle.java), [`Rectangle.java`](../../src/main/java/jbdl/session01/principles/Rectangle.java), [`Square.java`](../../src/main/java/jbdl/session01/principles/Square.java) | Abstraction, inheritance, encapsulated validation |
| [`Shapes.java`](../../src/main/java/jbdl/session01/principles/Shapes.java) | Run-time vs compile-time polymorphism |
| [`Car.java`](../../src/main/java/jbdl/session01/principles/Car.java), [`SuperCar.java`](../../src/main/java/jbdl/session01/principles/SuperCar.java), [`CarFactory.java`](../../src/main/java/jbdl/session01/principles/CarFactory.java), [`InheritanceDemo.java`](../../src/main/java/jbdl/session01/principles/InheritanceDemo.java) | (2024 code, upgraded) constructor chaining, `super`, overriding, factory |
| [`ShapesTest.java`](../../src/test/java/jbdl/session01/principles/ShapesTest.java), [`InheritanceTest.java`](../../src/test/java/jbdl/session01/principles/InheritanceTest.java) | Each principle, checked |

## Run it

```bash
./mvnw -q -pl session-01-java-architecture-oop verify
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.principles.Shapes
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.principles.InheritanceDemo
```

```
Circle with area 3.14
Rectangle with area 6.00
Square with area 4.00
Total area: 13.14
shape: Circle
round shape: Circle
shape: Circle (3.142)
```

## Interview questions

- **Overloading vs overriding?** Overloading: same class, different parameters, compile time. Overriding: subclass, same signature, run time.
- **Can you override a `static` or `private` method?** No. A static method with the same signature in a subclass *hides* the parent one; private methods aren't inherited.
- **Can an abstract class have a constructor?** Yes. Subclasses call it with `super(...)` (`Shape(String name)`).
- **Why no multiple inheritance of classes?** The diamond problem: two parents with the same method. Interfaces solve it with explicit rules (Session 2).
- **Composition vs inheritance?** Inheritance = IS-A, tight coupling to the parent. Composition = HAS-A, swap parts freely. Prefer composition unless the IS-A is real.
