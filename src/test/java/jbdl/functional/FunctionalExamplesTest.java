package jbdl.functional;

import static jbdl.testdata.TestData.FULL_NAME;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FunctionalExamplesTest {

    @Test
    @DisplayName("Named class, anonymous class and lambda all implement Greeting")
    void threeImplementations() {
        assertThat(FunctionalExamples.NAMED_CLASS.greet("Ana")).isEqualTo("Good morning, Ana");
        assertThat(FunctionalExamples.ANONYMOUS_CLASS.greet("Ana")).isEqualTo("Hello, Ana");
        assertThat(FunctionalExamples.LAMBDA.greet("Ana")).isEqualTo("Hi, Ana");
    }

    @Test
    @DisplayName("The default method works for a lambda too")
    void defaultMethod() {
        assertThat(FunctionalExamples.LAMBDA.greetLoudly("Ana")).isEqualTo("HI, ANA");
    }

    @Test
    @DisplayName("Class-only methods need a variable of the class type")
    void classOnlyMethod() {
        FormalGreeting formal = new FormalGreeting();
        Greeting asInterface = formal;

        assertThat(formal.signature()).isEqualTo("Kind regards");
        assertThat(asInterface.greet("Ana")).isEqualTo(formal.greet("Ana"));
    }

    @Test
    @DisplayName("Function: ASCII code to char, first letter")
    void functions() {
        assertThat(FunctionalExamples.ASCII_TO_CHAR.apply(100)).isEqualTo('d');
        assertThat(FunctionalExamples.FIRST_LETTER.apply("Niranjan")).isEqualTo('N');
    }

    @Test
    @DisplayName("andThen runs after, compose runs before")
    void andThenVersusCompose() {
        assertThat(FunctionalExamples.halfThenTriple(10)).isEqualTo(15.0);
        assertThat(FunctionalExamples.tripleThenHalf(5)).isEqualTo(7.5);
    }

    @ParameterizedTest(name = "{0} outside 10..20 -> {1}")
    @CsvSource({"6, true", "10, false", "16, false", "20, false", "21, true"})
    void predicateOr(int number, boolean expected) {
        assertThat(FunctionalExamples.OUTSIDE_10_TO_20.test(number)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Predicate and / negate")
    void predicateAndNegate() {
        assertThat(FunctionalExamples.LESS_THAN_10.and(FunctionalExamples.MORE_THAN_20).test(16)).isFalse();
        assertThat(FunctionalExamples.LESS_THAN_10.negate().test(6)).isFalse();
    }

    @Test
    @DisplayName("Chained Consumers run in order: first name, then last name")
    void chainedConsumers() {
        List<String> parts = new ArrayList<>();
        FunctionalExamples.splitName(FULL_NAME, parts);

        assertThat(parts).containsExactly("Niranjan", "Kumar");
    }

    @Test
    @DisplayName("One MetroEngine interface, two lambda behaviours")
    void metroEngines() {
        assertThat(MetroEngine.DRIVER_BASED.start()).isEqualTo("Drive with driver");
        assertThat(MetroEngine.DRIVERLESS.start()).isEqualTo("Drive without driver");
    }
}
