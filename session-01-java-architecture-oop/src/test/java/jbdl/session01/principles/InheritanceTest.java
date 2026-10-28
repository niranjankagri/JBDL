package jbdl.session01.principles;

import static jbdl.session01.testdata.TestData.BRAND;
import static jbdl.session01.testdata.TestData.COLOR;
import static jbdl.session01.testdata.TestData.FIRST_RACE_YEAR;
import static jbdl.session01.testdata.TestData.MODEL;
import static jbdl.session01.testdata.TestData.RACING_BRAND;
import static jbdl.session01.testdata.TestData.RACING_MODEL;
import static jbdl.session01.testdata.TestData.TROPHIES;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Year;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class InheritanceTest {

    @Test
    @DisplayName("Car describes itself with brand and model")
    void carInfo() {
        assertThat(new Car(BRAND, COLOR, MODEL).getInfo()).isEqualTo("The car is Hyundai's Santro");
    }

    @Test
    @DisplayName("Two-argument constructor chains to the three-argument one")
    void constructorChaining() {
        Car car = new Car(BRAND, COLOR);

        assertThat(car.getName()).isEqualTo("unknown model");
        assertThat(car.getColor()).isEqualTo(COLOR);
    }

    @Test
    @DisplayName("Through a Car variable, the SuperCar override runs and extends the parent text")
    void overrideUsesSuper() {
        Car car = new SuperCar(RACING_BRAND, "Black", RACING_MODEL, FIRST_RACE_YEAR, TROPHIES);

        assertThat(car).isInstanceOf(SuperCar.class);
        assertThat(car.getInfo())
                .startsWith("The car is McLaren's F1")
                .endsWith("racing since 2005 with 40 trophies");
    }

    @Test
    @DisplayName("Factory returns the right subclass for each kind")
    void factoryPicksSubclass() {
        Car city = CarFactory.create(CarFactory.Kind.CITY, BRAND, COLOR, MODEL);
        Car racing = CarFactory.create(CarFactory.Kind.RACING, RACING_BRAND, COLOR, RACING_MODEL);

        assertThat(city).isExactlyInstanceOf(Car.class);
        assertThat(racing).isExactlyInstanceOf(SuperCar.class);
        assertThat(((SuperCar) racing).getTrophiesWon()).isZero();
        assertThat(((SuperCar) racing).getFirstRaceYear()).isEqualTo(Year.now().getValue());
    }
}
