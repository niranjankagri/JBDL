package jbdl.session02.propagation;

import java.sql.SQLException;

/** Bottom layer: talks to the "database" and throws the low-level checked SQLException. */
public class OrderRepository {

    private final boolean databaseUp;

    public OrderRepository(boolean databaseUp) {
        this.databaseUp = databaseUp;
    }

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
