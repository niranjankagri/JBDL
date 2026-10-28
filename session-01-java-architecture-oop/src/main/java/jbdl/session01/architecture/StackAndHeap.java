package jbdl.session01.architecture;

/**
 * Java always passes arguments by value. For a primitive the value is the number itself; for an
 * object it is the reference (the address of the object on the heap). Each method call gets its own
 * stack frame holding these copies, so:
 * - changing a primitive parameter never changes the caller's variable,
 * - changing the object a reference points to IS visible to the caller (same object on the heap),
 * - pointing the parameter at a new object is NOT visible to the caller (only the copy changed).
 */
public final class StackAndHeap {

    /** A tiny mutable holder, so the examples can change an object's state. */
    public static final class Box {
        private int value;

        public Box(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private StackAndHeap() {
    }

    /** Changes only this method's copy of the number. */
    public static void addTen(int number) {
        number += 10; // only the copy changes
    }

    /** Changes the object both the caller and this method point to. */
    public static void addTen(Box box) {
        box.setValue(box.getValue() + 10);
    }

    /** Points this method's copy of the reference at a new object; the caller keeps the old one. */
    public static void replace(Box box) {
        box = new Box(999); // only the copy now points elsewhere
        box.setValue(1000);
    }

    /** Runs the three cases and prints what the caller sees afterwards. */
    public static void main(String[] args) {
        int number = 5;
        addTen(number);
        System.out.println("Primitive after addTen:   " + number + "  (unchanged)");

        Box box = new Box(5);
        addTen(box);
        System.out.println("Object after addTen:      " + box.getValue() + " (changed: same object)");

        replace(box);
        System.out.println("Object after replace:     " + box.getValue() + " (unchanged: new object was local)");
    }
}
