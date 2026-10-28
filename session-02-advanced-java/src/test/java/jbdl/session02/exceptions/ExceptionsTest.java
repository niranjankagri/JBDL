package jbdl.session02.exceptions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Topics 2-3: places eight throwables in the hierarchy, checks catch order and multi-catch, the
 * custom exceptions, and exactly when finally and try-with-resources run.
 */
class ExceptionsTest {

    /** Input for the parameterized test: a throwable and the kind it must be classified as. */
    static List<Arguments> throwables() {
        return List.of(
                Arguments.of(new IOException(), ExceptionKinds.Kind.CHECKED),
                Arguments.of(new SQLException(), ExceptionKinds.Kind.CHECKED),
                Arguments.of(new InsufficientBalanceException(1), ExceptionKinds.Kind.CHECKED),
                Arguments.of(new NullPointerException(), ExceptionKinds.Kind.UNCHECKED),
                Arguments.of(new ArithmeticException(), ExceptionKinds.Kind.UNCHECKED),
                Arguments.of(new InvalidAmountException(-1), ExceptionKinds.Kind.UNCHECKED),
                Arguments.of(new StackOverflowError(), ExceptionKinds.Kind.ERROR),
                Arguments.of(new OutOfMemoryError(), ExceptionKinds.Kind.ERROR));
    }

    @ParameterizedTest(name = "{0} is {1}")
    @MethodSource("throwables")
    void hierarchy(Throwable throwable, ExceptionKinds.Kind expected) {
        assertThat(ExceptionKinds.kindOf(throwable)).isEqualTo(expected);
    }

    @Test
    @DisplayName("The most specific catch block wins; multi-catch handles unrelated types")
    void catchOrder() {
        assertThat(ExceptionKinds.handle(() -> {
            throw new FileNotFoundException("a.txt");
        })).isEqualTo("missing file: a.txt");
        assertThat(ExceptionKinds.handle(() -> {
            throw new IOException("disk full");
        })).isEqualTo("other I/O problem: disk full");
        assertThat(ExceptionKinds.handle(() -> {
            throw new IllegalStateException("closed");
        })).isEqualTo("bad input or state: closed");
        assertThat(ExceptionKinds.handle(() -> { })).isEqualTo("ok");
    }

    @Test
    @DisplayName("Custom checked exception carries business data; unchecked one flags a caller bug")
    void customExceptions() {
        Wallet wallet = new Wallet(1_000);

        assertThatThrownBy(() -> wallet.pay(1_500))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessage("insufficient balance: short by 500 paise")
                .extracting(e -> ((InsufficientBalanceException) e).getShortByInPaise()).isEqualTo(500L);
        assertThatThrownBy(() -> wallet.pay(-5)).isInstanceOf(InvalidAmountException.class)
                .isInstanceOf(IllegalArgumentException.class);
        assertThat(wallet.getBalanceInPaise()).isEqualTo(1_000);
    }

    @Test
    @DisplayName("finally runs after a normal try and after a caught exception")
    void finallyAlwaysRuns() {
        assertThat(FinallyDemo.tryCatchFinally(false)).containsExactly("try", "try finished", "finally");
        assertThat(FinallyDemo.tryCatchFinally(true)).containsExactly("try", "catch boom", "finally");
    }

    @Test
    @DisplayName("finally runs after return, but can't change the value already being returned")
    void finallyAfterReturn() {
        List<String> events = new ArrayList<>();

        assertThat(FinallyDemo.returnFromTry(events)).isEqualTo(1);
        assertThat(events).containsExactly("try returns 1", "finally sets value to 2");
    }

    @Test
    @DisplayName("try-with-resources closes in reverse order, before finally")
    void tryWithResourcesOrder() {
        assertThat(FinallyDemo.tryWithResources()).containsExactly(
                "open db", "open file", "use db and file", "close file", "close db", "finally");
    }

    @Test
    @DisplayName("When body and close() both fail, the close failure is kept as suppressed")
    void suppressedExceptions() {
        IllegalStateException thrown = FinallyDemo.bodyAndCloseBothFail();

        assertThat(thrown).hasMessage("body failed on socket");
        assertThat(thrown.getSuppressed()).singleElement()
                .extracting(Throwable::getMessage).isEqualTo("close failed: socket");
    }
}
