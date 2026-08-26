// 課後作業五：Tree Shape Experiment
// 需求：使用相同 15 個值以三種順序插入，
//       比較 height 與全部 search comparison count。

class ExperimentNode {
    int value;
    ExperimentNode left;
    ExperimentNode right;

    ExperimentNode(int value) {
        this.value = value;
    }
}

class ExperimentBst {
    private ExperimentNode root;

    boolean add(int value) {
        if (root == null) {
            root = new ExperimentNode(value);
            return true;
        }
        ExperimentNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new ExperimentNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ExperimentNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    int comparisons(int target) {
        ExperimentNode current = root;
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

    private int size(ExperimentNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(ExperimentNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }
}

public class BstShapeExperiment {

    // 相同的 15 個 key，只改變插入順序
    private static final int[] KEYS =
            {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    private static void experiment(String title, int[] insertOrder) {
        ExperimentBst tree = new ExperimentBst();
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
        System.out.println("  totalComparisons(15 keys)=" + total);
        System.out.println("  averageComparisons="
                + String.format("%.2f", total / (double) KEYS.length));
        System.out.println("  worstComparisons=" + worst);
        System.out.println("  missing(0)=" + tree.comparisons(0)
                + "  missing(16)=" + tree.comparisons(16));
        System.out.println();
    }

    public static void main(String[] args) {
        experiment("ascending 1..15",
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});
        experiment("descending 15..1",
                new int[]{15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1});
        experiment("balanced order",
                new int[]{8, 4, 12, 2, 6, 10, 14, 1, 3, 5, 7, 9, 11, 13, 15});
    }
}
