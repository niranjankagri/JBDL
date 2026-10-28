package jbdl.session04.streams;

import java.util.List;

/** Sample data for the stream examples: an order with a city, a status and its line items. */
public record Order(int id, String customer, String city, Status status, List<Item> items) {

    public enum Status { PLACED, SHIPPED, DELIVERED, CANCELLED }

    public record Item(String product, int quantity, long priceInRupees) {
        public long total() {
            return quantity * priceInRupees;
        }
    }

    public long total() {
        return items.stream().mapToLong(Item::total).sum();
    }
}
