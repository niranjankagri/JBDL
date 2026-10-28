package jbdl.session05.sync;

/**
 * Transfers between two accounts need BOTH locks. If thread 1 locks A then B while thread 2 locks B
 * then A, each waits for the other forever: a deadlock. The fix: always lock in one global order
 * (here by account id), whatever the direction of the transfer.
 */
public final class Accounts {

    private Accounts() {
    }

    /** Its monitor (synchronized on the object) guards the balance. */
    public static final class Account {
        private final int id;
        private long balance;

        public Account(int id, long balance) {
            this.id = id;
            this.balance = balance;
        }

        public int getId() {
            return id;
        }

        public synchronized long getBalance() {
            return balance;
        }
    }

    /** Moves money atomically: both accounts are locked, always the lower id first. */
    public static void transfer(Account from, Account to, long amount) {
        Account first = from.id < to.id ? from : to;   // lock ordering prevents deadlock
        Account second = from.id < to.id ? to : from;
        synchronized (first) {
            synchronized (second) {
                if (from.balance < amount) {
                    throw new IllegalStateException("insufficient funds in account " + from.id);
                }
                from.balance -= amount;
                to.balance += amount;
            }
        }
    }
}
