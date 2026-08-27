// 除錯練習（8/26）
// 練習一：Height 的 Base Case 不一致
// 練習二：Delete 後遺失 Subtree
//
// md 沒有指定檔名，本檔把兩個練習的「錯誤版」與「修正版」並列執行。
// md 明確採用「Empty=-1、leaf=0，以 edge 數量表示 height」這一套定義。

import java.util.ArrayList;
import java.util.List;

class HeightDebugNode {
    int value;
    HeightDebugNode left;
    HeightDebugNode right;

    HeightDebugNode(int value) {
        this.value = value;
    }
}

public class HeightDeleteDebugPractice {

    // ---------- 除錯練習一：Height 的 Base Case 不一致 ----------

    // 錯誤：empty tree 回傳 0，卻仍用「1 + max(左, 右)」的 edge 公式，
    //       leaf 會被算成 1，整份程式的 height 全部多 1。
    static int brokenHeight(HeightDebugNode node) {
        if (node == null) return 0;
        return 1 + Math.max(brokenHeight(node.left), brokenHeight(node.right));
    }

    // 修正 A（md 採用）：empty=-1、leaf=0，height 等於 edge 數。
    static int edgeHeight(HeightDebugNode node) {
        if (node == null) return -1;
        return 1 + Math.max(edgeHeight(node.left), edgeHeight(node.right));
    }

    // 修正 B：empty=0、leaf=1，height 等於 node 數。
    // 兩套都自洽，但整份實作只能選一套，不可混用。
    static int nodeHeight(HeightDebugNode node) {
        if (node == null) return 0;
        return 1 + Math.max(nodeHeight(node.left), nodeHeight(node.right));
    }

    private static void reportHeight(String title, HeightDebugNode root,
                                     int expectedEdge) {
        System.out.println("  " + title);
        System.out.println("    broken(empty=0 + edge formula)=" + brokenHeight(root));
        System.out.println("    fixed A edge  (empty=-1, leaf=0)="
                + edgeHeight(root) + "  expected=" + expectedEdge
                + "  ok=" + (edgeHeight(root) == expectedEdge));
        System.out.println("    fixed B node  (empty=0,  leaf=1)="
                + nodeHeight(root));
    }

    private static void runExerciseOne() {
        System.out.println("[Debug 1] inconsistent base case for height");

        reportHeight("empty tree", null, -1);
        reportHeight("single leaf", new HeightDebugNode(10), 0);

        //      50
        //     /  \
        //   30    70
        //   /
        // 20
        HeightDebugNode root = new HeightDebugNode(50);
        root.left = new HeightDebugNode(30);
        root.right = new HeightDebugNode(70);
        root.left.left = new HeightDebugNode(20);
        reportHeight("four nodes", root, 2);

        System.out.println("  note: brokenHeight makes a leaf 1 and empty 0, "
                + "so the two are not one edge apart.");
        System.out.println("  note: fix B prints the same numbers as broken, "
                + "but B commits to the node-count definition everywhere;");
        System.out.println("        broken claims the edge definition "
                + "(leaf=0) yet keeps the empty=0 base case.");
        System.out.println();
    }

    // ---------- 除錯練習二：Delete 後遺失 Subtree ----------

    // 錯誤：one-child case 一律 return null，
    //       target 底下唯一的 child subtree 會整個從 root 斷開。
    static HeightDebugNode brokenRemove(HeightDebugNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = brokenRemove(node.left, target);
        } else if (target > node.value) {
            node.right = brokenRemove(node.right, target);
        } else {
            if (node.left == null || node.right == null) return null;
            HeightDebugNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = brokenRemove(node.right, successor.value);
        }
        return node;
    }

    // 修正：分別回傳存在的那一個 child，subtree 才不會斷線。
    static HeightDebugNode fixedRemove(HeightDebugNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = fixedRemove(node.left, target);
        } else if (target > node.value) {
            node.right = fixedRemove(node.right, target);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            HeightDebugNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = fixedRemove(node.right, successor.value);
        }
        return node;
    }

    private static HeightDebugNode minimumNode(HeightDebugNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private static HeightDebugNode build(int... values) {
        HeightDebugNode root = null;
        for (int value : values) {
            root = insert(root, value);
        }
        return root;
    }

    private static HeightDebugNode insert(HeightDebugNode node, int value) {
        if (node == null) return new HeightDebugNode(value);
        if (value < node.value) {
            node.left = insert(node.left, value);
        } else if (value > node.value) {
            node.right = insert(node.right, value);
        }
        return node;
    }

    private static List<Integer> inorder(HeightDebugNode node) {
        List<Integer> result = new ArrayList<>();
        inorder(node, result);
        return result;
    }

    private static void inorder(HeightDebugNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    private static void compareRemove(String title, int target,
                                      int[] values) {
        HeightDebugNode brokenTree = build(values);
        HeightDebugNode fixedTree = build(values);
        System.out.println("  " + title + ", remove(" + target + ")");
        System.out.println("    before=" + inorder(brokenTree));
        System.out.println("    broken=" + inorder(brokenRemove(brokenTree, target)));
        System.out.println("    fixed =" + inorder(fixedRemove(fixedTree, target)));
    }

    private static void runExerciseTwo() {
        System.out.println("[Debug 2] delete loses a subtree");
        System.out.println("  the notes require testing root and "
                + "internal node at least once each after the fix.");

        // internal node，只有 left child
        compareRemove("internal node (30 has only left child 20)", 30,
                new int[]{50, 30, 70, 20});

        // internal node，只有 right child
        compareRemove("internal node (30 has only right child 40)", 30,
                new int[]{50, 30, 70, 40});

        // root，只有一個 child
        compareRemove("root (50 has only a left subtree)", 50,
                new int[]{50, 30, 20, 40});

        System.out.println("  symptom: the broken version cuts off the whole "
                + "subtree under target, so inorder loses those keys.");
        System.out.println();
    }

    public static void main(String[] args) {
        runExerciseOne();
        runExerciseTwo();
    }
}
