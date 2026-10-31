package jbdl.session08.books;

import java.math.BigDecimal;

/** The resource the API exposes. Immutable: a change creates a new Book with the same id. */
public record Book(long id, String title, String author, String isbn, BigDecimal price, int publishedYear) {

    public Book withPrice(BigDecimal newPrice) {
        return new Book(id, title, author, isbn, newPrice, publishedYear);
    }
}
