# Topic 2: Model, View and Controller

MVC splits a web page into three responsibilities:

| Part | Job | In this module |
|---|---|---|
| **Model** | The data to show | `Model` with attributes `books` and `author` |
| **View** | How it looks | [`templates/books.html`](../../src/main/resources/templates/books.html) (Thymeleaf) |
| **Controller** | Handles the request: asks the service for data, fills the model, picks the view | [`BookPageController`](../../src/main/java/jbdl/session08/web/BookPageController.java) |

```java
@Controller
public class BookPageController {
    @GetMapping("/books")
    public String books(@RequestParam(required = false) String author, Model model) {
        model.addAttribute("books", service.findAll(author));   // MODEL
        model.addAttribute("author", author);
        return "books";                                         // VIEW name → templates/books.html
    }
}
```

```html
<h1 th:text="${author} ? 'Books by ' + ${author} : 'All books'">All books</h1>
<tr th:each="book : ${books}">
    <td th:text="${book.title}">Title</td>
```

The controller never writes HTML; the template never fetches data. Each can change without the other.

## MVC for a REST API

A REST API is still MVC, with JSON as the "view": [`BookController`](../../src/main/java/jbdl/session08/books/BookController.java)
returns `Book` objects (the model) and Jackson renders them. Same layering, same rules:

```
Controller   HTTP in/out only: map URLs, read parameters, validate, pick a status code
Service      business rules (BookService)
Repository   data access (Sessions 10-11)
```

Keeping logic out of controllers is what makes it testable and reusable (the HTML page and the REST
API share `BookService`).

## Tests

[`BookPageTest`](../../src/test/java/jbdl/session08/BookPageTest.java) checks each part separately:

- controller: `view().name("books")`, `model().attribute("books", hasSize(1))`
- view: the rendered HTML contains `Books by Martin Fowler` and `<td>Refactoring</td>`

## Interview questions

- **What does each part of MVC do?** Model = data, View = presentation, Controller = request handling.
- **What is a `ViewResolver`?** It maps a view name to a template (`books` → `classpath:/templates/books.html`).
- **`Model` vs `ModelAndView`?** A parameter you fill vs a return value with both the view name and the data.
- **Where does business logic go?** In services, not controllers.
- **Server-side rendering vs SPA?** HTML built on the server (Thymeleaf) vs a JavaScript app calling a JSON API.
