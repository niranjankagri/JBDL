package jbdl.inheritance;

/** Base class for the inheritance example; SuperCar extends it. */
public class Car {

    private final String brand;
    private final String color;
    private final String name;

    // Overloaded constructors: a car without a model name gets "unknown model"
    public Car(String brand, String color) {
        this(brand, color, "unknown model");
    }

    public Car(String brand, String color, String name) {
        this.brand = brand;
        this.color = color;
        this.name = name;
    }

    public String getInfo() {
        return "The car is " + brand + "'s " + name;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
