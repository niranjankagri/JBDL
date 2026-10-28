package jbdl.session02.abstraction;

/**
 * Abstract class: a partly built base class. It can have state (fields), constructors and finished
 * methods, plus abstract methods the subclasses must write. A class can extend only ONE of them.
 * Use it when the subclasses share state and code, not only a contract.
 */
public abstract class Employee implements Payable {

    private final String name;
    private final long baseSalaryInRupees;

    protected Employee(String name, long baseSalaryInRupees) {
        this.name = name;
        this.baseSalaryInRupees = baseSalaryInRupees;
    }

    /** Template method: the steps are fixed here; the bonus step differs per subclass. */
    @Override
    public final long monthlyPayInRupees() {
        return baseSalaryInRupees / 12 + bonusInRupees();
    }

    /** The step each kind of employee fills in. */
    protected abstract long bonusInRupees();

    public String getName() {
        return name;
    }

    protected long getBaseSalaryInRupees() {
        return baseSalaryInRupees;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + name + ")";
    }
}
