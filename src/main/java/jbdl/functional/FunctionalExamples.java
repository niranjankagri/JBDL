package jbdl.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The built-in functional interfaces from java.util.function, plus three ways to implement Greeting
 * (named class, anonymous class, lambda). Fields instead of main-only code so the tests can use them.
 */
public final class FunctionalExamples {

    private FunctionalExamples() {
    }

    // --- Three ways to implement the same interface ---

    public static final Greeting NAMED_CLASS = new FormalGreeting();

    public static final Greeting ANONYMOUS_CLASS = new Greeting() {
        @Override
        public String greet(String name) {
            return "Hello, " + name;
        }
    };

    public static final Greeting LAMBDA = name -> "Hi, " + name;

    // --- Function<T, R>: takes T, returns R ---

    public static final Function<Integer, Character> ASCII_TO_CHAR = code -> (char) code.intValue();
    public static final Function<String, Character> FIRST_LETTER = text -> text.charAt(0);
    public static final Function<Integer, Double> HALF = number -> number / 2.0;

    /** andThen runs HALF first, then triples: (10 / 2) * 3 = 15.0 */
    public static double halfThenTriple(int number) {
        return HALF.andThen(result -> result * 3).apply(number);
    }

    /** compose runs the triple first, then HALF: (5 * 3) / 2 = 7.5 */
    public static double tripleThenHalf(int number) {
        return HALF.compose((Integer input) -> input * 3).apply(number);
    }

    // --- Predicate<T>: a yes/no test, combinable with and / or / negate ---

    public static final Predicate<Integer> LESS_THAN_10 = number -> number < 10;
    public static final Predicate<Integer> MORE_THAN_20 = number -> number > 20;
    public static final Predicate<Integer> OUTSIDE_10_TO_20 = LESS_THAN_10.or(MORE_THAN_20);

    // --- Consumer<T>: takes T, returns nothing (here it adds to a list, so the effect is visible) ---

    /** Splits a full name with two chained Consumers: first name, then last name. */
    public static void splitName(String fullName, List<String> parts) {
        Consumer<String> addFirstName = name -> parts.add(name.split("\\s+")[0]);
        Consumer<String> addLastName = name -> {
            String[] words = name.split("\\s+");
            parts.add(words[words.length - 1]);
        };
        addFirstName.andThen(addLastName).accept(fullName);
    }

    public static void main(String[] args) {
        System.out.println(NAMED_CLASS.greet("Niranjan"));
        System.out.println(ANONYMOUS_CLASS.greet("Niranjan"));
        System.out.println(LAMBDA.greetLoudly("Niranjan"));
        System.out.println("ASCII 100 = " + ASCII_TO_CHAR.apply(100));
        System.out.println("First letter of Niranjan = " + FIRST_LETTER.apply("Niranjan"));
        System.out.println("10 halved then tripled = " + halfThenTriple(10));
        System.out.println("5 tripled then halved = " + tripleThenHalf(5));
        System.out.println("Function.identity() of 10 = " + Function.<Integer>identity().apply(10));
        System.out.println("6 outside 10..20? " + OUTSIDE_10_TO_20.test(6));
        System.out.println("16 outside 10..20? " + OUTSIDE_10_TO_20.test(16));
        System.out.println("6 not less than 10? " + LESS_THAN_10.negate().test(6));
        List<String> parts = new ArrayList<>();
        splitName("Niranjan Kumar", parts);
        System.out.println("Name parts: " + parts);
        System.out.println(MetroEngine.DRIVER_BASED.start() + " / " + MetroEngine.DRIVERLESS.start());
    }
}
