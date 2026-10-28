package functionalinterface.task;

public interface MetroEngine {

    void start();

}

class Task {

    public static void main(String[] args) {
        MetroEngine driverBasedEngine = () -> {
            System.out.println("Drive with Driver");
        };
        driverBasedEngine.start();

        MetroEngine driverLessEngine = () -> {
            System.out.println("Drive without Driver");
        };
        driverLessEngine.start();

    }
}
