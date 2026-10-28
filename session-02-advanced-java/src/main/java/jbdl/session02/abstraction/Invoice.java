package jbdl.session02.abstraction;

/** Not an Employee at all, yet Payable: interfaces connect unrelated classes. */
public record Invoice(String supplier, long amountInRupees) implements Payable {

    /** The whole invoice amount is paid in the month it arrives. */
    @Override
    public long monthlyPayInRupees() {
        return amountInRupees;
    }

    @Override
    public long yearlyPayInRupees() {
        return amountInRupees; // a one-off invoice: overrides the default
    }
}
