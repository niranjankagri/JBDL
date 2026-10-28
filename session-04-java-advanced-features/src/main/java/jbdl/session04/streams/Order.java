package jbdl.session04.streams;

import java.util.List;

/** Sample data for the stream examples: an order with a city, a status and its line items. */
public record Order(int id, String customer, String city, Status status, List<Item> items) {

    /** Life cycle of an order; cancelled orders are left out of revenue. */
    public enum Status { PLACED, SHIPPED, DELIVERED, CANCELLED }

    /** One line of an order: product, how many, price per piece. */
    public record Item(String product, int quantity, long priceInRupees) {
        public long total() {
            return quantity * priceInRupees;
        }
    }

    /** Sum of the line totals (a small stream inside the record). */
    public long total() {
        return items.stream().mapToLong(Item::total).sum();
    }
}
