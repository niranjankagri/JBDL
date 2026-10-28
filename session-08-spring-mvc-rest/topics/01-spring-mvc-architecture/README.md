# Topic 1: Overview of the Spring MVC architecture

Spring MVC is Spring's web framework on top of the Servlet API. Spring Boot adds an **embedded
Tomcat**, so the application is a plain `java -jar` with the web server inside.

## One request, end to end

```
HTTP request
   │
   ▼
Tomcat (embedded) ──▶ Filters (security, logging, CORS)
   │
   ▼
DispatcherServlet  ── the "front controller": every request enters here
   │ 1. HandlerMapping: which controller method matches? (@GetMapping("/api/books/{id}") ...)
   │ 2. HandlerAdapter: call it; resolve arguments (@PathVariable, @RequestParam, @RequestBody via Jackson, @Valid)
   ▼
Controller method  ──▶ Service ──▶ (Repository / database, Sessions 10-11)
   │
   │ returns an object (@RestController)            or a view name + Model (@Controller)
   ▼                                                  ▼
HttpMessageConverter (Jackson → JSON)              ViewResolver → Thymeleaf template → HTML
   │                                                  │
   └──────────────────────────┬───────────────────────┘
                              ▼
                        HTTP response
   (if anything throws: HandlerExceptionResolver → @RestControllerAdvice → Problem Details)
```

## The parts in this module

| Spring MVC part | Here |
|---|---|
| Embedded server | Tomcat, from `spring-boot-starter-webmvc`: `Tomcat started on port 18080 (http)` |
| `DispatcherServlet` | Auto-configured by Spring Boot, mapped to `/` |
| Handler mapping | `@RequestMapping("/api/books")` + `@GetMapping`, `@PostMapping`... on [`BookController`](../../src/main/java/jbdl/session08/books/BookController.java) |
| Argument resolvers | `@PathVariable long id`, `@RequestParam String author`, `@Valid @RequestBody BookRequest` |
| Message converter | Jackson turns `Book` records into JSON and JSON into `BookRequest` |
| View resolver | Thymeleaf: view name `"books"` → `templates/books.html` ([`BookPageController`](../../src/main/java/jbdl/session08/web/BookPageController.java)) |
| Exception resolver | [`ApiExceptionHandler`](../../src/main/java/jbdl/session08/web/ApiExceptionHandler.java) (`@RestControllerAdvice`) |

## Beans and dependency injection

`@SpringBootApplication` on [`BookstoreApplication`](../../src/main/java/jbdl/session08/BookstoreApplication.java)
turns on **component scanning**: classes annotated `@RestController`, `@Controller`, `@Service`,
`@Component`, `@Repository` in its package become **beans**, objects created and wired by Spring.
`BookController` asks for a `BookService` in its constructor and Spring passes the one shared
instance (**constructor injection**). Beans are singletons by default (Session 2, topic 5), so they
must be thread-safe: `BookService` uses a `ConcurrentHashMap`.

## Starting the real application

```bash
./mvnw -pl session-08-spring-mvc-rest package
java -jar session-08-spring-mvc-rest/target/session-08-spring-mvc-rest-1.0.0-SNAPSHOT.jar --server.port=18080
```

```
o.s.boot.tomcat.TomcatWebServer          : Tomcat started on port 18080 (http) with context path '/'
jbdl.session08.BookstoreApplication      : Started BookstoreApplication in 3.734 seconds
```

The jar is 23 MB: `spring-boot-maven-plugin` (`repackage` goal, Session 7) put Spring, Tomcat and
Jackson inside it.

## Interview questions

- **What is the DispatcherServlet?** The front controller that receives every request and delegates to handler mappings, adapters, converters and view resolvers.
- **`@Controller` vs `@RestController`?** View names vs response bodies (`@RestController` = `@Controller` + `@ResponseBody`).
- **What does `@SpringBootApplication` do?** `@Configuration` + `@EnableAutoConfiguration` + `@ComponentScan`.
- **Why constructor injection?** Required dependencies, `final` fields, easy to test with plain `new`.
- **Are controllers thread-safe?** They're singletons called by many threads; keep them stateless.
