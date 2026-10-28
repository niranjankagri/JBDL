package jbdl.session03.functional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Lambda syntax from longest to shortest, the four kinds of method reference, and what a lambda may
 * capture from the surrounding code.
 */
public final class LambdaSyntax {

    private LambdaSyntax() {
    }

    // The same comparator written four ways: each step drops something the compiler can infer

    /** 1. Anonymous class: what we wrote before Java 8. */
    public static final Comparator<String> BY_LENGTH_ANONYMOUS = new Comparator<>() {
        @Override
        public int compare(String a, String b) {
            return Integer.compare(a.length(), b.length());
        }
    };

    /** 2. Lambda with parameter types and a block body. */
    public static final Comparator<String> BY_LENGTH_BLOCK = (String a, String b) -> {
        return Integer.compare(a.length(), b.length());
    };

    /** 3. Types inferred, expression body: no braces, no return. */
    public static final Comparator<String> BY_LENGTH_EXPRESSION = (a, b) -> Integer.compare(a.length(), b.length());

    /** 4. A ready-made comparator built from a method reference. */
    public static final Comparator<String> BY_LENGTH_REFERENCE = Comparator.comparingInt(String::length);

    // The four kinds of method reference

    /** Static method: Integer::parseInt is s -> Integer.parseInt(s). */
    public static final Function<String, Integer> STATIC_REFERENCE = Integer::parseInt;

    /** Instance method of a particular object: prefix::concat is s -> prefix.concat(s). */
    public static Function<String, String> boundReference(String prefix) {
        return prefix::concat;
    }

    /** Instance method of an arbitrary object: String::toUpperCase is s -> s.toUpperCase(). */
    public static final Function<String, String> UNBOUND_REFERENCE = String::toUpperCase;

    /** Constructor: ArrayList::new is () -> new ArrayList<>(). */
    public static final Supplier<List<String>> CONSTRUCTOR_REFERENCE = ArrayList::new;

    /** Two parameters: the first becomes the object, the second the argument: (s, n) -> s.repeat(n). */
    public static final BiFunction<String, Integer, String> REPEAT = String::repeat;

    /**
     * A lambda can read local variables only if they are effectively final (never reassigned).
     * Here `tax` is captured; reassigning it anywhere in this method would be a compile error.
     */
    public static Function<Integer, Integer> addTax(int percent) {
        int tax = percent; // effectively final: assigned once
        return price -> price + price * tax / 100;
    }
}
