package jbdl.session01.principles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 3 (the four OOP pillars) on the Shape hierarchy: each subclass's own area(), one loop for
 * all shapes (overriding), overloads chosen by declared type, and validating constructors.
 */
class ShapesTest {

    // areas are doubles: compare within a tolerance, never with exact equality
    private static final double PRECISION = 1e-9;

    @Test
    @DisplayName("Each subclass computes its own area")
    void ownFormulas() {
        assertThat(new Circle(1).area()).isCloseTo(Math.PI, within(PRECISION));
        assertThat(new Rectangle(2, 3).area()).isEqualTo(6.0);
        assertThat(new Square(2).area()).isEqualTo(4.0);
    }

    @Test
    @DisplayName("Polymorphism: one loop over Shape runs each subclass's area()")
    void dynamicDispatch() {
        List<Shape> shapes = List.of(new Circle(1), new Rectangle(2, 3), new Square(2));

        assertThat(Shapes.totalArea(shapes)).isCloseTo(Math.PI + 6 + 4, within(PRECISION));
    }

    @Test
    @DisplayName("Inheritance: a Square is a Rectangle and a Shape, and reuses Rectangle's area()")
    void squareIsARectangle() {
        Shape square = new Square(3);

        assertThat(square).isInstanceOf(Rectangle.class).isInstanceOf(Shape.class);
        assertThat(square.describe()).isEqualTo("Square with area 9.00");
    }

    @Test
    @DisplayName("Overloads are picked by the declared type, at compile time")
    void overloadingUsesDeclaredType() {
        Shape declaredAsShape = new Circle(1);
        Circle declaredAsCircle = new Circle(1);

        assertThat(Shapes.label(declaredAsShape)).isEqualTo("shape: Circle");
        assertThat(Shapes.label(declaredAsCircle)).isEqualTo("round shape: Circle");
        assertThat(Shapes.label(declaredAsShape, 2)).isEqualTo("shape: Circle (3.14)");
    }

    @Test
    @DisplayName("Encapsulation: constructors refuse shapes that can't exist")
    void invalidShapesAreRefused() {
        assertThatThrownBy(() -> new Circle(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Rectangle(-1, 2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Square(0)).isInstanceOf(IllegalArgumentException.class);
    }
}
