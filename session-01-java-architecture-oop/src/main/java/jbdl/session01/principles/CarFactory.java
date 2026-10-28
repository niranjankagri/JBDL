package jbdl.session01.principles;

import java.time.Year;

/** Factory pattern: callers ask for a kind of car and get a Car back, without knowing the subclass. */
public final class CarFactory {

    /** What the caller asks for; the factory decides which class that means. */
    public enum Kind { CITY, RACING }

    private CarFactory() {
    }

    /** Returns the declared type Car, so callers never depend on the concrete subclass. */
    public static Car create(Kind kind, String brand, String color, String name) {
        // A switch EXPRESSION (Java 14+): each case gives a value with ->, no break needed.
        // The classic form does the same:
        //   if (kind == Kind.CITY) { return new Car(...); } else { return new SuperCar(...); }
        return switch (kind) {
            case CITY -> new Car(brand, color, name);
            // A new racing car has no record yet: first race this year, no trophies
            case RACING -> new SuperCar(brand, color, name, Year.now().getValue(), 0);
        };
    }
}
