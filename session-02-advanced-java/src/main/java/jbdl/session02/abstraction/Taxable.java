package jbdl.session02.abstraction;

/** A second, independent contract: a class can implement it next to Payable. */
public interface Taxable {

    /** Every interface field is implicitly public static final: a constant, never state. */
    long TAX_FREE_LIMIT_IN_RUPEES = 300_000;

    long taxableIncomeInRupees();

    default long taxInRupees() {
        return Math.max(0, taxableIncomeInRupees() - TAX_FREE_LIMIT_IN_RUPEES) * tenPercent() / 100;
    }

    /** Private interface method (Java 9+): a helper for the default methods only. */
    private int tenPercent() {
        return 10;
    }
}
