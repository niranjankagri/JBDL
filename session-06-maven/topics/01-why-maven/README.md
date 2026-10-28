# Topic 1: The need for Maven

## Building Java by hand

Without a build tool, this module's small program needs:

```bash
# 1. find and download commons-lang3-3.21.0.jar yourself (and every jar IT needs)
# 2. compile with the jar on the classpath (';' on Windows, ':' elsewhere)
javac -cp commons-lang3-3.21.0.jar -d out src/main/java/jbdl/session06/*.java
# 3. copy resources and replace version numbers in them by hand
# 4. compile and run the tests with JUnit's jars on the classpath
# 5. build a jar, then run with the right classpath again
java -cp "out;commons-lang3-3.21.0.jar" jbdl.session06.BuildInfo
```

Forget the jar in step 5, and it compiles fine but fails at run time. Real output:

```
$ java -cp session-06-maven/target/classes jbdl.session06.BuildInfo
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/lang3/StringUtils
	at jbdl.session06.TextTools.headline(TextTools.java:17)
```

Multiply that by 100 libraries, their versions, their own dependencies, a team of developers and a
CI server, and every machine builds slightly differently.

## What Maven gives you

| Problem | Maven's answer |
|---|---|
| Finding and downloading jars | Declare `groupId:artifactId:version` in `pom.xml`; Maven downloads from **Maven Central** into `~/.m2/repository` |
| Dependencies of dependencies | **Transitive** resolution: it reads each library's own POM |
| Which jars on which classpath | **Scopes**: `compile`, `test`, `provided`, `runtime` (topic 2) |
| "Works on my machine" | One standard **lifecycle**: `./mvnw verify` does the same steps everywhere (topic 3) |
| Every project laid out differently | **Convention over configuration**: `src/main/java`, `src/test/java`, `src/main/resources`, output in `target/` |
| Tools (compiler, tests, packaging, reports) | **Plugins**, configured once in the POM (Session 7) |
| Different Maven versions on each machine | The **Maven Wrapper** (`./mvnw`) downloads one pinned, checksum-verified Maven |

## Maven vs Gradle

Both do dependency management and builds. Maven: declarative XML, one fixed lifecycle, very
predictable. Gradle: a Kotlin/Groovy build script, flexible tasks, incremental builds and a build cache
(faster on large projects; Android's default). Spring Boot supports both; this course uses Maven.

## The standard layout (this repository)

```
pom.xml                                parent: shared settings for all sessions
session-06-maven/
  pom.xml                              this module
  src/main/java/jbdl/session06/        application code         → target/classes
  src/main/resources/                  files copied to the jar  → target/classes
  src/test/java/jbdl/session06/        tests                    → target/test-classes
  target/                              everything Maven builds (never committed)
```

## Interview questions

- **What is Maven?** A build and dependency management tool based on a project object model (POM).
- **What is "convention over configuration"?** Follow the standard layout and lifecycle and you configure almost nothing.
- **Why the Maven Wrapper?** Everyone and CI use the same Maven version without installing it.
- **`NoClassDefFoundError` vs `ClassNotFoundException`?** The class was there at compile time but not at run time (classpath) vs a class looked up by name (reflection) isn't found.
