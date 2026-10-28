package jbdl.session01.oopbasics;

import java.util.Objects;

/**
 * Object identity vs equality. == asks "same object?"; equals asks "same value?". Object's default
 * equals is ==, so a value class overrides equals AND hashCode (equal objects must have equal hash
 * codes, or HashSet/HashMap can't find them). PointRecord below gets all of this generated.
 */
public final class Point {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /** Same value = same x and y. Steps: same object? same type? then compare the fields. */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        // "instanceof Point point" (Java 16+) checks the type AND creates the variable `point`.
        // Older form: if (!(other instanceof Point)) { return false; }  Point point = (Point) other;
        if (!(other instanceof Point point)) {
            return false;
        }
        return x == point.x && y == point.y;
    }

    /** Built from the same fields as equals, so equal points always have equal hash codes. */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point(" + x + ", " + y + ")";
    }

    /**
     * Since Java 16: a record is a final class with private final fields, a constructor, accessors
     * (x(), y()), equals, hashCode and toString, all generated from this one line.
     */
    public record PointRecord(int x, int y) {
    }
}
