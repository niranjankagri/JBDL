package jbdl.session04.generics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GenericsTest {

    record Customer(int id, String name) {
    }

    @Test
    @DisplayName("Generic class and generic method keep the types without casts")
    void pairAndSwap() {
        Pair<String, Integer> pair = new Pair<>("age", 30);
        Pair<Integer, String> swapped = Pair.swap(pair);

        int age = swapped.first(); // no cast needed
        assertThat(age).isEqualTo(30);
        assertThat(swapped.second()).isEqualTo("age");
    }

    @Test
    @DisplayName("Bounded type parameter: max works for any Comparable type")
    void boundedMax() {
        assertThat(GenericMethods.max(List.of(3, 11, 7))).isEqualTo(11);
        assertThat(GenericMethods.max(List.of("pear", "apple", "zucchini"))).isEqualTo("zucchini");
        assertThat(GenericMethods.max(List.of(LocalDate.of(2024, 8, 24), LocalDate.of(2026, 10, 28))))
                .isEqualTo(LocalDate.of(2026, 10, 28));
        assertThatThrownBy(() -> GenericMethods.max(List.<Integer>of())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("? extends Number accepts lists of any Number subtype")
    void upperBoundedWildcard() {
        assertThat(GenericMethods.sum(List.of(1, 2, 3))).isEqualTo(6.0);
        assertThat(GenericMethods.sum(List.of(1.5, 2.5))).isEqualTo(4.0);
    }

    @Test
    @DisplayName("PECS: copy Integers into a List<Number> or List<Object>")
    void producerExtendsConsumerSuper() {
        List<Number> numbers = new ArrayList<>();
        List<Object> objects = new ArrayList<>();

        GenericMethods.copy(List.of(1, 2), numbers);
        GenericMethods.<Integer>copy(List.of(3), objects);

        assertThat(numbers).containsExactly(1, 2);
        assertThat(objects).containsExactly(3);
    }

    @Test
    @DisplayName("Type erasure: List<String> and List<Integer> are the same class at run time")
    void erasure() {
        assertThat(GenericMethods.sameRuntimeClass()).isTrue();
    }

    @Test
    @DisplayName("One generic repository class stores any entity with any id type")
    void genericRepository() {
        InMemoryRepository<Customer, Integer> customers = new InMemoryRepository<>(Customer::id);
        InMemoryRepository<String, Character> words = new InMemoryRepository<>(word -> word.charAt(0));

        customers.save(new Customer(1, "Asha"));
        customers.save(new Customer(2, "Ravi"));
        customers.save(new Customer(1, "Asha K")); // same id: replaces
        words.save("java");

        assertThat(customers.count()).isEqualTo(2);
        assertThat(customers.findById(1)).map(Customer::name).contains("Asha K");
        assertThat(customers.findById(9)).isEmpty();
        assertThat(customers.deleteById(2)).isTrue();
        assertThat(customers.findAll()).extracting(Customer::name).containsExactly("Asha K");
        assertThat(words.findById('j')).contains("java");
    }
}
