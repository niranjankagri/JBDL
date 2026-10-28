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

    /** All books in id order; author (case-insensitive) filters when given, null means all. */
    public List<Book> findAll(String author) {
        return books.values().stream()
                .filter(book -> author == null || book.author().equalsIgnoreCase(author))
                .sorted(Comparator.comparingLong(Book::id))
                .toList();
    }

    /** The book, or BookNotFoundException (which the API turns into a 404). */
    public Book findById(long id) {
        Book book = books.get(id);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return book;
    }

    /** Assigns the next id (thread-safe AtomicLong) and stores the new book. */
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
        // compute does the read-check-write as one atomic step on the map entry
        return books.compute(id, (key, existing) -> {
            if (existing == null) {
                throw new BookNotFoundException(id);
            }
            return existing.withPrice(price);
        });
    }

    /** Removes the book; deleting an unknown id is a 404, also on a repeated DELETE. */
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

    /** Maps the client's DTO to the stored record, adding the server-owned id. */
    private static Book toBook(long id, BookRequest request) {
        return new Book(id, request.title(), request.author(), request.isbn(), request.price(), request.publishedYear());
    }
}
