package jbdl.session02.exceptions;

/** Uses both custom exceptions: `throws` declares the checked one, `throw` raises either. */
public class Wallet {

    private long balanceInPaise;

    /** Starts with the given balance (in paise, never double for money). */
    public Wallet(long balanceInPaise) {
        this.balanceInPaise = balanceInPaise;
    }

    /** `throws` in the signature: part of the method's contract, checked by the compiler. */
    public void pay(long amountInPaise) throws InsufficientBalanceException {
        if (amountInPaise <= 0) {
            throw new InvalidAmountException(amountInPaise); // unchecked: no `throws` needed
        }
        if (amountInPaise > balanceInPaise) {
            throw new InsufficientBalanceException(amountInPaise - balanceInPaise);
        }
        balanceInPaise -= amountInPaise;
    }

    public long getBalanceInPaise() {
        return balanceInPaise;
    }
}
