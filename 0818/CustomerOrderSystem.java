class Customer { private String id; private String name; Customer(String id, String name) { this.id = id; this.name = name; } String summary() { return id + " " + name; } }
class Order { private String orderId; private Customer customer; private int total; Order(String orderId, Customer customer, int total) { this.orderId = orderId; this.customer = customer; this.total = Math.max(0, total); } String summary() { return orderId + " | " + customer.summary() + " | $" + total; } }
public class CustomerOrderSystem {
    public static void main(String[] args) {
        Customer c = new Customer("C101", "Amy");
        Order o = new Order("O9001", c, 2500);
        System.out.println(o.summary());
    }
}
