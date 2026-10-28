# Topic 1: Introduction to Java architecture

How a `.java` file becomes a running program, and where the JVM keeps your data while it runs.

## From source code to a running program

```
 Hello.java ──javac──▶ Hello.class ──▶ JVM: class loader ─▶ bytecode verifier ─▶ interpreter + JIT ─▶ CPU
 (source)              (bytecode,         loads classes     checks the bytecode   runs it; hot code is
                        same on every     on first use      is safe to run        compiled to machine code
                        operating system)
```

| Part | What it is | Who uses it |
|---|---|---|
| **JDK** (Java Development Kit) | JRE + tools: `javac` (compiler), `javap` (disassembler), `jar`, `jshell`, `jdeps` | Developers |
| **JRE** (Java Runtime Environment) | JVM + the standard class library (`java.base`, `java.sql`, ...) | Anyone running Java (since Java 11 there is no separate JRE download; `jlink` builds a custom runtime) |
| **JVM** (Java Virtual Machine) | Loads, verifies and runs bytecode; manages memory and threads | Runs on each operating system; the bytecode stays the same |

**Write once, run anywhere:** `javac` produces bytecode, not machine code. The same `.class` files run on
Windows, Linux or macOS because each platform has its own JVM that turns bytecode into machine code.

### Inside the JVM

- **Class loader subsystem**: loads a class the first time it is used. Loaders delegate to their parent
  first, so a class can't replace a core class like `String`:
  `application ("app")` ─▶ `platform` ─▶ `bootstrap` (built in, shown as `null` in Java).
- **Runtime data areas**:
  - **Heap**: all objects and arrays; shared by all threads; cleaned by the **garbage collector**.
  - **Stack**: one per thread; one **frame** per method call holding local variables, parameters
    and partial results. Freed when the method returns.
  - **Metaspace** (method area): class metadata, method bytecode, static fields' definitions.
  - **PC register**: per thread, the bytecode instruction being executed.
- **Execution engine**: the **interpreter** starts running bytecode at once; the **JIT compiler**
  (just-in-time) compiles frequently used ("hot") methods to machine code; the **garbage collector**
  frees heap objects nothing refers to any more (default collector since Java 9: G1).

## The code

| File | What it shows |
|---|---|
| [`JvmInfo.java`](../../src/main/java/jbdl/session01/architecture/JvmInfo.java) | What the running JVM reports: version, vendor, OS, processors, max heap (`-Xmx`) |
| [`ClassLoaders.java`](../../src/main/java/jbdl/session01/architecture/ClassLoaders.java) | The loader chain of a class: `String` → `[bootstrap]`, `java.sql.Date` → `[platform, bootstrap]`, our classes → `[app, platform, bootstrap]` |
| [`StackAndHeap.java`](../../src/main/java/jbdl/session01/architecture/StackAndHeap.java) | Java passes everything **by value**: a copied primitive, a shared object, a reassigned parameter |
| [`JavaQuiz.java`](../../src/main/java/jbdl/session01/architecture/JavaQuiz.java) | Primitive and wrapper traps from the 2024 quiz: `byte` overflow, the `Integer` cache with `==`, post-increment |
| [`ArchitectureTest.java`](../../src/test/java/jbdl/session01/architecture/ArchitectureTest.java), [`JavaQuizTest.java`](../../src/test/java/jbdl/session01/architecture/JavaQuizTest.java) | Every statement above, checked |

### Pass by value: stack and heap

```java
int number = 5;
addTen(number);          // the method gets a COPY of 5 in its own stack frame -> number is still 5

Box box = new Box(5);    // the Box object lives on the heap; `box` holds a reference to it
addTen(box);             // the method gets a copy of the REFERENCE -> same object -> box.getValue() == 15
replace(box);            // the method points its copy at a new Box -> the caller's box is untouched (15)
```

### Primitives and wrappers

| Expression | Result | Why |
|---|---|---|
| `(byte) (127 + 1)` | `-128` | `byte` is 8 bits (-128..127); the value wraps around |
| `Integer a = 1, b = 1; a == b` | `true` | Autoboxing calls `Integer.valueOf`, which reuses cached objects for -128..127 |
| `Integer a = 1000, b = 1000; a == b` | `false` | Outside the cache: two different objects. Compare values with `equals` |
| `a = 3; a++ > 3 \|\| a == 4` | `true` ("GFG") | `a++` compares the **old** value (3 > 3 is false), then `a` is 4 |

The post-increment, as bytecode (`javap -c` on the compiled class; real output):

```
 2: iload_1          // push a (3) onto the operand stack     <- the OLD value is used
 3: iinc    1, 1     // a = a + 1 in the local variable (now 4)
 6: iconst_3
 7: if_icmpgt 15     // 3 > 3? no
10: iload_1          // push a again (4)
11: iconst_4
12: if_icmpne 18     // 4 != 4? no -> return "GFG"
```

## Run it

```bash
./mvnw -q -pl session-01-java-architecture-oop verify      # build + tests of this session
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.architecture.JvmInfo
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.architecture.ClassLoaders
java -cp session-01-java-architecture-oop/target/classes jbdl.session01.architecture.StackAndHeap
javap -c -cp session-01-java-architecture-oop/target/classes jbdl.session01.architecture.JavaQuiz
```

Sample output on this machine (JDK 27, Windows 11):

```
String (java.base):        [bootstrap]
java.sql.Date (java.sql):  [platform, bootstrap]
ClassLoaders (this course): [app, platform, bootstrap]

Primitive after addTen:   5  (unchanged)
Object after addTen:      15 (changed: same object)
Object after replace:     15 (unchanged: new object was local)
```

## Interview questions

- **JDK vs JRE vs JVM?** JDK = tools + JRE; JRE = JVM + libraries; JVM = runs bytecode.
- **Why is Java platform independent but the JVM is not?** Bytecode is the same everywhere; each OS needs its own JVM.
- **Is Java pass-by-reference?** No. Always pass-by-value; for objects the value is the reference.
- **Stack vs heap?** Stack: per thread, method frames, local variables, freed on return. Heap: shared, all objects, freed by the GC.
- **What is the JIT?** It compiles hot bytecode to native machine code at run time, so long-running code gets fast.
- **Parent delegation?** A loader asks its parent first, so core classes can't be replaced by application classes.
