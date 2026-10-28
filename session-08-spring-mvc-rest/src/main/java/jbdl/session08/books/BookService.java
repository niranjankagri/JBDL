package jbdl.session08.books;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Business logic, kept out of the controller. @Service makes it a Spring bean (one shared instance),
 * injected into the controllers. Data lives in memory for now; Sessions 10-11 move it to a database.
 * A web server handles requests on many threads, hence ConcurrentHashMap and AtomicLong (Session 5).
 */
@Service
public class BookService {

    private final Map<Long, Book> books = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<Book> findAll(String author) {
        return books.values().stream()
                .filter(book -> author == null || book.author().equalsIgnoreCase(author))
                .sorted(Comparator.comparingLong(Book::id))
                .toList();
    }

    public Book findById(long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    public Book create(BookRequest request) {
        long id = nextId.getAndIncrement();
        Book book = toBook(id, request);
        books.put(id, book);
        return book;
    }

    /** PUT semantics: replace the whole resource. */
    public Book replace(long id, BookRequest request) {
        findById(id);
        Book book = toBook(id, request);
        books.put(id, book);
        return book;
    }

    /** PATCH semantics: change only the given field. */
    public Book changePrice(long id, BigDecimal price) {
        return books.compute(id, (key, existing) -> {
            if (existing == null) {
                throw new BookNotFoundException(id);
            }
            return existing.withPrice(price);
        });
    }

    public void delete(long id) {
        if (books.remove(id) == null) {
            throw new BookNotFoundException(id);
        }
    }

    /** For tests: start every test from an empty store. */
    public void clear() {
        books.clear();
        nextId.set(1);
    }

    private static Book toBook(long id, BookRequest request) {
        return new Book(id, request.title(), request.author(), request.isbn(), request.price(), request.publishedYear());
    }
}
