package jbdl.session01.principles;

import java.util.Locale;

/**
 * Abstraction: callers work with "a shape that has an area" and don't care how each shape computes
 * it. An abstract class can't be instantiated; each concrete subclass must implement area().
 */
public abstract class Shape {

    private final String name;

    /** protected: only subclasses call it (via super(name)); an abstract class can't be created directly. */
    protected Shape(String name) {
        this.name = name;
    }

    /** Each subclass knows its own formula. */
    public abstract double area();

    /** Shared behaviour, written once for every shape (a "template" that calls the abstract method). */
    public String describe() {
        return String.format(Locale.ROOT, "%s with area %.2f", name, area());
    }

    public String getName() {
        return name;
    }
}
