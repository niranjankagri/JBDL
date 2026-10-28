package jbdl.session01.principles;

import java.time.Year;

/** Factory pattern: callers ask for a kind of car and get a Car back, without knowing the subclass. */
public final class CarFactory {

    public enum Kind { CITY, RACING }

    private CarFactory() {
    }

    public static Car create(Kind kind, String brand, String color, String name) {
        return switch (kind) {
            case CITY -> new Car(brand, color, name);
            // A new racing car has no record yet: first race this year, no trophies
            case RACING -> new SuperCar(brand, color, name, Year.now().getValue(), 0);
        };
    }
}
