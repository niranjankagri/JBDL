package functionalinterface;

import java.util.function.Function;

public class FunctionExample {

    public static void main(String[] args) {
        Function<Integer, Character> intToChar = asciiVal -> (char) asciiVal.intValue();;
        System.out.println(intToChar.apply(100));

        Function<String, Character> strToChar = str -> str.charAt(0);
        System.out.println(strToChar.apply("Niranjan"));

        Function<Integer, Double> half = a -> a/2.0;
        System.out.println(half.andThen(a -> a * 3).apply(10));

        half = half.andThen(a -> 3 * a); // Execute andThen after
        System.out.println(half.apply(10));

        half = half.compose(a -> 3 * a); // Execute compose first
        System.out.println(half.apply(5));
        System.out.println(half.compose(a -> 3 * (Integer) a).apply(5));

        Function<Integer, Integer> i =  Function.identity();
        System.out.println(i.apply(10));



    }
}
