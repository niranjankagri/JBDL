# Topic 4: Design and implement RESTful APIs

## Design rules applied to `/api/books`

| Rule | Applied |
|---|---|
| URLs name **resources** (nouns, plural), methods say the action | `GET /api/books/1`, not `GET /getBook?id=1` |
| Collection vs item | `/api/books` (list, create) and `/api/books/{id}` (read, replace, patch, delete) |
| Filters, sorting, paging as query parameters | `GET /api/books?author=Joshua Bloch` |
| Correct status codes | 201 + `Location` on create, 204 on delete, 404 / 400 with a body |
| The client never sends server-owned fields | `BookRequest` has no `id`; the server assigns it |
| Validate input at the edge | `@Valid` + Bean Validation annotations on `BookRequest` |
| One error format | RFC 9457 **Problem Details**, `Content-Type: application/problem+json` |
| Stateless | Every request carries everything needed; no server session |
| Versioning when breaking changes come | `/api/v2/books` or a header; not needed yet |

## Request and response objects (DTOs)

`BookRequest` (input) and `Book` (output) are separate records. The API shape can differ from storage,
clients can't set the `id`, and validation rules live on the input:

```java
public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotBlank @Pattern(regexp = "\\d{13}", message = "must be 13 digits") String isbn,
        @NotNull @Positive @Digits(integer = 6, fraction = 2) BigDecimal price,
        @Min(1450) @Max(2100) int publishedYear) { }
```

Money is `BigDecimal` (never `double`, Session 1).

## Errors as Problem Details

[`ApiExceptionHandler`](../../src/main/java/jbdl/session08/web/ApiExceptionHandler.java) extends Spring's
`ResponseEntityExceptionHandler`, so all of Spring MVC's own errors (405, 415, unreadable JSON) come
back as Problem Details too, and adds two of its own. Real responses:

```json
GET /api/books/42  →  404
{"detail":"book 42 not found","instance":"/api/books/42","status":404,"title":"Book not found"}

POST /api/books with an invalid body  →  400
{"detail":"request body is invalid","instance":"/api/books","status":400,"title":"Validation failed",
 "errors":{"isbn":"must be 13 digits","price":"must be greater than 0",
           "publishedYear":"must be greater than or equal to 1450","title":"must not be blank"}}
```

**Lesson from building it:** the first version used a separate `@RestControllerAdvice` *and*
`spring.mvc.problemdetails.enabled=true`. Spring Boot then registered its own handler too, which
answered validation errors first with a generic `"title": "Bad Request"`; the test caught it. Extending
`ResponseEntityExceptionHandler` makes Boot back off and leaves one handler in charge.

## Layers

```
BookController (HTTP)  →  BookService (rules, in-memory store)  →  database later (Sessions 10-11)
```

## The code

| File | What it shows |
|---|---|
| [`BookController.java`](../../src/main/java/jbdl/session08/books/BookController.java) | All five methods, `ResponseEntity.created(location)`, `noContent()`, PATCH body record |
| [`BookRequest.java`](../../src/main/java/jbdl/session08/books/BookRequest.java), [`Book.java`](../../src/main/java/jbdl/session08/books/Book.java) | Input and output DTOs |
| [`BookService.java`](../../src/main/java/jbdl/session08/books/BookService.java) | Thread-safe in-memory store, PUT vs PATCH semantics |
| [`ApiExceptionHandler.java`](../../src/main/java/jbdl/session08/web/ApiExceptionHandler.java) | Problem Details for 404 and validation |
| [`BookApiTest.java`](../../src/test/java/jbdl/session08/BookApiTest.java) | 8 tests through the real DispatcherServlet with MockMvc |

## Interview questions

- **What makes an API RESTful?** Resources with URLs, standard methods and status codes, stateless requests, representations (JSON).
- **Why DTOs instead of exposing entities?** Control the contract, hide internal fields, validate input, decouple API from storage.
- **How do you report validation errors?** 400 with a structured body naming each field.
- **What is Problem Details?** RFC 9457's standard JSON error format: `type`, `title`, `status`, `detail`, `instance` + extensions.
- **How would you add pagination?** `?page=0&size=20` (Spring Data `Pageable`, Session 11) and return page metadata.
