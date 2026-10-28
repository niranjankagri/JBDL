package jbdl.session02.abstraction;

/** A concrete Employee: fills in the one step the abstract class left open (the bonus). */
public class Developer extends Employee {

    public Developer(String name, long baseSalaryInRupees) {
        super(name, baseSalaryInRupees);
    }

    /** A flat monthly bonus for every developer. */
    @Override
    protected long bonusInRupees() {
        return 5_000;
    }
}
