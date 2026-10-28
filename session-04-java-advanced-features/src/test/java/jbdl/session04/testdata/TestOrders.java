package jbdl.session04.testdata;

import java.util.List;
import jbdl.session04.streams.Order;
import jbdl.session04.streams.Order.Item;
import jbdl.session04.streams.Order.Status;

/** Five orders shared by the stream tests; totals are noted so expected values are easy to check. */
public final class TestOrders {

    private TestOrders() {
    }

    public static final List<Order> ORDERS = List.of(
            // total 2,400
            new Order(1, "Asha", "Pune", Status.DELIVERED, List.of(new Item("pen", 10, 20), new Item("notebook", 4, 550))),
            // total 1,500
            new Order(2, "Ravi", "Delhi", Status.SHIPPED, List.of(new Item("mouse", 1, 1_500))),
            // total 900 (cancelled: not revenue)
            new Order(3, "Asha", "Pune", Status.CANCELLED, List.of(new Item("pen", 45, 20))),
            // total 6,000
            new Order(4, "Meena", "Bengaluru", Status.DELIVERED, List.of(new Item("keyboard", 2, 3_000))),
            // total 600
            new Order(5, "Kiran", "Delhi", Status.DELIVERED, List.of(new Item("pen", 30, 20))));
}
