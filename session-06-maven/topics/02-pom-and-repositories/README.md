# Topic 2: Working with pom.xml and Maven repositories

## The POM

`pom.xml` (Project Object Model) describes the project. The parts used in this repository:

| Element | Meaning | Where |
|---|---|---|
| `groupId`, `artifactId`, `version` | **Coordinates**: unique name of the artifact (`io.github.niranjankagri:session-06-maven:1.0.0-SNAPSHOT`) | every POM |
| `packaging` | `jar` (default), `war`, or `pom` for a parent/aggregator | parent: `pom` |
| `parent` | Inherit settings from another POM | [`session-06-maven/pom.xml`](../../pom.xml) |
| `modules` | Build these sub-projects together (multi-module) | [root `pom.xml`](../../../pom.xml) |
| `properties` | Variables, `${name}`; also standard ones like `maven.compiler.release` | both |
| `dependencies` | Libraries this project needs | both |
| `dependencyManagement` | Versions for dependencies **if** a module declares them (a BOM can be imported here) | root: `junit-bom` |
| `build/plugins`, `pluginManagement` | Tools and their configuration (Session 7) | root: compiler with `-Werror`, surefire |
| `build/resources` | Resource folders, with optional **filtering** | this module |

A `-SNAPSHOT` version means "work in progress": Maven may re-download newer snapshots. A release
version (`1.0.0`) never changes once published.

## Dependency scopes

| Scope | Compile | Test | Run / packaged | Example |
|---|---|---|---|---|
| `compile` (default) | ✓ | ✓ | ✓ | `commons-lang3` here, Spring Boot starters |
| `test` | | ✓ | | JUnit, AssertJ, Mockito |
| `provided` | ✓ | ✓ | (the server provides it) | Servlet API in a WAR, Lombok |
| `runtime` | | ✓ | ✓ | JDBC drivers |
| `import` | only in `dependencyManagement`, for BOMs | | | `junit-bom`, `spring-boot-dependencies` |

## Transitive dependencies: real `dependency:tree`

```bash
./mvnw -pl session-06-maven dependency:tree
```

```
io.github.niranjankagri:session-06-maven:jar:1.0.0-SNAPSHOT
+- org.apache.commons:commons-lang3:jar:3.21.0:compile
+- org.junit.jupiter:junit-jupiter:jar:6.1.3:test
|  +- org.junit.jupiter:junit-jupiter-api:jar:6.1.3:test
|  |  +- org.opentest4j:opentest4j:jar:1.3.0:test
|  |  +- org.junit.platform:junit-platform-commons:jar:6.1.3:test
|  |  +- org.apiguardian:apiguardian-api:jar:1.1.2:test
|  |  \- org.jspecify:jspecify:jar:1.0.0:test
|  +- org.junit.jupiter:junit-jupiter-params:jar:6.1.3:test
|  \- org.junit.jupiter:junit-jupiter-engine:jar:6.1.3:test
|     \- org.junit.platform:junit-platform-engine:jar:6.1.3:test
```

We declared one test dependency, `junit-jupiter`; Maven brought eight more jars. When two paths
bring different versions of the same library, Maven picks the **nearest** one in the tree (first
declared wins on a tie). Fix conflicts with `dependencyManagement` or `<exclusions>`.

## Repositories

```
your build ──▶ local repository ~/.m2/repository ──(missing?)──▶ remote: Maven Central (or a company Nexus/Artifactory)
```

- **Local**: a cache on your machine, laid out by coordinates. Real content for our library:
  ```
  ~/.m2/repository/org/apache/commons/commons-lang3/3.21.0/
      commons-lang3-3.21.0.jar        commons-lang3-3.21.0.jar.sha1
      commons-lang3-3.21.0.pom        commons-lang3-3.21.0.pom.sha1
      commons-lang3-3.21.0-sources.jar ...
  ```
  The `.pom` next to the jar is how Maven knows that library's own dependencies.
- **Central**: the default public repository (`https://repo.maven.apache.org/maven2`).
- **Remote/company**: configured in `<repositories>` or `~/.m2/settings.xml` (with credentials there,
  never in the POM). `./mvnw install` puts your own artifact in the local repository; `deploy`
  uploads it to a remote one.

## Resource filtering (this module)

[`build-info.properties`](../../src/main/resources/build-info.properties) contains `version=${project.version}`.
With `<filtering>true</filtering>`, Maven replaces the placeholders while copying it to `target/classes`:

```
artifact=session-06-maven
group=io.github.niranjankagri
version=1.0.0-SNAPSHOT
java.release=17
commons-lang3=3.21.0
```

[`BuildInfo`](../../src/main/java/jbdl/session06/BuildInfo.java) reads it at run time, and
[`MavenBuildTest`](../../src/test/java/jbdl/session06/MavenBuildTest.java) checks every value, plus that
`StringUtils` was loaded from the downloaded `commons-lang3` jar.

## Interview questions

- **What are Maven coordinates?** `groupId:artifactId:version` (+ packaging/classifier).
- **`dependencies` vs `dependencyManagement`?** Adds a dependency vs only fixes its version for modules that add it.
- **What is a BOM?** A POM of aligned versions imported with scope `import` (Spring Boot's `spring-boot-dependencies`).
- **SNAPSHOT vs release?** Mutable work-in-progress vs immutable published version.
- **How does Maven resolve version conflicts?** Nearest definition wins; override with `dependencyManagement`.
- **Where do credentials for a private repository go?** `~/.m2/settings.xml`, not the POM.
