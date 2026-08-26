// 課堂實作題一：BST Search Trace
// 需求：輸出每次比較的 current value、方向與 comparison count，
//       測試找到 root、leaf、internal node 與 missing value。

class TraceNode {
    int value;
    TraceNode left;
    TraceNode right;

    TraceNode(int value) {
        this.value = value;
    }
}

class TraceBst {
    private TraceNode root;

    boolean add(int value) {
        if (root == null) {
            root = new TraceNode(value);
            return true;
        }
        TraceNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new TraceNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TraceNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // 逐步輸出比較過程，回傳實際比較次數
    int searchWithTrace(int target) {
        System.out.println("search " + target);
        TraceNode current = root;
        int comparisons = 0;

        while (current != null) {
            comparisons++;
            System.out.print("  compare " + target + " with " + current.value);
            if (target == current.value) {
                System.out.println(" -> FOUND");
                System.out.println("  comparisons=" + comparisons);
                return comparisons;
            }
            if (target < current.value) {
                System.out.println(" -> go left");
                current = current.left;
            } else {
                System.out.println(" -> go right");
                current = current.right;
            }
        }

        System.out.println("  reached null -> NOT FOUND");
        System.out.println("  comparisons=" + comparisons);
        return comparisons;
    }
}

public class BstSearchTrace {
    public static void main(String[] args) {
        TraceBst tree = new TraceBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }

        tree.searchWithTrace(50);   // root
        tree.searchWithTrace(20);   // leaf
        tree.searchWithTrace(70);   // internal node
        tree.searchWithTrace(65);   // missing value

        System.out.println("[empty tree]");
        new TraceBst().searchWithTrace(10);
    }
}
