package jbdl.session01.principles;

/**
 * Inheritance: a Square IS-A Rectangle with equal sides, so it reuses Rectangle's area() unchanged.
 * Safe here because both are immutable; a mutable Rectangle with setWidth/setHeight would break a
 * Square (the classic Liskov substitution example).
 */
public class Square extends Rectangle {

    public Square(double side) {
        super("Square", side, side);
    }
}
