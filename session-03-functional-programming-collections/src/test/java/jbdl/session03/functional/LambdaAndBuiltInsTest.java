package jbdl.session03.functional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LambdaAndBuiltInsTest {

    private static final List<String> WORDS = List.of("banana", "fig", "apple");

    @Test
    @DisplayName("Anonymous class, block lambda, expression lambda and method reference sort the same")
    void fourWaysOneComparator() {
        List<Comparator<String>> comparators = List.of(
                LambdaSyntax.BY_LENGTH_ANONYMOUS, LambdaSyntax.BY_LENGTH_BLOCK,
                LambdaSyntax.BY_LENGTH_EXPRESSION, LambdaSyntax.BY_LENGTH_REFERENCE);

        for (Comparator<String> comparator : comparators) {
            List<String> sorted = new ArrayList<>(WORDS);
            sorted.sort(comparator);
            assertThat(sorted).containsExactly("fig", "apple", "banana");
        }
    }

    @Test
    @DisplayName("The four kinds of method reference")
    void methodReferences() {
        assertThat(LambdaSyntax.STATIC_REFERENCE.apply("42")).isEqualTo(42);
        assertThat(LambdaSyntax.boundReference("Mr. ").apply("Kumar")).isEqualTo("Mr. Kumar");
        assertThat(LambdaSyntax.UNBOUND_REFERENCE.apply("java")).isEqualTo("JAVA");
        assertThat(LambdaSyntax.CONSTRUCTOR_REFERENCE.get()).isEmpty();
        assertThat(LambdaSyntax.CONSTRUCTOR_REFERENCE.get()).isNotSameAs(LambdaSyntax.CONSTRUCTOR_REFERENCE.get());
        assertThat(LambdaSyntax.REPEAT.apply("ab", 3)).isEqualTo("ababab");
    }

    @Test
    @DisplayName("A lambda captures an effectively final local variable")
    void capture() {
        assertThat(LambdaSyntax.addTax(18).apply(100)).isEqualTo(118);
        assertThat(LambdaSyntax.addTax(5).apply(200)).isEqualTo(210);
    }

    @Test
    @DisplayName("Supplier, BiFunction, UnaryOperator, BinaryOperator, primitive specialisations")
    void builtIns() {
        assertThat(BuiltInInterfaces.DEFAULT_CURRENCY.get()).isEqualTo("INR");
        assertThat(BuiltInInterfaces.PERCENT_OF.apply(10, 2_500)).isEqualTo(250);
        assertThat(BuiltInInterfaces.TRIM_AND_LOWER.apply("  Hello ")).isEqualTo("hello");
        assertThat(BuiltInInterfaces.LARGER.apply(3, 9)).isEqualTo(9);
        assertThat(IntStream.rangeClosed(1, 10).filter(BuiltInInterfaces.EVEN).sum()).isEqualTo(30);
        assertThat(BuiltInInterfaces.WORD_COUNT.applyAsInt("  one two   three ")).isEqualTo(3);
        assertThat(BuiltInInterfaces.largest(List.of(4, 17, 9))).isEqualTo(17);
    }

    @Test
    @DisplayName("A Supplier runs only when its value is needed")
    void supplierIsLazy() {
        AtomicInteger calls = new AtomicInteger();

        assertThat(BuiltInInterfaces.valueOrDefault("set", () -> "default " + calls.incrementAndGet())).isEqualTo("set");
        assertThat(calls).hasValue(0);
        assertThat(BuiltInInterfaces.valueOrDefault(null, () -> "default " + calls.incrementAndGet())).isEqualTo("default 1");
    }
}
