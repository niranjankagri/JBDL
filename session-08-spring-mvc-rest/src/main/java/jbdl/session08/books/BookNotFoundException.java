package jbdl.session08.books;

/** Thrown by the service; ApiExceptionHandler turns it into a 404 response. */
public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BookNotFoundException(long id) {
        super("book " + id + " not found");
    }
}
