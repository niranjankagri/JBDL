package jbdl.session02.exceptions;

/** Custom UNCHECKED exception: a caller bug (negative amount), not something to recover from. */
public class InvalidAmountException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /** The message names the bad value, which makes the caller's bug easy to find in a log. */
    public InvalidAmountException(long amountInPaise) {
        super("amount must be positive, got " + amountInPaise);
    }
}
