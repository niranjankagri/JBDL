package jbdl.session08.books;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * The REST API. @RestController = @Controller + @ResponseBody: return values are written to the
 * response body as JSON by Jackson (no view). One method per HTTP method on the /api/books resource.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService service;

    /** Constructor injection: Spring passes in the BookService bean. */
    public BookController(BookService service) {
        this.service = service;
    }

    /** GET /api/books?author=... : list (optionally filtered), 200. */
    @GetMapping
    public List<Book> list(@RequestParam(required = false) String author) {
        return service.findAll(author);
    }

    /** GET /api/books/{id} : one book, 200; or 404 via the exception handler. */
    @GetMapping("/{id}")
    public Book get(@PathVariable long id) {
        return service.findById(id);
    }

    /** POST /api/books : create, 201 Created + Location header of the new resource. */
    @PostMapping
    public ResponseEntity<Book> create(@Valid @RequestBody BookRequest request) {
        Book created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(created.id()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    /** PUT /api/books/{id} : replace the whole book, 200. Repeating it changes nothing more (idempotent). */
    @PutMapping("/{id}")
    public Book replace(@PathVariable long id, @Valid @RequestBody BookRequest request) {
        return service.replace(id, request);
    }

    /** PATCH /api/books/{id} : change part of the book (the price), 200. */
    @PatchMapping("/{id}")
    public Book changePrice(@PathVariable long id, @Valid @RequestBody PriceChange change) {
        return service.changePrice(id, change.price());
    }

    /** DELETE /api/books/{id} : 204 No Content; a second DELETE gets 404. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** Body of the PATCH request. */
    public record PriceChange(@NotNull @Positive BigDecimal price) {
    }
}
