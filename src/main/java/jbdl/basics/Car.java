package jbdl.basics;

/**
 * A car with seven properties. The 2024 version had one seven-argument constructor, which is easy to
 * call with the arguments in the wrong order; the Builder names every value instead (builder pattern).
 */
public final class Car {

    private final String brand;
    private final String color;
    private final int year;
    private final int topSpeed;
    private final String name;
    private final String fuelType;
    private final boolean automatic;

    private Car(Builder builder) {
        this.brand = builder.brand;
        this.color = builder.color;
        this.year = builder.year;
        this.topSpeed = builder.topSpeed;
        this.name = builder.name;
        this.fuelType = builder.fuelType;
        this.automatic = builder.automatic;
    }

    public static Builder builder(String brand, String name) {
        return new Builder(brand, name);
    }

    public String getInfo() {
        return "The car uses " + fuelType + " and is " + (automatic ? "automatic" : "manual") + ".";
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public int getYear() {
        return year;
    }

    public int getTopSpeed() {
        return topSpeed;
    }

    public String getName() {
        return name;
    }

    public String getFuelType() {
        return fuelType;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    /** Brand and name are required; everything else has a default. */
    public static final class Builder {
        private final String brand;
        private final String name;
        private String color = "White";
        private int year;
        private int topSpeed;
        private String fuelType = "Petrol";
        private boolean automatic;

        private Builder(String brand, String name) {
            this.brand = brand;
            this.name = name;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder year(int year) {
            this.year = year;
            return this;
        }

        public Builder topSpeed(int topSpeed) {
            this.topSpeed = topSpeed;
            return this;
        }

        public Builder fuelType(String fuelType) {
            this.fuelType = fuelType;
            return this;
        }

        public Builder automatic(boolean automatic) {
            this.automatic = automatic;
            return this;
        }

        public Car build() {
            return new Car(this);
        }
    }
}
