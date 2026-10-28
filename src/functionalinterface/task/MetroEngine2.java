package functionalinterface.task;

public interface MetroEngine2 {

    void start();

}

class Test {

    public static void main(String[] args) {
        MetroEngine2 driverBasedEngine = () -> System.out.println("Drive with Driver");
        driverBasedEngine.start();

        MetroEngine driverLessEngine = () ->  System.out.println("Drive without Driver");
        driverLessEngine.start();

    }
}
