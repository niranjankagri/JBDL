# Session 6: Maven Project Management

| # | Topic | Code | README |
|---|---|---|---|
| 1 | The need for Maven | (README only; demo uses this module) | [01-why-maven](topics/01-why-maven/README.md) |
| 2 | Working with pom.xml and Maven repositories | [`pom.xml`](pom.xml), [`build-info.properties`](src/main/resources/build-info.properties), [`BuildInfo`](src/main/java/jbdl/session06/BuildInfo.java) | [02-pom-and-repositories](topics/02-pom-and-repositories/README.md) |
| 3 | Maven lifecycle management | [`MavenBuildTest`](src/test/java/jbdl/session06/MavenBuildTest.java) | [03-maven-lifecycle](topics/03-maven-lifecycle/README.md) |

This module exists to show Maven at work: a downloaded dependency (`commons-lang3`), a filtered
resource that carries the POM's version into the code, and tests that only pass if the lifecycle ran
in the right order. The whole repository is itself a multi-module Maven build with a parent POM and
the Maven Wrapper.

## What you can do after this session

- Explain what Maven solves compared to `javac` and hand-managed jars.
- Read and write a POM: coordinates, parent, modules, properties, dependencies and scopes, `dependencyManagement`, resources.
- Explain local vs remote repositories and transitive dependencies, and read `dependency:tree`.
- Choose the right lifecycle command and explain which plugin goals run in each phase.

## Run it

```bash
./mvnw -pl session-06-maven clean verify
./mvnw -pl session-06-maven dependency:tree
./mvnw -pl session-06-maven help:effective-pom
```
