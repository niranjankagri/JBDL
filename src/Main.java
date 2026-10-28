import inheritance.Car;
import inheritance.SuperCar;

public class Main {
    public static void main(String[] args) {
//        Car car = new Car(
//                "Hyundai",
//                "Red",
//                2000,
//                110,
//                "Santro",
//                "Petrol",
//                false
//        );
        Car car = new Car(
                "Hyundai",
                "Red",
                "Santro"
        );
        System.out.println(car.getInfo());

        SuperCar superCar = new SuperCar("McLaren", "Black", "F1", 2005, 40);
        System.out.println(superCar.getInfo());

        SuperCar buggati =  new SuperCar("Buggati", "Black", "XYZ", 2006, 30);
        System.out.println(buggati.getInfo());

        SuperCar xyz = buggati;
        System.out.println(xyz.firstRaceYear);
    }
}
