// Customer 定義在同資料夾的 Customer.java，本檔直接使用。
class Order {
    private String orderId;
    private Customer customer;
    private int total;

    Order(String orderId, Customer customer, int total) {
        this.orderId = orderId;
        this.customer = customer;
        this.total = Math.max(0, total);
    }

    String summary() {
        return orderId + " | " + customer.label() + " | $" + total;
    }
}

public class OrderCompositionDemo {
    public static void main(String[] args) {
        Customer customer = new Customer("C101", "Amy");
        Order order = new Order("O9001", customer, 2500);

        System.out.println(order.summary());
    }
}
