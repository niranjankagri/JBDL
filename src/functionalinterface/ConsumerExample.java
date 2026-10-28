package functionalinterface;

import java.util.function.Consumer;

public class ConsumerExample {

    public static void main(String[] args) {
        Consumer<Integer> c = (i) -> System.out.println(i);
        c.accept(10);

        Consumer<String> printFirstName = (name) -> {
            System.out.println(name.split("\\s")[0]);
        };
        printFirstName.accept("Niranjan Kumar");

        Consumer<String> printLastName = (name) -> {
            System.out.println(name.split("\\s")[1]);
        };
        printLastName.accept("Niranjan Kumar");

        printFirstName.andThen(printLastName).accept("Niranjan Kumar");
    }
}
