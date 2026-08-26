// 課堂實作題五：Skewed Tree Report
// 需求：分別用排序資料與平衡順序建立 tree，
//       比較 size、height 與 search comparison count。

class ShapeNode {
    int value;
    ShapeNode left;
    ShapeNode right;

    ShapeNode(int value) {
        this.value = value;
    }
}

class ShapeBst {
    private ShapeNode root;

    boolean add(int value) {
        if (root == null) {
            root = new ShapeNode(value);
            return true;
        }
        ShapeNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ShapeNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ShapeNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // 回傳搜尋 target 所需的比較次數
    int comparisons(int target) {
        ShapeNode current = root;
        int count = 0;
        while (current != null) {
            count++;
            if (target == current.value) return count;
            current = target < current.value ? current.left : current.right;
        }
        return count;
    }

    int size() {
        return size(root);
    }

    private int size(ShapeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(ShapeNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }
}

public class SkewedBstReport {

    private static final int[] KEYS = {10, 20, 30, 40, 50, 60, 70};

    private static ShapeBst build(int[] order) {
        ShapeBst tree = new ShapeBst();
        for (int value : order) {
            tree.add(value);
        }
        return tree;
    }

    private static void report(String title, int[] order) {
        ShapeBst tree = build(order);
        System.out.println("[" + title + "]");
        System.out.print("insert order=");
        for (int value : order) {
            System.out.print(value + " ");
        }
        System.out.println();
        System.out.println("size=" + tree.size());
        System.out.println("height=" + tree.height());

        int total = 0;
        StringBuilder detail = new StringBuilder();
        for (int key : KEYS) {
            int count = tree.comparisons(key);
            total += count;
            detail.append(key).append(":").append(count).append(" ");
        }
        System.out.println("comparisons=" + detail.toString().trim());
        System.out.println("totalComparisons=" + total);
        System.out.println("missing(35) comparisons=" + tree.comparisons(35));
        System.out.println();
    }

    public static void main(String[] args) {
        report("sorted ascending (skewed right)",
                new int[]{10, 20, 30, 40, 50, 60, 70});
        report("sorted descending (skewed left)",
                new int[]{70, 60, 50, 40, 30, 20, 10});
        report("balanced order",
                new int[]{40, 20, 60, 10, 30, 50, 70});
    }
}
