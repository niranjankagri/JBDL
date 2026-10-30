# Topic 1: Introduction to Maven plugins

Maven itself only knows the lifecycle (Session 6). **All real work is done by plugins**: compiling
(`maven-compiler-plugin`), running tests (`maven-surefire-plugin`), building the jar
(`maven-jar-plugin`). A plugin offers **goals**; a goal is bound to a **phase**, by default or by your
`<execution>`.

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.15</version>                 <!-- always pin plugin versions -->
    <executions>
        <execution>
            <id>check</id>                    <!-- shown in the log: (check) -->
            <phase>verify</phase>             <!-- when -->
            <goals><goal>check</goal></goals> <!-- what -->
            <configuration>...</configuration><!-- how -->
        </execution>
    </executions>
</plugin>
```

## Build plugins vs reporting plugins

- **Build plugins** (`<build><plugins>`): run during the build. Everything in this module.
- **Reporting plugins** (`<reporting>`): run during `mvn site` to produce HTML project reports.

`<pluginManagement>` in a parent POM fixes versions and configuration **without** activating the
plugin; a module activates it by listing it. This repository's root POM does that for the compiler
(with `-Xlint:all -Werror`) and surefire.

## The plugins in this module

| Plugin | Goal → phase | What it does here |
|---|---|---|
| `maven-enforcer-plugin` | `enforce` → validate | Fails fast unless Java ≥ 17, Maven ≥ 3.9, and every dependency converges to one version |
| `jacoco-maven-plugin` | `prepare-agent` → initialize; `report`, `check` → verify | Measures test coverage, writes `target/site/jacoco/index.html`, **fails the build below 90 % line coverage** |
| `maven-jar-plugin` | `jar` → package | Adds `Main-Class: jbdl.session07.Cli` to the manifest |
| `maven-shade-plugin` | `shade` → package | Builds a second, "fat" jar (`-all`) with `commons-lang3` inside |

## Real log: `./mvnw -pl session-07-maven-plugins clean verify`

```
[INFO] --- enforcer:3.6.3:enforce (enforce-build-environment) @ session-07-maven-plugins ---
[INFO] Rule 0: org.apache.maven.enforcer.rules.version.RequireJavaVersion passed
[INFO] Rule 1: org.apache.maven.enforcer.rules.version.RequireMavenVersion passed
[INFO] Rule 2: org.apache.maven.enforcer.rules.dependency.DependencyConvergence passed
[INFO] --- jacoco:0.8.15:prepare-agent (prepare-agent) @ session-07-maven-plugins ---
[INFO] --- compiler:3.16.0:compile (default-compile) ...
[INFO] --- surefire:3.6.1:test (default-test) ...
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] --- jar:3.5.1:jar (default-jar) ...
[INFO] --- shade:3.6.2:shade (default) ...
[INFO] --- jacoco:0.8.15:report (report) ...
[INFO] --- jacoco:0.8.15:check (check) ...
[INFO] All coverage checks have been met.
[INFO] BUILD SUCCESS
```

### The quality gate failing for real

The first build of this module had only two tests and nothing tested `main`. JaCoCo stopped it:

```
[WARNING] Rule violated for bundle session-07-maven-plugins: lines covered ratio is 0.71, but expected minimum is 0.90
[ERROR] Failed to execute goal org.jacoco:jacoco-maven-plugin:0.8.15:check (check) ... Coverage checks have not been met.
```

Adding `CliTest.mainPrints` brought it to 100 %. That's what a plugin-based gate is for: the build,
not a reviewer, refuses untested code.

### Thin jar vs fat jar

```
$ java -jar session-07-maven-plugins/target/session-07-maven-plugins-1.0.0-SNAPSHOT-all.jar "Spring Boot & Microservices"
spring-boot-microservices

$ java -jar session-07-maven-plugins/target/session-07-maven-plugins-1.0.0-SNAPSHOT.jar Hello
Exception in thread "main" java.lang.NoClassDefFoundError: org/apache/commons/lang3/StringUtils
```

| Jar | Files | Size | Runs alone? |
|---|---|---|---|
| `session-07-maven-plugins-1.0.0-SNAPSHOT.jar` | 10 | 8 KB | No: `commons-lang3` must be on the classpath |
| `session-07-maven-plugins-1.0.0-SNAPSHOT-all.jar` (shaded) | 479 | 1.7 MB | Yes |

The shade plugin also warned that both jars contain `META-INF/MANIFEST.MF`; the `<filters>` exclude the
library's manifest and `ManifestResourceTransformer` writes ours. Spring Boot does the fat-jar job
with its own `spring-boot-maven-plugin` (Session 8).

## Other plugins you'll meet

`maven-failsafe-plugin` (integration tests in `integration-test`/`verify`), `maven-dependency-plugin`
(`tree`, `analyze`), `versions-maven-plugin` (`display-dependency-updates`), `spotless`/`checkstyle`
(formatting, style), `spring-boot-maven-plugin` (`repackage`, `run`, `build-image`), `jib` (container images).

## The code

| File | What it shows |
|---|---|
| [`pom.xml`](../../pom.xml) | Four plugins, each commented with its phase |
| [`Cli.java`](../../src/main/java/jbdl/session07/Cli.java) | A small CLI packaged by the jar and shade plugins |
| [`CliTest.java`](../../src/test/java/jbdl/session07/CliTest.java) | The tests that keep JaCoCo's gate green |

## Interview questions

- **Plugin vs goal vs phase?** A plugin has goals; a goal is bound to a phase; a phase is a step of a lifecycle.
- **How do you run a goal without a phase?** `mvn plugin-prefix:goal`, e.g. `mvn dependency:tree`.
- **`plugins` vs `pluginManagement`?** Activate vs only configure for children.
- **Surefire vs failsafe?** Unit tests in `test` vs integration tests in `integration-test`/`verify` (the build fails only after cleanup).
- **How do you build an executable jar?** Manifest `Main-Class` + dependencies: shade/assembly plugin, or `spring-boot-maven-plugin`.
