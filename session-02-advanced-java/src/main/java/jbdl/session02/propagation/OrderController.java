package jbdl.session02.propagation;

/** Top layer: the one place that turns any failure into a response, like a REST controller would. */
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    /** Maps every outcome to a status: 200 found, 400 bad input, 503 the database is down. */
    public String get(int id) {
        try {
            return "200 " + service.orderSummary(id);
        } catch (OrderLookupException e) {
            return "503 " + e.getMessage() + " (cause: " + e.getCause().getMessage() + ")";
        } catch (IllegalArgumentException e) {
            return "400 " + e.getMessage();
        }
    }

    /** The three outcomes, one after another. */
    public static void main(String[] args) {
        System.out.println(new OrderController(new OrderService(new OrderRepository(true))).get(7));
        System.out.println(new OrderController(new OrderService(new OrderRepository(true))).get(-1));
        System.out.println(new OrderController(new OrderService(new OrderRepository(false))).get(7));
    }
}
