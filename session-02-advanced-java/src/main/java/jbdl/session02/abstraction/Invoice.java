package jbdl.session02.abstraction;

/** Not an Employee at all, yet Payable: interfaces connect unrelated classes. */
public record Invoice(String supplier, long amountInRupees) implements Payable {

    @Override
    public long monthlyPayInRupees() {
        return amountInRupees;
    }

    @Override
    public long yearlyPayInRupees() {
        return amountInRupees; // a one-off invoice: overrides the default
    }
}
