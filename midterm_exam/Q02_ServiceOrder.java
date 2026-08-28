// 第 2 題：Composition 維修訂單
// 重點：訂單 has-a 多個 LineItem，回傳的 List 必須是複本。

import java.util.ArrayList;
import java.util.List;

public class Q02_ServiceOrder {

    public static class LineItem {
        private final String name;
        private final int unitPrice;
        private final int quantity;

        public LineItem(String name, int unitPrice, int quantity) {
            this.name = name.trim();
            this.unitPrice = unitPrice;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public int subtotal() {
            return unitPrice * quantity;
        }
    }

    private final String orderId;
    private final List<LineItem> items = new ArrayList<>();

    public Q02_ServiceOrder(String orderId) {
        if (isBlank(orderId)) {
            throw new IllegalArgumentException("orderId 不可為 null 或空字串");
        }
        this.orderId = orderId.trim();
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public String getOrderId() {
        return orderId;
    }

    public boolean addItem(String name, int unitPrice, int quantity) {
        if (isBlank(name)) return false;
        if (unitPrice < 0) return false;
        if (quantity <= 0) return false;
        items.add(new LineItem(name, unitPrice, quantity));
        return true;
    }

    public int itemCount() {
        return items.size();
    }

    public int totalAmount() {
        int total = 0;
        for (LineItem item : items) {
            total += item.subtotal();
        }
        return total;
    }

    public String largestItemName() {
        if (items.isEmpty()) return "";
        LineItem largest = items.get(0);
        for (LineItem item : items) {
            if (item.subtotal() > largest.subtotal()) {   // 嚴格大於：平手保留較早加入者
                largest = item;
            }
        }
        return largest.getName();
    }

    public List<String> itemSummaries() {
        List<String> result = new ArrayList<>();          // 新 List，外部改動不影響訂單
        for (LineItem item : items) {
            result.add(item.getName() + ":" + item.subtotal());
        }
        return result;
    }

    public static void main(String[] args) {
        Q02_ServiceOrder order = new Q02_ServiceOrder("R-01");
        order.addItem("Inspection", 300, 1);
        order.addItem("Cable", 80, 4);
        order.addItem("Cleaning", 200, 1);
        System.out.println(order.itemCount());
        System.out.println(order.totalAmount());
        System.out.println(order.largestItemName());
        System.out.println(order.itemSummaries());

        System.out.println("--- 邊界測試 ---");
        System.out.println(order.addItem("  ", 100, 1));
        System.out.println(order.addItem("Free", -1, 1));
        System.out.println(order.addItem("Zero", 100, 0));
        System.out.println(order.itemCount());

        List<String> stolen = order.itemSummaries();
        stolen.clear();
        System.out.println(order.itemSummaries());

        Q02_ServiceOrder empty = new Q02_ServiceOrder("R-02");
        System.out.println("[" + empty.largestItemName() + "] " + empty.totalAmount());
    }
}
