# Topic 2: Create a new Maven project using archetypes

An **archetype** is a project template. The `maven-archetype-plugin`'s `generate` goal copies it and
fills in your coordinates, so a new project starts with the standard layout, a POM and a first test.

## Generating a project (real run)

```bash
./mvnw -B archetype:generate \
  -DarchetypeGroupId=org.apache.maven.archetypes \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.5 \
  -DgroupId=com.example.shop -DartifactId=order-service \
  -Dversion=0.1.0-SNAPSHOT -Dpackage=com.example.shop
```

`-B` (batch mode) takes every value from the command line; without it Maven asks for them one by
one, and without `-DarchetypeArtifactId` it lists thousands of archetypes to choose from.

```
[INFO] --- archetype:3.4.1:generate (default-cli) @ standalone-pom ---
[INFO] Parameter: groupId, Value: com.example.shop
[INFO] Parameter: artifactId, Value: order-service
[INFO] Parameter: junitVersion, Value: 5.11.0
[INFO] Parameter: javaCompilerVersion, Value: 17
[INFO] Project created from Archetype in dir: ...\archetype-demo\order-service
[INFO] BUILD SUCCESS
```

What it created:

```
order-service/
├── .mvn/jvm.config
├── .mvn/maven.config
├── pom.xml                                   groupId/artifactId/version filled in, release 17, junit-bom 5.11.0
└── src/
    ├── main/java/com/example/shop/App.java   "Hello World!"
    └── test/java/com/example/shop/AppTest.java
```

It builds and runs straight away: `mvn package` → `target/order-service-0.1.0-SNAPSHOT.jar`, and
`java -cp target/classes com.example.shop.App` prints `Hello World!`.

## After generating: update the versions

The template is frozen at the time it was published. The generated POM pins **JUnit 5.11.0, surefire
3.3.0, compiler 3.13.0**, while this repository already uses JUnit 6.1.3, surefire 3.6.1 and compiler
3.16.0. First steps on a generated project: update versions (or import a BOM), add the Maven Wrapper
(`mvn wrapper:wrapper`), add `.gitignore` with `target/`.

## Common archetypes

| Archetype | Gives you |
|---|---|
| `maven-archetype-quickstart` | A plain jar project with one class and one test (above) |
| `maven-archetype-webapp` | A WAR project with `src/main/webapp/WEB-INF/web.xml` |
| `maven-archetype-archetype` | A project for writing your own archetype |
| Your own (`archetype:create-from-project`) | Your company's standard service template |

For Spring Boot, the usual "archetype" is **Spring Initializr** (start.spring.io, or `curl https://start.spring.io/starter.zip -d dependencies=web`):
it generates a Maven project with the Boot parent, starters, the wrapper and a test. Session 8 starts
from that shape.

## Interview questions

- **What is an archetype?** A Maven project template used by `archetype:generate`.
- **Why `-B`?** Non-interactive: needed in scripts and CI.
- **How do you create your own?** `mvn archetype:create-from-project` on a template project, then install/deploy it.
- **Archetype vs Spring Initializr?** Generic Maven templates vs a Spring Boot-specific generator with dependency selection.
