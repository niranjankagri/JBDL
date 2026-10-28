package jbdl.session02.propagation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.sql.SQLException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PropagationTest {

    private static OrderController controller(boolean databaseUp) {
        return new OrderController(new OrderService(new OrderRepository(databaseUp)));
    }

    @Test
    @DisplayName("Happy path through all three layers")
    void happyPath() {
        assertThat(controller(true).get(7)).isEqualTo("200 Summary of order-7");
    }

    @Test
    @DisplayName("A checked exception is wrapped by the service; the original stays as the cause")
    void checkedIsWrappedWithCause() {
        OrderService service = new OrderService(new OrderRepository(false));

        assertThatThrownBy(() -> service.orderSummary(7))
                .isInstanceOf(OrderLookupException.class)
                .hasMessage("could not load order 7")
                .cause().isInstanceOf(SQLException.class).hasMessage("connection refused");
    }

    @Test
    @DisplayName("An unchecked exception propagates up untouched until someone catches it")
    void uncheckedPropagates() {
        OrderService service = new OrderService(new OrderRepository(true));

        assertThatThrownBy(() -> service.orderSummary(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("order id must be positive: -1")
                .satisfies(e -> assertThat(e.getStackTrace()[0].getClassName()).endsWith("OrderRepository"));
    }

    @Test
    @DisplayName("The top layer turns each failure into a response")
    void controllerMapsFailures() {
        assertThat(controller(true).get(-1)).isEqualTo("400 order id must be positive: -1");
        assertThat(controller(false).get(7)).isEqualTo("503 could not load order 7 (cause: connection refused)");
    }
}
