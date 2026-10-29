# Topic 3: Maven lifecycle management

Maven has three built-in **lifecycles**; each is an ordered list of **phases**. Running a phase runs
**every phase before it** in that lifecycle.

| Lifecycle | Main phases (in order) |
|---|---|
| **clean** | `pre-clean` → `clean` (delete `target/`) → `post-clean` |
| **default** (build) | `validate` → `compile` → `test-compile` → `test` → `package` → `verify` → `install` → `deploy` |
| **site** | `site` → `site-deploy` (project documentation) |

The full default lifecycle has 23 phases (`generate-sources`, `process-resources`,
`process-test-resources`, `integration-test`, ...); the table shows the ones you call.

| Phase | Does |
|---|---|
| `validate` | Checks the POM is correct |
| `compile` | Compiles `src/main/java` (after `process-resources` copies and filters resources) |
| `test` | Compiles and runs unit tests; a failing test stops the build |
| `package` | Builds the jar/war in `target/` |
| `verify` | Runs checks on the package (integration tests, quality gates); what CI runs here |
| `install` | Copies the artifact to `~/.m2/repository` so other local projects can use it |
| `deploy` | Uploads it to a remote repository |

## Phases are carried out by plugin goals

A phase does nothing by itself; **plugin goals** are bound to it. Real log of
`./mvnw -pl session-06-maven clean package` (only the step lines):

```
[INFO] --- clean:3.5.0:clean (default-clean) @ session-06-maven ---                  clean phase
[INFO] --- resources:3.5.0:resources (default-resources) @ session-06-maven ---      process-resources (filtering here)
[INFO] --- compiler:3.16.0:compile (default-compile) @ session-06-maven ---          compile
[INFO] --- resources:3.5.0:testResources (default-testResources) @ session-06-maven ---
[INFO] --- compiler:3.16.0:testCompile (default-testCompile) @ session-06-maven ---  test-compile
[INFO] --- surefire:3.6.1:test (default-test) @ session-06-maven ---                 test
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jar:3.5.1:jar (default-jar) @ session-06-maven ---                        package
[INFO] Building jar: ...\session-06-maven\target\session-06-maven-1.0.0-SNAPSHOT.jar
[INFO] BUILD SUCCESS
```

Format: `plugin:version:goal (execution id)`. You can also run a goal directly, outside any phase:
`./mvnw dependency:tree`, `./mvnw help:effective-pom`.

## Commands you'll use

| Command | Effect |
|---|---|
| `./mvnw clean verify` | Fresh full build with all tests (what this repo's CI runs) |
| `./mvnw test` | Compile and unit-test |
| `./mvnw package -DskipTests` | Build the jar without running tests (still compiles them; `-Dmaven.test.skip=true` skips compiling too) |
| `./mvnw -pl session-06-maven verify` | One module (`-pl`), add `-am` to also build the modules it depends on |
| `./mvnw -o verify` | Offline: only the local repository |
| `./mvnw -q` / `-X` | Quiet / debug output |
| `./mvnw help:effective-pom` | The POM after inheritance and defaults: what Maven really uses |

In a multi-module build (this repository), Maven runs the lifecycle for each module in dependency
order (the "reactor"): `./mvnw verify` builds every session.

## The code

The module's [`pom.xml`](../../pom.xml) is commented line by line; [`MavenBuildTest`](../../src/test/java/jbdl/session06/MavenBuildTest.java)
only passes because `process-resources` filtered the properties file before the tests ran.

## Interview questions

- **Lifecycle vs phase vs goal?** Lifecycle = ordered phases; phase = a step; goal = a plugin task bound to a phase.
- **What does `mvn install` run?** Every default phase up to `install`: validate, compile, test, package, verify, install.
- **`package` vs `install`?** Jar in `target/` vs also copied into `~/.m2/repository`.
- **Is `clean` part of `install`?** No, it's a separate lifecycle: `mvn clean install` runs both.
- **How do you skip tests?** `-DskipTests` (compile, don't run) or `-Dmaven.test.skip=true` (don't compile either).
