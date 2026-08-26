// 課後作業二：商品庫存 BST
// 需求：完成新增、查詢、補貨、扣庫存、delete 與 inorder report。
//       所有修改都先依 id 找到 object 再操作。

class InventoryProduct {
    final int id;
    final String name;
    int stock;

    InventoryProduct(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = Math.max(0, stock);
    }

    @Override
    public String toString() {
        return id + " " + name + " stock=" + stock;
    }
}

class InventoryNode {
    InventoryProduct data;
    InventoryNode left;
    InventoryNode right;

    InventoryNode(InventoryProduct data) {
        this.data = data;
    }
}

class InventoryBst {
    private InventoryNode root;

    boolean add(InventoryProduct product) {
        if (product == null) return false;
        if (root == null) {
            root = new InventoryNode(product);
            return true;
        }
        InventoryNode current = root;
        while (true) {
            if (product.id == current.data.id) return false;
            if (product.id < current.data.id) {
                if (current.left == null) {
                    current.left = new InventoryNode(product);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new InventoryNode(product);
                    return true;
                }
                current = current.right;
            }
        }
    }

    InventoryProduct find(int id) {
        InventoryNode current = root;
        while (current != null) {
            if (id == current.data.id) return current.data;
            current = id < current.data.id ? current.left : current.right;
        }
        return null;
    }

    // 補貨：先找到 object 再修改欄位
    boolean restock(int id, int amount) {
        if (amount <= 0) return false;
        InventoryProduct product = find(id);
        if (product == null) return false;
        product.stock += amount;
        return true;
    }

    // 扣庫存：庫存不足就整筆拒絕
    boolean ship(int id, int amount) {
        if (amount <= 0) return false;
        InventoryProduct product = find(id);
        if (product == null || product.stock < amount) return false;
        product.stock -= amount;
        return true;
    }

    boolean delete(int id) {
        if (find(id) == null) return false;
        root = delete(root, id);
        return true;
    }

    private InventoryNode delete(InventoryNode node, int id) {
        if (node == null) return null;
        if (id < node.data.id) {
            node.left = delete(node.left, id);
        } else if (id > node.data.id) {
            node.right = delete(node.right, id);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            InventoryNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data.id);
        }
        return node;
    }

    private InventoryNode minimumNode(InventoryNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    int totalStock() {
        return totalStock(root);
    }

    private int totalStock(InventoryNode node) {
        if (node == null) return 0;
        return node.data.stock + totalStock(node.left) + totalStock(node.right);
    }

    void report(String title) {
        System.out.println("[" + title + "]");
        report(root);
        System.out.println("  totalStock=" + totalStock());
        System.out.println();
    }

    private void report(InventoryNode node) {
        if (node == null) return;
        report(node.left);
        System.out.println("  " + node.data);
        report(node.right);
    }
}

public class ProductInventoryBst {
    public static void main(String[] args) {
        InventoryBst inventory = new InventoryBst();
        inventory.add(new InventoryProduct(300, "Keyboard", 5));
        inventory.add(new InventoryProduct(100, "Mouse", 8));
        inventory.add(new InventoryProduct(500, "Monitor", 2));
        inventory.add(new InventoryProduct(200, "Hub", 4));
        inventory.add(new InventoryProduct(400, "Cable", 30));
        System.out.println("duplicate 100="
                + inventory.add(new InventoryProduct(100, "Other", 1)));

        inventory.report("initial");

        System.out.println("find(200)=" + inventory.find(200));
        System.out.println("find(999)=" + inventory.find(999));

        System.out.println("restock(500, 10)=" + inventory.restock(500, 10));
        System.out.println("restock(999, 10)=" + inventory.restock(999, 10));
        System.out.println("restock(500, 0)=" + inventory.restock(500, 0));

        System.out.println("ship(100, 3)=" + inventory.ship(100, 3));
        System.out.println("ship(100, 99)=" + inventory.ship(100, 99));
        System.out.println("ship(999, 1)=" + inventory.ship(999, 1));

        inventory.report("after restock and ship");

        System.out.println("delete(200)=" + inventory.delete(200));
        System.out.println("delete(300)=" + inventory.delete(300));
        System.out.println("delete(999)=" + inventory.delete(999));

        inventory.report("after delete");
    }
}
