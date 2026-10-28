package jbdl.basics;

import static jbdl.testdata.TestData.BRAND;
import static jbdl.testdata.TestData.MODEL;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BasicsTest {

    @Test
    @DisplayName("Builder sets every named value")
    void builderSetsValues() {
        Car car = Car.builder(BRAND, MODEL)
                .color("Blue").year(2010).topSpeed(150).fuelType("Diesel").automatic(true)
                .build();

        assertThat(car.getBrand()).isEqualTo(BRAND);
        assertThat(car.getName()).isEqualTo(MODEL);
        assertThat(car.getColor()).isEqualTo("Blue");
        assertThat(car.getYear()).isEqualTo(2010);
        assertThat(car.getTopSpeed()).isEqualTo(150);
        assertThat(car.getInfo()).isEqualTo("The car uses Diesel and is automatic.");
    }

    @Test
    @DisplayName("Builder defaults: white, petrol, manual")
    void builderDefaults() {
        Car car = Car.builder(BRAND, MODEL).build();

        assertThat(car.getColor()).isEqualTo("White");
        assertThat(car.isAutomatic()).isFalse();
        assertThat(car.getInfo()).isEqualTo("The car uses Petrol and is manual.");
    }

    @Test
    @DisplayName("(byte) (127 + 1) wraps around to -128")
    void byteOverflowWraps() {
        assertThat(JavaQuiz.addOneAsByte((byte) 127)).isEqualTo((byte) -128);
        assertThat(JavaQuiz.addOneAsByte((byte) 5)).isEqualTo((byte) 6);
    }

    @Test
    @DisplayName("== on Integers: true inside the -128..127 cache, false outside")
    void integerCache() {
        assertThat(JavaQuiz.sameReference(1, 1)).isTrue();
        assertThat(JavaQuiz.sameReference(127, 127)).isTrue();
        assertThat(JavaQuiz.sameReference(1000, 1000)).isFalse();
    }

    @Test
    @DisplayName("a++ > 3 || a == 4 with a = 3 prints GFG")
    void postIncrement() {
        assertThat(JavaQuiz.postIncrementQuiz(3)).isEqualTo("GFG");
        assertThat(JavaQuiz.postIncrementQuiz(4)).isEqualTo("GFG"); // 4 > 3 short-circuits
        assertThat(JavaQuiz.postIncrementQuiz(1)).isEqualTo("Hi");
    }
}
