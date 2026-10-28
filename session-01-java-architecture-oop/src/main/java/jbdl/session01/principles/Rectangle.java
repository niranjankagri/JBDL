package jbdl.session01.principles;

/** A Shape with width and height. Square extends it and reuses area() unchanged. */
public class Rectangle extends Shape {

    private final double width;
    private final double height;

    public Rectangle(double width, double height) {
        this("Rectangle", width, height);
    }

    /** protected: for subclasses such as Square, which pass their own name. */
    protected Rectangle(String name, double width, double height) {
        super(name);
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("width and height must be positive");
        }
        this.width = width;
        this.height = height;
    }

    /** width * height; also correct for Square, which passes the same value twice. */
    @Override
    public double area() {
        return width * height;
    }
}
