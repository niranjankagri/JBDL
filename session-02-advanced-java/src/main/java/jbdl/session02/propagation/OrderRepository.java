package jbdl.session02.propagation;

import java.sql.SQLException;

/** Bottom layer: talks to the "database" and throws the low-level checked SQLException. */
public class OrderRepository {

    private final boolean databaseUp;

    /** databaseUp = false simulates an outage, so the tests can trigger the checked exception. */
    public OrderRepository(boolean databaseUp) {
        this.databaseUp = databaseUp;
    }

    /** Declares the checked SQLException; the unchecked IllegalArgumentException needs no declaration. */
    public String findOrder(int id) throws SQLException {
        if (!databaseUp) {
            throw new SQLException("connection refused", "08001");
        }
        if (id <= 0) {
            throw new IllegalArgumentException("order id must be positive: " + id); // unchecked
        }
        return "order-" + id;
    }
}
