package jbdl.session02.propagation;

/** The service layer's own checked exception; wraps whatever went wrong below. */
public class OrderLookupException extends Exception {

    private static final long serialVersionUID = 1L;

    public OrderLookupException(String message, Throwable cause) {
        super(message, cause);
    }
}
