// Customer 已定義於同資料夾的 OrderCompositionDemo.java（概念 6），本檔直接使用。

class OrderItem {
    private String productName;
    private int unitPrice;
    private int quantity;

    OrderItem(String productName, int unitPrice, int quantity) {
        this.productName = productName;
        this.unitPrice = Math.max(0, unitPrice);
        this.quantity = Math.max(0, quantity);
    }

    int getQuantity() {
        return quantity;
    }

    int subtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return productName + " 單價=" + unitPrice + " 數量=" + quantity
                + " 小計=" + subtotal();
    }
}

class CustomerOrder {
    private String orderId;
    private Customer customer;
    private OrderItem[] items;
    private int itemCount;

    CustomerOrder(String orderId, Customer customer, int capacity) {
        this.orderId = orderId;
        this.customer = customer;
        this.items = new OrderItem[Math.max(1, capacity)];
        this.itemCount = 0;
    }

    boolean addItem(OrderItem item) {
        if (item == null || itemCount >= items.length) {
            return false;
        }
        items[itemCount] = item;
        itemCount++;
        return true;
    }

    int totalAmount() {
        int total = 0;
        for (int i = 0; i < itemCount; i++) {
            total += items[i].subtotal();
        }
        return total;
    }

    int totalQuantity() {
        int total = 0;
        for (int i = 0; i < itemCount; i++) {
            total += items[i].getQuantity();
        }
        return total;
    }

    int getItemCount() {
        return itemCount;
    }

    Customer getCustomer() {
        return customer;
    }

    void printSummary() {
        System.out.println("訂單編號：" + orderId);
        System.out.println("顧客：" + customer.label());
        System.out.println("品項（" + itemCount + " 筆）：");
        for (int i = 0; i < itemCount; i++) {
            System.out.println("  " + items[i]);
        }
        System.out.println("總數量：" + totalQuantity());
        System.out.println("訂單總額：" + totalAmount());
    }
}

public class CustomerOrderSystem {
    public static void main(String[] args) {
        Customer amy = new Customer("C101", "Amy");
        CustomerOrder order = new CustomerOrder("O9001", amy, 3);

        System.out.println("=== 加入品項 ===");
        System.out.println("addItem 滑鼠：" + order.addItem(new OrderItem("無線滑鼠", 690, 2)));
        System.out.println("addItem 鍵盤：" + order.addItem(new OrderItem("機械鍵盤", 2480, 1)));
        System.out.println("addItem 螢幕：" + order.addItem(new OrderItem("27 吋螢幕", 5990, 1)));
        System.out.println("addItem 喇叭（已滿）：" + order.addItem(new OrderItem("喇叭", 1200, 1)));
        System.out.println("addItem null：" + order.addItem(null));
        System.out.println("實際品項數：" + order.getItemCount());

        System.out.println();
        System.out.println("=== 訂單摘要 ===");
        order.printSummary();

        System.out.println();
        System.out.println("=== 第二筆訂單，共用同一位顧客物件 ===");
        CustomerOrder second = new CustomerOrder("O9002", amy, 2);
        second.addItem(new OrderItem("USB 集線器", 850, 3));
        second.printSummary();
        System.out.println("兩張訂單的顧客是同一個物件："
                + (order.getCustomer() == second.getCustomer()));

        System.out.println();
        System.out.println("=== 邊界值 ===");
        CustomerOrder invalid = new CustomerOrder("O9003", new Customer("C102", "Ben"), 2);
        invalid.addItem(new OrderItem("負數測試", -100, -5));
        invalid.printSummary();

        System.out.println();
        System.out.println("Composition 說明：CustomerOrder 只保存一個 Customer reference");
        System.out.println("與一個固定長度的 OrderItem[]，沒有把顧客與品項拆成平行陣列。");
    }
}
