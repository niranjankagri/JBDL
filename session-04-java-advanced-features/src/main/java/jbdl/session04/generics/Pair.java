package jbdl.session04.generics;

/**
 * A generic class: K and V are type parameters, filled in by the caller (Pair<String, Integer>).
 * The compiler checks the types, so no casts and no ClassCastException at run time.
 */
public record Pair<K, V>(K first, V second) {

    /** A generic method: its own type parameters, inferred from the arguments. */
    public static <A, B> Pair<B, A> swap(Pair<A, B> pair) {
        return new Pair<>(pair.second(), pair.first());
    }
}
