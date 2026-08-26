// 課堂實作題五：Tree Bug Lab
// 需求：為下列四種錯誤各建立最小失敗案例，再完成修正：
//       1. search 方向相反
//       2. inorder 順序錯誤
//       3. delete 遺失 child
//       4. validation 只檢查直接 child

import java.util.ArrayList;
import java.util.List;

class BugNode {
    int value;
    BugNode left;
    BugNode right;

    BugNode(int value) {
        this.value = value;
    }
}

public class TreeBugLab {

    // ---------- Bug 1：search 方向相反 ----------

    static boolean brokenContains(BugNode node, int target) {
        BugNode current = node;
        while (current != null) {
            if (target == current.value) return true;
            // 錯誤：較小的 target 應該往 left，這裡卻往 right
            current = target < current.value ? current.right : current.left;
        }
        return false;
    }

    static boolean fixedContains(BugNode node, int target) {
        BugNode current = node;
        while (current != null) {
            if (target == current.value) return true;
            current = target < current.value ? current.left : current.right;
        }
        return false;
    }

    // ---------- Bug 2：inorder 順序錯誤 ----------

    static void brokenInorder(BugNode node, List<Integer> result) {
        if (node == null) return;
        // 錯誤：root 在 left subtree 之前被加入，實際上是 preorder
        result.add(node.value);
        brokenInorder(node.left, result);
        brokenInorder(node.right, result);
    }

    static void fixedInorder(BugNode node, List<Integer> result) {
        if (node == null) return;
        fixedInorder(node.left, result);
        result.add(node.value);
        fixedInorder(node.right, result);
    }

    // ---------- Bug 3：delete 遺失 child ----------

    static BugNode brokenRemove(BugNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = brokenRemove(node.left, target);
        } else if (target > node.value) {
            node.right = brokenRemove(node.right, target);
        } else {
            // 錯誤：只有一個 child 時也回傳 null，整個 child subtree 被切斷
            if (node.left == null || node.right == null) return null;
            BugNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = brokenRemove(node.right, successor.value);
        }
        return node;
    }

    static BugNode fixedRemove(BugNode node, int target) {
        if (node == null) return null;
        if (target < node.value) {
            node.left = fixedRemove(node.left, target);
        } else if (target > node.value) {
            node.right = fixedRemove(node.right, target);
        } else {
            if (node.left == null) return node.right;    // 回傳 non-null child
            if (node.right == null) return node.left;
            BugNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = fixedRemove(node.right, successor.value);
        }
        return node;
    }

    private static BugNode minimumNode(BugNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // ---------- Bug 4：validation 只檢查直接 child ----------

    static boolean brokenIsValid(BugNode node) {
        if (node == null) return true;
        // 錯誤：只比較 parent 與直接 child，看不到 ancestor 的範圍限制
        if (node.left != null && node.left.value >= node.value) return false;
        if (node.right != null && node.right.value <= node.value) return false;
        return brokenIsValid(node.left) && brokenIsValid(node.right);
    }

    static boolean fixedIsValid(BugNode node) {
        return fixedIsValid(node, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean fixedIsValid(BugNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return fixedIsValid(node.left, low, node.value)
                && fixedIsValid(node.right, node.value, high);
    }

    // ---------- 最小失敗案例 ----------

    private static List<Integer> collect(BugNode root, boolean useFixed) {
        List<Integer> result = new ArrayList<>();
        if (useFixed) {
            fixedInorder(root, result);
        } else {
            brokenInorder(root, result);
        }
        return result;
    }

    private static void section(String title) {
        System.out.println("[" + title + "]");
    }

    public static void main(String[] args) {
        // Bug 1 最小案例：兩個 node，target 在 left
        section("Bug 1: search direction reversed");
        BugNode searchTree = new BugNode(50);
        searchTree.left = new BugNode(30);
        System.out.println("  tree = 50 with left 30, search 30");
        System.out.println("  broken=" + brokenContains(searchTree, 30));
        System.out.println("  fixed =" + fixedContains(searchTree, 30));
        System.out.println();

        // Bug 2 最小案例：三個 node 就能分辨 preorder 與 inorder
        section("Bug 2: inorder order wrong");
        BugNode orderTree = new BugNode(2);
        orderTree.left = new BugNode(1);
        orderTree.right = new BugNode(3);
        System.out.println("  tree = 2 with left 1, right 3");
        System.out.println("  broken=" + collect(orderTree, false));
        System.out.println("  fixed =" + collect(orderTree, true));
        System.out.println();

        // Bug 3 最小案例：刪除只有一個 child 的 node
        section("Bug 3: delete loses child subtree");
        BugNode brokenTree = new BugNode(50);
        brokenTree.left = new BugNode(30);
        brokenTree.left.left = new BugNode(20);
        BugNode fixedTree = new BugNode(50);
        fixedTree.left = new BugNode(30);
        fixedTree.left.left = new BugNode(20);
        System.out.println("  tree = 50 -> left 30 -> left 20, remove 30");
        System.out.println("  broken="
                + collect(brokenRemove(brokenTree, 30), true));
        System.out.println("  fixed ="
                + collect(fixedRemove(fixedTree, 30), true));
        System.out.println();

        // Bug 4 最小案例：深層 node 違反 ancestor 上限
        section("Bug 4: validation checks direct child only");
        BugNode validationTree = new BugNode(50);
        validationTree.left = new BugNode(30);
        validationTree.left.right = new BugNode(55);
        System.out.println("  tree = 50 -> left 30 -> right 55");
        System.out.println("  broken=" + brokenIsValid(validationTree));
        System.out.println("  fixed =" + fixedIsValid(validationTree));
    }
}
