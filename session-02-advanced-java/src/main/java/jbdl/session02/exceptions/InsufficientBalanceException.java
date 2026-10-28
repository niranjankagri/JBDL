package jbdl.session02.exceptions;

/**
 * Custom CHECKED exception: a business outcome the caller is expected to handle (show a message,
 * offer a smaller amount), so the compiler makes sure they do.
 */
public class InsufficientBalanceException extends Exception {

    private static final long serialVersionUID = 1L;

    private final long shortByInPaise;

    public InsufficientBalanceException(long shortByInPaise) {
        super("insufficient balance: short by " + shortByInPaise + " paise");
        this.shortByInPaise = shortByInPaise;
    }

    public long getShortByInPaise() {
        return shortByInPaise;
    }
}
