# Session 7: Maven Plugins

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Introduction to Maven plugins | [`pom.xml`](pom.xml) (enforcer, JaCoCo, jar, shade), [`Cli`](src/main/java/jbdl/session07/Cli.java) | [01-maven-plugins](topics/01-maven-plugins/README.md) |
| 2 | Create a new Maven project using archetypes | (README only: real `archetype:generate` run) | [02-maven-archetypes](topics/02-maven-archetypes/README.md) |

## What you can do after this session

- Configure a plugin with executions, phases, goals and configuration, and pin its version.
- Add build guards (enforcer rules, a coverage gate) and package a runnable fat jar.
- Generate a new project from an archetype and bring its versions up to date.

## Run it

```bash
./mvnw -pl session-07-maven-plugins clean verify      # enforcer, tests, jar, shade, coverage gate
java -jar session-07-maven-plugins/target/session-07-maven-plugins-1.0.0-SNAPSHOT-all.jar Hello Maven
# coverage report: session-07-maven-plugins/target/site/jacoco/index.html
```
