package jbdl.session01.architecture;

/**
 * The three quiz questions from the 2024 Quiz class, as methods that return their answer so the tests
 * can check it. Each one is a classic Java interview trap.
 */
public final class JavaQuiz {

    private JavaQuiz() {
    }

    /** A byte holds -128..127, so 127 + 1 cast back to byte wraps around to -128. */
    public static byte addOneAsByte(byte value) {
        return (byte) (value + 1);
    }

    /**
     * Compares two boxed Integers with == (reference equality). Autoboxing reuses cached objects for
     * -128..127, so small values look "equal" and larger ones do not. Use equals() for values.
     */
    public static boolean sameReference(int first, int second) {
        // valueOf is what autoboxing (Integer a = first) calls behind the scenes
        Integer a = Integer.valueOf(first);
        Integer b = Integer.valueOf(second);
        return a == b;
    }

    /**
     * a++ > 3 compares the OLD value, then increments. With a = 3 the left side is false, a is now 4,
     * so the right side a == 4 is true and the answer is "GFG".
     */
    public static String postIncrementQuiz(int start) {
        int a = start;
        if (a++ > 3 || a == 4) {
            return "GFG";
        }
        return "Hi";
    }

    public static void main(String[] args) {
        System.out.println("(byte) (127 + 1) = " + addOneAsByte((byte) 127));
        System.out.println("Integer 1 == Integer 1: " + sameReference(1, 1));
        System.out.println("Integer 1000 == Integer 1000: " + sameReference(1000, 1000));
        System.out.println("Post-increment quiz with a = 3: " + postIncrementQuiz(3));
    }
}
