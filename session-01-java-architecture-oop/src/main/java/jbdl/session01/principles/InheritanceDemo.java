package jbdl.session01.principles;

/** Was Main.java in 2024: polymorphism and reference assignment. */
public final class InheritanceDemo {

    private InheritanceDemo() {
    }

    /** Base class, subclass through a base-class variable, a shared reference, and the factory. */
    public static void main(String[] args) {
        Car car = new Car("Hyundai", "Red", "Santro");
        System.out.println(car.getInfo());

        // The variable type is Car, but the SuperCar override of getInfo() runs (dynamic dispatch)
        Car mcLaren = new SuperCar("McLaren", "Black", "F1", 2005, 40);
        System.out.println(mcLaren.getInfo());

        SuperCar bugatti = new SuperCar("Bugatti", "Black", "Chiron", 2006, 30);
        // Assigning a reference copies the reference, not the object: both names point to one car
        SuperCar sameCar = bugatti;
        System.out.println(sameCar.getFirstRaceYear() + " (same object: " + (sameCar == bugatti) + ")");

        System.out.println(CarFactory.create(CarFactory.Kind.RACING, "Ferrari", "Red", "SF-25").getInfo());
    }
}
