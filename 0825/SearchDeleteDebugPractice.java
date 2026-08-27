// 除錯練習（8/25）
// 練習一：Search 方向寫反
// 練習二：Two-child delete 只複製 successor
//
// md 沒有指定檔名，本檔把兩個練習的「錯誤版」與「修正版」並列執行，
// 讓錯誤症狀可以實際重現。

import java.util.ArrayList;
import java.util.List;

class DebugBstNode {
    int value;
    DebugBstNode left;
    DebugBstNode right;

    DebugBstNode(int value) {
        this.value = value;
    }
}

class DebugBst {
    DebugBstNode root;

    boolean add(int value) {
        if (root == null) {
            root = new DebugBstNode(value);
            return true;
        }
        DebugBstNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new DebugBstNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new DebugBstNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    // ---------- 除錯練習一：Search 方向寫反 ----------

    // 錯誤：target < current.value 時應該往 left，這裡卻移到 right，
    //       每一步都離 target 越來越遠。
    boolean brokenContains(int target) {
        DebugBstNode current = root;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.right : current.left;
        }
        return false;
    }

    // 修正：較小往 left，較大往 right。
    boolean fixedContains(int target) {
        DebugBstNode current = root;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    // ---------- 除錯練習二：Two-child delete 只複製 successor ----------

    // 錯誤：把 target value 換成 successor value 之後就結束，
    //       right subtree 裡原本的 successor node 沒有被刪除，
    //       結果 tree 出現兩個相同 key。
    boolean brokenRemove(int value) {
        if (!fixedContains(value)) return false;
        root = brokenRemove(root, value);
        return true;
    }

    private DebugBstNode brokenRemove(DebugBstNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = brokenRemove(node.left, value);
        } else if (value > node.value) {
            node.right = brokenRemove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            DebugBstNode successor = minimumNode(node.right);
            node.value = successor.value;
            // 少了這一行：node.right = remove(node.right, successor.value);
        }
        return node;
    }

    // 修正：複製 successor value 之後，必須再從 right subtree 刪掉原 successor。
    boolean fixedRemove(int value) {
        if (!fixedContains(value)) return false;
        root = fixedRemove(root, value);
        return true;
    }

    private DebugBstNode fixedRemove(DebugBstNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = fixedRemove(node.left, value);
        } else if (value > node.value) {
            node.right = fixedRemove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            DebugBstNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = fixedRemove(node.right, successor.value);
        }
        return node;
    }

    private DebugBstNode minimumNode(DebugBstNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---------- 觀察用 ----------

    List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(DebugBstNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    int size() {
        return size(root);
    }

    private int size(DebugBstNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(DebugBstNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }
}

public class SearchDeleteDebugPractice {

    private static DebugBst standardTree() {
        DebugBst tree = new DebugBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        return tree;
    }

    private static void runExerciseOne() {
        System.out.println("[Debug 1] search direction reversed");
        DebugBst tree = standardTree();
        System.out.println("  tree=" + tree.inorder());

        for (int target : new int[]{20, 40, 60, 80, 65}) {
            System.out.println("  target=" + target
                    + "  broken=" + tree.brokenContains(target)
                    + "  fixed=" + tree.fixedContains(target));
        }
        System.out.println("  note: 20 lives in the left subtree of 50; "
                + "the reversed version walks right and never finds it.");
        System.out.println();
    }

    private static void runExerciseTwo() {
        System.out.println("[Debug 2] two-child delete only copies the successor");

        DebugBst broken = standardTree();
        DebugBst fixed = standardTree();
        System.out.println("  before=" + broken.inorder()
                + " size=" + broken.size());

        broken.brokenRemove(50);
        fixed.fixedRemove(50);

        System.out.println("  broken remove(50)");
        System.out.println("    inorder=" + broken.inorder());
        System.out.println("    size=" + broken.size()
                + "  valid=" + broken.isValid());
        System.out.println("    symptom: 60 appears twice, size did not drop, "
                + "invariant broken.");

        System.out.println("  fixed remove(50)");
        System.out.println("    inorder=" + fixed.inorder());
        System.out.println("    size=" + fixed.size()
                + "  valid=" + fixed.isValid());
        System.out.println();
    }

    public static void main(String[] args) {
        runExerciseOne();
        runExerciseTwo();
    }
}
