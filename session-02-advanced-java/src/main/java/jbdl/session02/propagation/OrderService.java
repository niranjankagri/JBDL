package jbdl.session02.propagation;

import java.sql.SQLException;

/**
 * Middle layer. A checked exception must be caught or declared at EVERY level it passes. Here the
 * service translates SQLException into its own OrderLookupException and keeps the original as the
 * cause, so callers don't depend on SQL details but nothing is lost for debugging.
 */
public class OrderService {

    private final OrderRepository repository;

    public OrderService(OrderRepository repository) {
        this.repository = repository;
    }

    public String orderSummary(int id) throws OrderLookupException {
        try {
            return "Summary of " + repository.findOrder(id);
        } catch (SQLException e) {
            throw new OrderLookupException("could not load order " + id, e); // wrap, keep the cause
        }
        // IllegalArgumentException is unchecked: it propagates up untouched, no code needed here
    }
}
