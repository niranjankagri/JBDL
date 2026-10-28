package jbdl.session01.principles;

public class Circle extends Shape {

    private final double radius;

    public Circle(double radius) {
        super("Circle");
        if (radius <= 0) {
            throw new IllegalArgumentException("radius must be positive");
        }
        this.radius = radius;
    }

    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}
