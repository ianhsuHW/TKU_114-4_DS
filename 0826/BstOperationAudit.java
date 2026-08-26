// 課堂實作題三：BST Operation Audit
// 需求：每次 add 或 remove 後輸出 operation、result、inorder、size、height、valid。
//       資料必須包含 duplicate、missing 與三種 delete case。

import java.util.ArrayList;
import java.util.List;

class AuditNode {
    int value;
    AuditNode left;
    AuditNode right;

    AuditNode(int value) {
        this.value = value;
    }
}

class AuditBst {
    private AuditNode root;

    boolean add(int value) {
        if (root == null) {
            root = new AuditNode(value);
            return true;
        }
        AuditNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new AuditNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new AuditNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    String deleteCase(int target) {
        AuditNode node = findNode(target);
        if (node == null) return "MISSING";
        if (node.left == null && node.right == null) return "LEAF";
        if (node.left == null || node.right == null) return "ONE_CHILD";
        return "TWO_CHILDREN";
    }

    private AuditNode findNode(int target) {
        AuditNode current = root;
        while (current != null) {
            if (target == current.value) return current;
            current = target < current.value ? current.left : current.right;
        }
        return null;
    }

    boolean remove(int target) {
        if (findNode(target) == null) return false;
        root = remove(root, target);
        return true;
    }

    private AuditNode remove(AuditNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = remove(node.left, target);
        } else if (target > node.value) {
            node.right = remove(node.right, target);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            AuditNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private AuditNode minimumNode(AuditNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(AuditNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    int size() {
        return size(root);
    }

    private int size(AuditNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(AuditNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(AuditNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }
}

public class BstOperationAudit {

    private static final AuditBst TREE = new AuditBst();

    private static void audit(String operation, boolean result) {
        System.out.println(operation);
        System.out.println("  result=" + result);
        System.out.println("  inorder=" + TREE.inorder());
        System.out.println("  size=" + TREE.size());
        System.out.println("  height=" + TREE.height());
        System.out.println("  valid=" + TREE.isValid());
    }

    private static void auditAdd(int value) {
        audit("add(" + value + ")", TREE.add(value));
    }

    private static void auditRemove(int value) {
        String deleteCase = TREE.deleteCase(value);
        audit("remove(" + value + ") case=" + deleteCase, TREE.remove(value));
    }

    public static void main(String[] args) {
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80, 65}) {
            auditAdd(value);
        }

        auditAdd(40);       // duplicate
        auditAdd(65);       // duplicate

        auditRemove(999);   // MISSING
        auditRemove(20);    // LEAF
        auditRemove(60);    // ONE_CHILD（只剩 right child 65）
        auditRemove(50);    // TWO_CHILDREN（root）
        auditRemove(50);    // 已刪除，再次刪除為 MISSING
    }
}
