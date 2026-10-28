package jbdl.session01.oopbasics;

import static jbdl.session01.testdata.TestData.BRAND;
import static jbdl.session01.testdata.TestData.MODEL;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CarBuilderTest {

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
}
