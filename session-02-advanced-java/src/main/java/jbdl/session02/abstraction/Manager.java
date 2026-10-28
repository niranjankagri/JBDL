package jbdl.session02.abstraction;

/** Extends one class AND implements an extra interface: single inheritance, many interfaces. */
public class Manager extends Employee implements Taxable {

    private final int teamSize;

    public Manager(String name, long baseSalaryInRupees, int teamSize) {
        super(name, baseSalaryInRupees);
        this.teamSize = teamSize;
    }

    @Override
    protected long bonusInRupees() {
        return 1_000L * teamSize;
    }

    @Override
    public long taxableIncomeInRupees() {
        return getBaseSalaryInRupees();
    }
}
