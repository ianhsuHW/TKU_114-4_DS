// 課堂實作題四：Range Statistics
// 需求：完成 valuesBetween(low, high)、countBetween(low, high)
//       與 sumBetween(low, high)。
//       三個結果都必須使用 BST 方向剪枝，並測試空範圍與 low > high。

import java.util.ArrayList;
import java.util.List;

class StatRangeNode {
    int value;
    StatRangeNode left;
    StatRangeNode right;

    StatRangeNode(int value) {
        this.value = value;
    }
}

class StatRangeBst {
    private StatRangeNode root;
    private int visitedNodes;   // 用來證明剪枝確實減少走訪

    boolean add(int value) {
        if (root == null) {
            root = new StatRangeNode(value);
            return true;
        }
        StatRangeNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new StatRangeNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StatRangeNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    int visitedNodes() {
        return visitedNodes;
    }

    List<Integer> valuesBetween(int low, int high) {
        visitedNodes = 0;
        List<Integer> result = new ArrayList<>();
        if (low > high) return result;          // 無效範圍直接回傳空結果
        valuesBetween(root, low, high, result);
        return result;
    }

    private void valuesBetween(StatRangeNode node, int low, int high,
                               List<Integer> result) {
        if (node == null) return;
        visitedNodes++;
        if (low < node.value) {                 // 左側才可能有更小的值
            valuesBetween(node.left, low, high, result);
        }
        if (low <= node.value && node.value <= high) {
            result.add(node.value);
        }
        if (node.value < high) {                // 右側才可能有更大的值
            valuesBetween(node.right, low, high, result);
        }
    }

    int countBetween(int low, int high) {
        visitedNodes = 0;
        if (low > high) return 0;
        return countBetween(root, low, high);
    }

    private int countBetween(StatRangeNode node, int low, int high) {
        if (node == null) return 0;
        visitedNodes++;
        int total = 0;
        if (low < node.value) {
            total += countBetween(node.left, low, high);
        }
        if (low <= node.value && node.value <= high) {
            total++;
        }
        if (node.value < high) {
            total += countBetween(node.right, low, high);
        }
        return total;
    }

    int sumBetween(int low, int high) {
        visitedNodes = 0;
        if (low > high) return 0;
        return sumBetween(root, low, high);
    }

    private int sumBetween(StatRangeNode node, int low, int high) {
        if (node == null) return 0;
        visitedNodes++;
        int total = 0;
        if (low < node.value) {
            total += sumBetween(node.left, low, high);
        }
        if (low <= node.value && node.value <= high) {
            total += node.value;
        }
        if (node.value < high) {
            total += sumBetween(node.right, low, high);
        }
        return total;
    }

    int size() {
        return size(root);
    }

    private int size(StatRangeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }
}

public class BstRangeStatistics {

    private static void report(StatRangeBst tree, int low, int high) {
        System.out.println("range[" + low + "," + high + "]");

        List<Integer> values = tree.valuesBetween(low, high);
        System.out.println("  values=" + values
                + "  visited=" + tree.visitedNodes() + "/" + tree.size());

        int count = tree.countBetween(low, high);
        System.out.println("  count=" + count
                + "  visited=" + tree.visitedNodes() + "/" + tree.size());

        int sum = tree.sumBetween(low, high);
        System.out.println("  sum=" + sum
                + "  visited=" + tree.visitedNodes() + "/" + tree.size());

        System.out.println("  consistent=" + (values.size() == count));
    }

    public static void main(String[] args) {
        StatRangeBst tree = new StatRangeBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 65, 75}) {
            tree.add(value);
        }
        System.out.println("size=" + tree.size());
        System.out.println();

        report(tree, 35, 65);    // 一般範圍，含端點
        report(tree, 20, 80);    // 全部
        report(tree, 41, 49);    // 空範圍（範圍合法但沒有資料）
        report(tree, 90, 99);    // 空範圍（完全超出）
        report(tree, 65, 35);    // low > high

        System.out.println("[empty tree]");
        StatRangeBst empty = new StatRangeBst();
        report(empty, 10, 20);
    }
}
