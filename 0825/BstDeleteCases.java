// 課堂實作題四：三種 Delete Case
// 需求：依序刪除 leaf、single-child、two-child node，
//       每次輸出 inorder、size 與 valid result。

class CaseNode {
    int value;
    CaseNode left;
    CaseNode right;

    CaseNode(int value) {
        this.value = value;
    }
}

class CaseBst {
    private CaseNode root;

    boolean add(int value) {
        if (root == null) {
            root = new CaseNode(value);
            return true;
        }
        CaseNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new CaseNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CaseNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // 刪除當下的 shape 才決定 case
    String deleteCase(int value) {
        CaseNode node = findNode(value);
        if (node == null) return "MISSING";
        if (node.left == null && node.right == null) return "LEAF";
        if (node.left == null || node.right == null) return "ONE_CHILD";
        return "TWO_CHILDREN";
    }

    private CaseNode findNode(int value) {
        CaseNode current = root;
        while (current != null) {
            if (value == current.value) return current;
            current = value < current.value ? current.left : current.right;
        }
        return null;
    }

    boolean remove(int value) {
        if (findNode(value) == null) return false;
        root = remove(root, value);
        return true;
    }

    private CaseNode remove(CaseNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            CaseNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private CaseNode minimumNode(CaseNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    int size() {
        return size(root);
    }

    private int size(CaseNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(CaseNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }

    String inorder() {
        StringBuilder text = new StringBuilder();
        inorder(root, text);
        return text.toString().trim();
    }

    private void inorder(CaseNode node, StringBuilder text) {
        if (node == null) return;
        inorder(node.left, text);
        text.append(node.value).append(" ");
        inorder(node.right, text);
    }
}

public class BstDeleteCases {

    private static void removeAndReport(CaseBst tree, int value) {
        System.out.println("remove " + value + " (case=" + tree.deleteCase(value) + ")");
        System.out.println("  result=" + tree.remove(value));
        System.out.println("  inorder=" + tree.inorder());
        System.out.println("  size=" + tree.size());
        System.out.println("  valid=" + tree.isValid());
    }

    public static void main(String[] args) {
        CaseBst tree = new CaseBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 65}) {
            tree.add(value);
        }
        System.out.println("start inorder=" + tree.inorder());
        System.out.println("start size=" + tree.size());
        System.out.println();

        removeAndReport(tree, 20);   // LEAF
        removeAndReport(tree, 60);   // ONE_CHILD（只剩 right child 65）
        removeAndReport(tree, 50);   // TWO_CHILDREN（root）
        removeAndReport(tree, 999);  // MISSING
    }
}
