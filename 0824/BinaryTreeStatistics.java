// 課後作業二：Binary Tree 統計系統
// 需求：完成 size、sum、maximum、leaf count、height 與 contains。
//       maximum 要明確處理 empty tree，不可一律用 0 代表空樹最大值。

class StatisticsNode {
    int value;
    StatisticsNode left;
    StatisticsNode right;

    StatisticsNode(int value) {
        this.value = value;
    }
}

public class BinaryTreeStatistics {

    static int size(StatisticsNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    static int sum(StatisticsNode node) {
        if (node == null) {
            return 0;
        }
        return node.value + sum(node.left) + sum(node.right);
    }

    // Empty tree 沒有最大值，回傳 null 而不是 0
    static Integer maximum(StatisticsNode node) {
        if (node == null) {
            return null;
        }
        int best = node.value;
        Integer leftMax = maximum(node.left);
        if (leftMax != null && leftMax > best) {
            best = leftMax;
        }
        Integer rightMax = maximum(node.right);
        if (rightMax != null && rightMax > best) {
            best = rightMax;
        }
        return best;
    }

    static int leafCount(StatisticsNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    static int height(StatisticsNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    static boolean contains(StatisticsNode node, int target) {
        if (node == null) {
            return false;
        }
        return node.value == target
                || contains(node.left, target)
                || contains(node.right, target);
    }

    static void report(String title, StatisticsNode root) {
        System.out.println("[" + title + "]");
        System.out.println("size=" + size(root));
        System.out.println("sum=" + sum(root));
        System.out.println("maximum=" + maximum(root));
        System.out.println("leafCount=" + leafCount(root));
        System.out.println("height=" + height(root));
        System.out.println("contains(-4)=" + contains(root, -4));
        System.out.println("contains(999)=" + contains(root, 999));
        System.out.println();
    }

    //          12
    //        /    \
    //      -4      35
    //     /  \       \
    //    7    3       18
    private static StatisticsNode buildTree() {
        StatisticsNode root = new StatisticsNode(12);
        root.left = new StatisticsNode(-4);
        root.right = new StatisticsNode(35);
        root.left.left = new StatisticsNode(7);
        root.left.right = new StatisticsNode(3);
        root.right.right = new StatisticsNode(18);
        return root;
    }

    public static void main(String[] args) {
        report("six nodes", buildTree());
        report("empty tree", null);
        report("single node", new StatisticsNode(-4));
    }
}
