package jbdl.session02.abstraction;

public class Developer extends Employee {

    public Developer(String name, long baseSalaryInRupees) {
        super(name, baseSalaryInRupees);
    }

    @Override
    protected long bonusInRupees() {
        return 5_000;
    }
}
