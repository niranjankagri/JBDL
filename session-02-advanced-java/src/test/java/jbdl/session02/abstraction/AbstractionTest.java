package jbdl.session02.abstraction;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Modifier;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 1: what an abstract class can do that an interface can't (state, constructor, template
 * method) and the reverse (several per class, unrelated implementers), plus default-method
 * conflicts and a sealed interface.
 */
class AbstractionTest {

    @Test
    @DisplayName("Abstract class: shared template, subclass-specific bonus")
    void templateMethod() {
        assertThat(new Developer("Asha", 1_200_000).monthlyPayInRupees()).isEqualTo(105_000);
        assertThat(new Manager("Ravi", 2_400_000, 6).monthlyPayInRupees()).isEqualTo(206_000);
    }

    @Test
    @DisplayName("An abstract class can't be instantiated; an interface has no constructor at all")
    void cannotInstantiate() {
        assertThat(Modifier.isAbstract(Employee.class.getModifiers())).isTrue();
        assertThat(Payable.class.isInterface()).isTrue();
        assertThat(Payable.class.getConstructors()).isEmpty();
    }

    @Test
    @DisplayName("Unrelated classes share an interface; static and default methods work for all")
    void interfaceAcrossUnrelatedClasses() {
        List<Payable> payables = List.of(new Developer("Asha", 1_200_000), new Invoice("Hosting", 15_000));

        assertThat(Payable.totalMonthly(payables)).isEqualTo(120_000);
        assertThat(payables.get(0).yearlyPayInRupees()).isEqualTo(1_260_000); // default method
        assertThat(payables.get(1).yearlyPayInRupees()).isEqualTo(15_000);    // overridden default
    }

    @Test
    @DisplayName("One class, one parent class, several interfaces")
    void multipleInterfaces() {
        Manager manager = new Manager("Ravi", 2_400_000, 6);

        assertThat(manager).isInstanceOf(Employee.class).isInstanceOf(Payable.class).isInstanceOf(Taxable.class);
        assertThat(manager.taxInRupees()).isEqualTo(210_000); // 10 % of (24 L - 3 L)
        assertThat(Taxable.TAX_FREE_LIMIT_IN_RUPEES).isEqualTo(300_000);
    }

    @Test
    @DisplayName("Diamond with default methods: the class decides, using X.super.method()")
    void diamondResolvedExplicitly() {
        assertThat(new SmartPhone().describe()).isEqualTo("takes photos + plays music");
    }

    @Test
    @DisplayName("Sealed interface: only the permitted classes implement it")
    void sealedInterface() {
        assertThat(PaymentMethod.class.isSealed()).isTrue();
        assertThat(PaymentMethod.class.getPermittedSubclasses()).hasSize(3);
        assertThat(PaymentMethod.describe(new PaymentMethod.Card("4242"))).isEqualTo("card ending 4242");
        assertThat(PaymentMethod.describe(new PaymentMethod.Cash())).isEqualTo("cash");
    }
}
