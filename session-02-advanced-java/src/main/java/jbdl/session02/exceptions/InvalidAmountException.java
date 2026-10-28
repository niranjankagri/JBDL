package jbdl.session02.exceptions;

/** Custom UNCHECKED exception: a caller bug (negative amount), not something to recover from. */
public class InvalidAmountException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public InvalidAmountException(long amountInPaise) {
        super("amount must be positive, got " + amountInPaise);
    }
}
