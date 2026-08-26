// 課後作業四：Tree Shape Comparison
// 需求：使用相同 15 個 key，以升冪、降冪與接近平衡三種順序建樹。
//       比較 height、全部 key 的 search comparison total，
//       以及 missing key 的 comparison count。

class ShapeCompareNode {
    int value;
    ShapeCompareNode left;
    ShapeCompareNode right;

    ShapeCompareNode(int value) {
        this.value = value;
    }
}

class ShapeCompareBst {
    private ShapeCompareNode root;

    boolean add(int value) {
        if (root == null) {
            root = new ShapeCompareNode(value);
            return true;
        }
        ShapeCompareNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ShapeCompareNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ShapeCompareNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // 走到 node 就算一次比較；找不到時停在 null
    int comparisons(int target) {
        ShapeCompareNode current = root;
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

    private int size(ShapeCompareNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(ShapeCompareNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }
}

public class TreeShapeComparison {

    // 相同的 15 個 key，只改變插入順序
    private static final int[] KEYS =
            {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150};

    private static final int[] MISSING_KEYS = {5, 75, 155};

    private static void compare(String title, int[] insertOrder) {
        ShapeCompareBst tree = new ShapeCompareBst();
        for (int value : insertOrder) {
            tree.add(value);
        }

        int total = 0;
        int worst = 0;
        for (int key : KEYS) {
            int count = tree.comparisons(key);
            total += count;
            worst = Math.max(worst, count);
        }

        System.out.println("[" + title + "]");
        System.out.println("  size=" + tree.size());
        System.out.println("  height=" + tree.height());
        System.out.println("  searchComparisonTotal(15 keys)=" + total);
        System.out.println("  averageComparisons="
                + String.format("%.2f", total / (double) KEYS.length));
        System.out.println("  worstComparisons=" + worst);

        StringBuilder missing = new StringBuilder();
        for (int key : MISSING_KEYS) {
            missing.append(key).append(":")
                    .append(tree.comparisons(key)).append(" ");
        }
        System.out.println("  missingKeyComparisons="
                + missing.toString().trim());
        System.out.println();
    }

    private static int[] ascending() {
        int[] order = new int[KEYS.length];
        System.arraycopy(KEYS, 0, order, 0, KEYS.length);
        return order;
    }

    private static int[] descending() {
        int[] order = new int[KEYS.length];
        for (int index = 0; index < KEYS.length; index++) {
            order[index] = KEYS[KEYS.length - 1 - index];
        }
        return order;
    }

    // 每次取中間值當 root，得到接近平衡的 tree
    private static int[] balanced() {
        int[] order = new int[KEYS.length];
        fillBalanced(0, KEYS.length - 1, order, new int[]{0});
        return order;
    }

    private static void fillBalanced(int low, int high, int[] order,
                                     int[] position) {
        if (low > high) {
            return;
        }
        int middle = (low + high) / 2;
        order[position[0]] = KEYS[middle];
        position[0]++;
        fillBalanced(low, middle - 1, order, position);
        fillBalanced(middle + 1, high, order, position);
    }

    public static void main(String[] args) {
        compare("ascending insert", ascending());
        compare("descending insert", descending());
        compare("near balanced insert", balanced());
    }
}
