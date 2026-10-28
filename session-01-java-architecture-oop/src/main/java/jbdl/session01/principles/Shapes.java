package jbdl.session01.principles;

import java.util.List;
import java.util.Locale;

/** Polymorphism in both forms: one call site for many shapes (overriding), and overloaded methods. */
public final class Shapes {

    private Shapes() {
    }

    /**
     * Run-time polymorphism: shape.area() runs Circle's, Rectangle's or Square's version depending on
     * the actual object, decided by the JVM while the program runs (dynamic dispatch).
     */
    public static double totalArea(List<? extends Shape> shapes) {
        double total = 0;
        for (Shape shape : shapes) {
            total += shape.area();
        }
        return total;
    }

    // Compile-time polymorphism: same name, different parameter lists; the compiler picks one.

    public static String label(Shape shape) {
        return "shape: " + shape.getName();
    }

    public static String label(Shape shape, int decimals) {
        return String.format(Locale.ROOT, "shape: %s (%." + decimals + "f)", shape.getName(), shape.area());
    }

    public static String label(Circle circle) {
        return "round shape: " + circle.getName();
    }

    /** Prints each shape, the total, and which overload the compiler picked for each call. */
    public static void main(String[] args) {
        List<Shape> shapes = List.of(new Circle(1), new Rectangle(2, 3), new Square(2));
        shapes.forEach(shape -> System.out.println(shape.describe()));
        System.out.printf(Locale.ROOT, "Total area: %.2f%n", totalArea(shapes));

        Shape someShape = new Circle(1);
        // Overloads are chosen by the DECLARED type (Shape), overrides by the ACTUAL object (Circle)
        System.out.println(label(someShape));
        System.out.println(label(new Circle(1)));
        System.out.println(label(someShape, 3));
    }
}
