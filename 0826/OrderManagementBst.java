// 課後作業六：訂單管理綜合系統
// 需求：Order 包含 orderId、customer、amount、status。
//       完成 add、find、updateStatus、cancel、remove、id range report
//       與 total amount。
//       只有 CANCELLED 訂單可以 remove，amount 不得為負數。

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ManagedOrder {
    final int orderId;
    final String customer;
    int amount;
    String status;

    ManagedOrder(int orderId, String customer, int amount) {
        this.orderId = orderId;
        this.customer = customer;
        this.amount = amount;
        this.status = "NEW";
    }

    @Override
    public String toString() {
        return orderId + " " + customer + " amount=" + amount
                + " status=" + status;
    }
}

class OrderManagementBstIndex {

    // 允許的狀態；CANCELLED 是唯一可以 remove 的狀態
    static final List<String> STATUSES =
            Arrays.asList("NEW", "PAID", "SHIPPED", "DONE", "CANCELLED");

    private OrderManagementNode root;

    static boolean validStatus(String status) {
        return status != null && STATUSES.contains(status);
    }

    boolean add(ManagedOrder order) {
        if (order == null || order.amount < 0) return false;   // 金額不得為負
        if (root == null) {
            root = new OrderManagementNode(order);
            return true;
        }
        OrderManagementNode current = root;
        while (true) {
            if (order.orderId == current.data.orderId) return false;
            if (order.orderId < current.data.orderId) {
                if (current.left == null) {
                    current.left = new OrderManagementNode(order);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new OrderManagementNode(order);
                    return true;
                }
                current = current.right;
            }
        }
    }

    ManagedOrder find(int orderId) {
        OrderManagementNode current = root;
        while (current != null) {
            if (orderId == current.data.orderId) return current.data;
            current = orderId < current.data.orderId
                    ? current.left
                    : current.right;
        }
        return null;
    }

    boolean updateStatus(int orderId, String status) {
        if (!validStatus(status)) return false;
        ManagedOrder order = find(orderId);
        if (order == null) return false;
        if (order.status.equals("CANCELLED")) return false;   // 已取消不再變更
        order.status = status;
        return true;
    }

    boolean updateAmount(int orderId, int amount) {
        if (amount < 0) return false;
        ManagedOrder order = find(orderId);
        if (order == null || order.status.equals("CANCELLED")) return false;
        order.amount = amount;
        return true;
    }

    boolean cancel(int orderId) {
        ManagedOrder order = find(orderId);
        if (order == null || order.status.equals("CANCELLED")) return false;
        order.status = "CANCELLED";
        return true;
    }

    // 只有 CANCELLED 訂單可以 remove
    boolean remove(int orderId) {
        ManagedOrder order = find(orderId);
        if (order == null || !order.status.equals("CANCELLED")) return false;
        root = remove(root, orderId);
        return true;
    }

    private OrderManagementNode remove(OrderManagementNode node, int orderId) {
        if (node == null) return null;
        if (orderId < node.data.orderId) {
            node.left = remove(node.left, orderId);
        } else if (orderId > node.data.orderId) {
            node.right = remove(node.right, orderId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            OrderManagementNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.orderId);
        }
        return node;
    }

    private OrderManagementNode minimumNode(OrderManagementNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<ManagedOrder> idRange(int lowId, int highId) {
        List<ManagedOrder> result = new ArrayList<>();
        if (lowId > highId) return result;
        idRange(root, lowId, highId, result);
        return result;
    }

    private void idRange(OrderManagementNode node, int low, int high,
                         List<ManagedOrder> result) {
        if (node == null) return;
        int id = node.data.orderId;
        if (low < id) idRange(node.left, low, high, result);
        if (low <= id && id <= high) result.add(node.data);
        if (id < high) idRange(node.right, low, high, result);
    }

    // 總金額不計入已取消訂單
    int totalAmount() {
        return totalAmount(root);
    }

    private int totalAmount(OrderManagementNode node) {
        if (node == null) return 0;
        int current = node.data.status.equals("CANCELLED")
                ? 0
                : node.data.amount;
        return current + totalAmount(node.left) + totalAmount(node.right);
    }

    int allAmount() {
        return allAmount(root);
    }

    private int allAmount(OrderManagementNode node) {
        if (node == null) return 0;
        return node.data.amount + allAmount(node.left) + allAmount(node.right);
    }

    List<ManagedOrder> inorder() {
        List<ManagedOrder> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(OrderManagementNode node, List<ManagedOrder> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    void report(String title) {
        System.out.println("[" + title + "]");
        List<ManagedOrder> orders = inorder();
        for (ManagedOrder order : orders) {
            System.out.println("  " + order);
        }
        System.out.println("  count=" + orders.size()
                + " totalAmount(active)=" + totalAmount()
                + " allAmount=" + allAmount());
        System.out.println();
    }
}

class OrderManagementNode {
    ManagedOrder data;
    OrderManagementNode left;
    OrderManagementNode right;

    OrderManagementNode(ManagedOrder data) {
        this.data = data;
    }
}

public class OrderManagementBst {
    public static void main(String[] args) {
        OrderManagementBstIndex system = new OrderManagementBstIndex();

        System.out.println("add 5003="
                + system.add(new ManagedOrder(5003, "Mina", 1200)));
        System.out.println("add 5001="
                + system.add(new ManagedOrder(5001, "Leo", 450)));
        System.out.println("add 5007="
                + system.add(new ManagedOrder(5007, "Nora", 3000)));
        System.out.println("add 5002="
                + system.add(new ManagedOrder(5002, "Ivy", 780)));
        System.out.println("add 5005="
                + system.add(new ManagedOrder(5005, "Ken", 260)));
        System.out.println("add 5009="
                + system.add(new ManagedOrder(5009, "Ray", 1500)));

        System.out.println("duplicate 5001="
                + system.add(new ManagedOrder(5001, "Copy", 10)));
        System.out.println("negative amount="
                + system.add(new ManagedOrder(5100, "Bad", -1)));
        System.out.println("null order=" + system.add(null));

        system.report("initial");

        System.out.println("find(5005)=" + system.find(5005));
        System.out.println("find(9999)=" + system.find(9999));

        System.out.println("updateStatus(5001, PAID)="
                + system.updateStatus(5001, "PAID"));
        System.out.println("updateStatus(5002, SHIPPED)="
                + system.updateStatus(5002, "SHIPPED"));
        System.out.println("updateStatus(5003, UNKNOWN)="
                + system.updateStatus(5003, "UNKNOWN"));
        System.out.println("updateStatus(9999, PAID)="
                + system.updateStatus(9999, "PAID"));

        System.out.println("updateAmount(5005, 990)="
                + system.updateAmount(5005, 990));
        System.out.println("updateAmount(5005, -5)="
                + system.updateAmount(5005, -5));

        System.out.println("remove active 5001=" + system.remove(5001));
        System.out.println("cancel(5007)=" + system.cancel(5007));
        System.out.println("cancel(5007) again=" + system.cancel(5007));
        System.out.println("updateStatus on cancelled="
                + system.updateStatus(5007, "PAID"));

        system.report("after status changes");

        System.out.println("idRange(5002, 5007)=");
        for (ManagedOrder order : system.idRange(5002, 5007)) {
            System.out.println("  " + order);
        }
        System.out.println("idRange(5007, 5002)="
                + system.idRange(5007, 5002));
        System.out.println();

        System.out.println("remove cancelled 5007=" + system.remove(5007));
        System.out.println("remove missing 9999=" + system.remove(9999));
        System.out.println("cancel and remove 5003="
                + system.cancel(5003) + "/" + system.remove(5003));

        system.report("after remove");
    }
}
