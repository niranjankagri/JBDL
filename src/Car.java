public class Car {
    public String brand;
    public String color;
    public int year;
    public int topSpeed;
    public String name;
    public String fuelType;
    public boolean isAutomatic;

    public Car(String brand, String color, int year, int topSpeed, String name, String fuelType, boolean isAutomatic) {
        this.brand = brand;
        this.color = color;
        this.year = year;
        this.topSpeed = topSpeed;
        this.name = name;
        this.fuelType = fuelType;
        this.isAutomatic = isAutomatic;
    }

    public String getInfo() {
        return "The car uses " + this.fuelType + " and is " + (this.isAutomatic ? "Automatic" : "manual") + ".";
    }
}
