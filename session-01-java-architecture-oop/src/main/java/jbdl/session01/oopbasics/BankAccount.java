package jbdl.session01.oopbasics;

/**
 * A class is the blueprint; every `new BankAccount(...)` is an object with its own state.
 *
 * Shows: private fields with public methods (the only way to change the balance), overloaded
 * constructors chained with this(...), `this` to tell a field from a parameter, a static field
 * shared by all objects vs instance fields per object, and validation that keeps the object valid.
 */
public class BankAccount {

    // static: ONE counter for the whole class, shared by every account
    private static int accountsOpened;

    // instance fields: every account has its own copy
    private final String accountNumber;
    private final String owner;
    private long balanceInPaise; // money as whole paise, never double (0.1 + 0.2 != 0.3)

    public BankAccount(String owner) {
        this(owner, 0); // constructor chaining: reuse the other constructor
    }

    public BankAccount(String owner, long openingBalanceInPaise) {
        if (owner == null || owner.isBlank()) {
            throw new IllegalArgumentException("owner is required");
        }
        if (openingBalanceInPaise < 0) {
            throw new IllegalArgumentException("opening balance cannot be negative");
        }
        accountsOpened++;
        this.accountNumber = "ACC-" + accountsOpened;
        this.owner = owner; // `this.owner` is the field, `owner` the parameter
        this.balanceInPaise = openingBalanceInPaise;
    }

    public void deposit(long amountInPaise) {
        requirePositive(amountInPaise);
        balanceInPaise += amountInPaise;
    }

    public void withdraw(long amountInPaise) {
        requirePositive(amountInPaise);
        if (amountInPaise > balanceInPaise) {
            throw new IllegalStateException("insufficient funds: balance " + balanceInPaise + ", asked " + amountInPaise);
        }
        balanceInPaise -= amountInPaise;
    }

    /** private: a helper for this class only, not part of what other classes can call. */
    private static void requirePositive(long amountInPaise) {
        if (amountInPaise <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public long getBalanceInPaise() {
        return balanceInPaise;
    }

    /** static method: belongs to the class, called as BankAccount.accountsOpened(), no object needed. */
    public static int accountsOpened() {
        return accountsOpened;
    }

    @Override
    public String toString() {
        return accountNumber + " (" + owner + "): " + balanceInPaise / 100 + "." + String.format("%02d", balanceInPaise % 100);
    }

    public static void main(String[] args) {
        BankAccount first = new BankAccount("Niranjan", 50_000);
        BankAccount second = new BankAccount("Asha");
        first.deposit(2_550);
        first.withdraw(10_000);
        second.deposit(100);
        System.out.println(first);
        System.out.println(second);
        System.out.println("Accounts opened: " + BankAccount.accountsOpened());
        try {
            second.withdraw(1_000);
        } catch (IllegalStateException e) {
            System.out.println("Refused: " + e.getMessage());
        }
    }
}
