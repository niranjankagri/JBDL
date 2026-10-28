package jbdl.session01.oopbasics;

import static jbdl.session01.testdata.TestData.ACCOUNT_OWNER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Topic 2 (OOP basics): objects keep their own state, static state is shared, the class refuses
 * invalid input, and == (identity) differs from equals (value), for a class and for a record.
 */
class OopBasicsTest {

    @Test
    @DisplayName("Each object keeps its own state")
    void objectsHaveOwnState() {
        BankAccount first = new BankAccount(ACCOUNT_OWNER, 10_000);
        BankAccount second = new BankAccount("Asha");

        first.deposit(500);

        assertThat(first.getBalanceInPaise()).isEqualTo(10_500);
        assertThat(second.getBalanceInPaise()).isZero();
        assertThat(first.getAccountNumber()).isNotEqualTo(second.getAccountNumber());
    }

    @Test
    @DisplayName("A static field is shared: every new object increases the same counter")
    void staticIsShared() {
        // other tests open accounts too, so compare with the value before, not with a fixed number
        int before = BankAccount.accountsOpened();

        new BankAccount(ACCOUNT_OWNER);
        new BankAccount(ACCOUNT_OWNER);

        assertThat(BankAccount.accountsOpened()).isEqualTo(before + 2);
    }

    @Test
    @DisplayName("The class protects its state: invalid objects and operations are refused")
    void validationKeepsObjectValid() {
        BankAccount account = new BankAccount(ACCOUNT_OWNER, 1_000);

        assertThatThrownBy(() -> new BankAccount(" ")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new BankAccount(ACCOUNT_OWNER, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> account.deposit(0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> account.withdraw(5_000))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("insufficient funds");
        assertThat(account.getBalanceInPaise()).isEqualTo(1_000); // the refused operations changed nothing
    }

    @Test
    @DisplayName("toString shows rupees and paise")
    void readableToString() {
        BankAccount account = new BankAccount(ACCOUNT_OWNER, 12_305);

        assertThat(account.toString()).endsWith("(Niranjan): 123.05");
    }

    @Test
    @DisplayName("== compares identity, equals compares value")
    void identityVersusEquality() {
        Point a = new Point(1, 2);
        Point b = new Point(1, 2);

        assertThat(a == b).isFalse();
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(new Point(2, 1));
    }

    @Test
    @DisplayName("equals + hashCode make a HashSet treat equal points as one")
    void hashSetUsesEqualsAndHashCode() {
        Set<Point> points = new HashSet<>(Set.of(new Point(1, 2)));

        points.add(new Point(1, 2));

        assertThat(points).hasSize(1);
    }

    @Test
    @DisplayName("A record generates equals, hashCode, accessors and toString")
    void recordGeneratesTheSame() {
        Point.PointRecord a = new Point.PointRecord(1, 2);

        assertThat(a).isEqualTo(new Point.PointRecord(1, 2));
        assertThat(a.x()).isEqualTo(1);
        assertThat(a).hasToString("PointRecord[x=1, y=2]");
    }
}
