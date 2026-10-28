# Session 8: Spring MVC Basics & REST API Implementation

A Spring Boot 4 bookstore: a REST API at `/api/books` and a server-rendered page at `/books`.

| # | Topic | Code | README |
|---|---|---|---|
| 1 | Overview of the Spring MVC architecture | [`BookstoreApplication`](src/main/java/jbdl/session08/BookstoreApplication.java) | [01-spring-mvc-architecture](topics/01-spring-mvc-architecture/README.md) |
| 2 | Model, View and Controller components | [`web/BookPageController`](src/main/java/jbdl/session08/web/BookPageController.java), [`templates/books.html`](src/main/resources/templates/books.html) | [02-model-view-controller](topics/02-model-view-controller/README.md) |
| 3 | Core HTTP methods | [`books/BookController`](src/main/java/jbdl/session08/books/BookController.java) | [03-http-methods](topics/03-http-methods/README.md) |
| 4 | Design and implement RESTful APIs | [`books`](src/main/java/jbdl/session08/books), [`web/ApiExceptionHandler`](src/main/java/jbdl/session08/web/ApiExceptionHandler.java) | [04-restful-api-design](topics/04-restful-api-design/README.md) |

## What you can do after this session

- Trace a request through Tomcat, the DispatcherServlet, a controller and back.
- Build a server-rendered page with a controller, a model and a Thymeleaf view.
- Use GET, POST, PUT, PATCH and DELETE with the right status codes, and explain safe and idempotent.
- Design a REST resource with DTOs, validation and Problem Details errors.

## Run it

```bash
./mvnw -pl session-08-spring-mvc-rest verify                     # 10 tests (MockMvc)
java -jar session-08-spring-mvc-rest/target/session-08-spring-mvc-rest-1.0.0-SNAPSHOT.jar
curl -X POST localhost:8080/api/books -H 'Content-Type: application/json' \
     -d '{"title":"Clean Code","author":"Robert C. Martin","isbn":"9780132350884","price":450.00,"publishedYear":2008}'
# then open http://localhost:8080/books
```

Stack: Spring Boot 4.1.2 (Spring Framework 7), embedded Tomcat, Jackson, Hibernate Validator,
Thymeleaf. Versions come from the Spring Boot BOM imported in [`pom.xml`](pom.xml).
