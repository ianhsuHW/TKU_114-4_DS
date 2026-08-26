// 課堂實作題三：Min、Max 與範圍
// 需求：完成 min、max 與 printRange(low, high)。
//       範圍包含端點，並處理 low > high。

class RangeNode {
    int value;
    RangeNode left;
    RangeNode right;

    RangeNode(int value) {
        this.value = value;
    }
}

class RangeBst {
    private RangeNode root;

    boolean add(int value) {
        if (root == null) {
            root = new RangeNode(value);
            return true;
        }
        RangeNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new RangeNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new RangeNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Integer min() {
        if (root == null) return null;
        RangeNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer max() {
        if (root == null) return null;
        RangeNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    // low > high 視為無效範圍，直接輸出空結果而不交換參數
    void printRange(int low, int high) {
        System.out.print("range[" + low + "," + high + "]= ");
        if (low > high) {
            System.out.println("(invalid range, empty)");
            return;
        }
        printRange(root, low, high);
        System.out.println();
    }

    private void printRange(RangeNode node, int low, int high) {
        if (node == null) return;
        if (low < node.value) {
            printRange(node.left, low, high);       // 只有可能更小才往左
        }
        if (low <= node.value && node.value <= high) {
            System.out.print(node.value + " ");     // 端點包含在內
        }
        if (node.value < high) {
            printRange(node.right, low, high);      // 只有可能更大才往右
        }
    }

    void inorder() {
        System.out.print("inorder= ");
        inorder(root);
        System.out.println();
    }

    private void inorder(RangeNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }
}

public class BstRangeReport {
    public static void main(String[] args) {
        RangeBst tree = new RangeBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 35, 65}) {
            tree.add(value);
        }

        tree.inorder();
        System.out.println("min=" + tree.min());
        System.out.println("max=" + tree.max());

        tree.printRange(35, 65);    // 含端點
        tree.printRange(20, 80);    // 全部
        tree.printRange(41, 49);    // 範圍內沒有資料
        tree.printRange(70, 35);    // low > high

        System.out.println("[empty tree]");
        RangeBst empty = new RangeBst();
        System.out.println("min=" + empty.min());
        System.out.println("max=" + empty.max());
        empty.printRange(10, 20);
    }
}
