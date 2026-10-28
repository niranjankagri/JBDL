package jbdl.session04.generics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Bounded type parameters, wildcards (PECS) and type erasure. */
public final class GenericMethods {

    private GenericMethods() {
    }

    /**
     * Bounded type: T must be Comparable to itself, so compareTo is available. Works for Integer,
     * String, LocalDate... and is checked at compile time.
     */
    public static <T extends Comparable<? super T>> T max(Collection<? extends T> items) {
        if (items.isEmpty()) {
            throw new IllegalArgumentException("no items");
        }
        T best = null;
        for (T item : items) {
            if (best == null || item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    /**
     * Upper-bounded wildcard (? extends Number): read-only use, accepts List<Integer>, List<Double>...
     * A plain List<Number> parameter would NOT accept a List<Integer> (generics are invariant).
     */
    public static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    /**
     * PECS, "Producer Extends, Consumer Super": source produces T (extends), target consumes T
     * (super). Collections.copy has the same signature.
     */
    public static <T> void copy(List<? extends T> source, List<? super T> target) {
        target.addAll(source);
    }

    /** Type erasure: at run time both lists are just ArrayList; the <String>/<Integer> is gone. */
    public static boolean sameRuntimeClass() {
        List<String> strings = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();
        return strings.getClass() == numbers.getClass();
    }
}
