package jbdl.session02.abstraction;

import java.util.List;

/**
 * Interface: a contract ("can be paid"), no instance state. Since Java 8 it may have default and
 * static methods, since Java 9 private ones. Unrelated classes (an employee, an invoice) can both
 * implement it, and a class can implement many interfaces.
 */
public interface Payable {

    long monthlyPayInRupees();

    /** Default method: every implementation gets it, and may override it. */
    default long yearlyPayInRupees() {
        return monthlyPayInRupees() * 12;
    }

    /** Static method: belongs to the interface, called as Payable.totalMonthly(...). */
    static long totalMonthly(List<? extends Payable> payables) {
        long total = 0;
        for (Payable payable : payables) {
            total += payable.monthlyPayInRupees();
        }
        return total;
        // With streams (Session 4) this is one line:
        //   payables.stream().mapToLong(Payable::monthlyPayInRupees).sum()
    }
}
