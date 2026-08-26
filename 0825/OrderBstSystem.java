// 課後作業六：訂單索引系統
// 需求：Order 以 orderId 排序，完成 add、find、cancel、updateAmount、
//       range report 與 summary。

class OrderRecord {
    final int orderId;
    final String customer;
    int amount;
    String status;

    OrderRecord(int orderId, String customer, int amount) {
        this.orderId = orderId;
        this.customer = customer;
        this.amount = Math.max(0, amount);
        this.status = "ACTIVE";
    }

    @Override
    public String toString() {
        return orderId + " " + customer + " amount=" + amount
                + " status=" + status;
    }
}

class OrderNode {
    OrderRecord data;
    OrderNode left;
    OrderNode right;

    OrderNode(OrderRecord data) {
        this.data = data;
    }
}

class OrderBst {
    private OrderNode root;

    boolean add(OrderRecord order) {
        if (order == null) return false;
        if (root == null) {
            root = new OrderNode(order);
            return true;
        }
        OrderNode current = root;
        while (true) {
            if (order.orderId == current.data.orderId) return false;
            if (order.orderId < current.data.orderId) {
                if (current.left == null) {
                    current.left = new OrderNode(order);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new OrderNode(order);
                    return true;
                }
                current = current.right;
            }
        }
    }

    OrderRecord find(int orderId) {
        OrderNode current = root;
        while (current != null) {
            if (orderId == current.data.orderId) return current.data;
            current = orderId < current.data.orderId
                    ? current.left
                    : current.right;
        }
        return null;
    }

    // 取消：改狀態，不從 tree 移除
    boolean cancel(int orderId) {
        OrderRecord order = find(orderId);
        if (order == null || order.status.equals("CANCELLED")) return false;
        order.status = "CANCELLED";
        return true;
    }

    // 金額不得為負數；已取消的訂單不得改金額
    boolean updateAmount(int orderId, int amount) {
        if (amount < 0) return false;
        OrderRecord order = find(orderId);
        if (order == null || order.status.equals("CANCELLED")) return false;
        order.amount = amount;
        return true;
    }

    void rangeReport(int lowId, int highId) {
        System.out.println("range[" + lowId + "," + highId + "]");
        if (lowId > highId) {
            System.out.println("  invalid range, empty result");
            return;
        }
        rangeReport(root, lowId, highId);
    }

    private void rangeReport(OrderNode node, int low, int high) {
        if (node == null) return;
        if (low < node.data.orderId) {
            rangeReport(node.left, low, high);
        }
        if (low <= node.data.orderId && node.data.orderId <= high) {
            System.out.println("  " + node.data);
        }
        if (node.data.orderId < high) {
            rangeReport(node.right, low, high);
        }
    }

    void summary() {
        int[] counters = new int[3];    // 0=total, 1=active, 2=cancelled
        int[] amounts = new int[2];     // 0=activeAmount, 1=allAmount
        summary(root, counters, amounts);
        System.out.println("summary");
        System.out.println("  totalOrders=" + counters[0]);
        System.out.println("  active=" + counters[1]
                + "  cancelled=" + counters[2]);
        System.out.println("  activeAmount=" + amounts[0]);
        System.out.println("  allAmount=" + amounts[1]);
    }

    private void summary(OrderNode node, int[] counters, int[] amounts) {
        if (node == null) return;
        summary(node.left, counters, amounts);
        counters[0]++;
        amounts[1] += node.data.amount;
        if (node.data.status.equals("CANCELLED")) {
            counters[2]++;
        } else {
            counters[1]++;
            amounts[0] += node.data.amount;
        }
        summary(node.right, counters, amounts);
    }

    void inorderReport(String title) {
        System.out.println("[" + title + "]");
        inorderReport(root);
        System.out.println();
    }

    private void inorderReport(OrderNode node) {
        if (node == null) return;
        inorderReport(node.left);
        System.out.println("  " + node.data);
        inorderReport(node.right);
    }
}

public class OrderBstSystem {
    public static void main(String[] args) {
        OrderBst system = new OrderBst();
        system.add(new OrderRecord(5003, "Mina", 1200));
        system.add(new OrderRecord(5001, "Leo", 450));
        system.add(new OrderRecord(5007, "Nora", 3000));
        system.add(new OrderRecord(5002, "Ivy", 780));
        system.add(new OrderRecord(5005, "Ken", 260));
        system.add(new OrderRecord(5009, "Ray", 1500));
        System.out.println("duplicate 5001="
                + system.add(new OrderRecord(5001, "Copy", 10)));
        System.out.println("null=" + system.add(null));

        system.inorderReport("initial");

        System.out.println("find(5005)=" + system.find(5005));
        System.out.println("find(9999)=" + system.find(9999));

        System.out.println("updateAmount(5005, 990)="
                + system.updateAmount(5005, 990));
        System.out.println("updateAmount(5005, -1)="
                + system.updateAmount(5005, -1));
        System.out.println("updateAmount(9999, 100)="
                + system.updateAmount(9999, 100));

        System.out.println("cancel(5007)=" + system.cancel(5007));
        System.out.println("cancel(5007) again=" + system.cancel(5007));
        System.out.println("cancel(9999)=" + system.cancel(9999));
        System.out.println("updateAmount on cancelled="
                + system.updateAmount(5007, 100));

        system.inorderReport("after update and cancel");

        system.rangeReport(5002, 5007);
        System.out.println();
        system.rangeReport(5007, 5002);
        System.out.println();
        system.summary();
    }
}
