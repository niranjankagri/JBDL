package functionalinterface;

import java.util.function.Predicate;

public class PredicateExample {

    public static void main(String[] args) {
        Predicate<Integer> lessThan10 = (x) -> x < 10;
        System.out.println(lessThan10.test(5));

        Predicate<Integer> moreThan20 = (x) -> x > 20;
        System.out.println(moreThan20.test(15));

        System.out.println(lessThan10.or(moreThan20).test(6));
        System.out.println(lessThan10.and(moreThan20).test(16));
        System.out.println(lessThan10.negate().test(6));
    }

}
