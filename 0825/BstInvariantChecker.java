// 課堂實作題六：BST Validation
// 需求：建立 valid tree 與至少三棵深層違規 tree，
//       使用 min/max boundary 驗證。

class CheckNode {
    int value;
    CheckNode left;
    CheckNode right;

    CheckNode(int value) {
        this.value = value;
    }
}

public class BstInvariantChecker {

    // 正確作法：把 ancestor 傳下來的 boundary 一起檢查
    static boolean isValid(CheckNode root) {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private static boolean isValid(CheckNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }

    // 錯誤作法：只比較 parent 與直接 child，看不到深層違規
    static boolean parentChildOnly(CheckNode node) {
        if (node == null) return true;
        if (node.left != null && node.left.value >= node.value) return false;
        if (node.right != null && node.right.value <= node.value) return false;
        return parentChildOnly(node.left) && parentChildOnly(node.right);
    }

    private static void inorder(CheckNode node, StringBuilder text) {
        if (node == null) return;
        inorder(node.left, text);
        text.append(node.value).append(" ");
        inorder(node.right, text);
    }

    private static void report(String title, CheckNode root) {
        StringBuilder text = new StringBuilder();
        inorder(root, text);
        System.out.println("[" + title + "]");
        System.out.println("  inorder=" + text.toString().trim());
        System.out.println("  parentChildOnly=" + parentChildOnly(root));
        System.out.println("  isValid(min/max boundary)=" + isValid(root));
        System.out.println();
    }

    //        50
    //       /  \
    //     30    70
    //    / \    / \
    //  20  40 60  80
    private static CheckNode validTree() {
        CheckNode root = new CheckNode(50);
        root.left = new CheckNode(30);
        root.right = new CheckNode(70);
        root.left.left = new CheckNode(20);
        root.left.right = new CheckNode(40);
        root.right.left = new CheckNode(60);
        root.right.right = new CheckNode(80);
        return root;
    }

    // 違規一：55 在 root 的 left subtree，卻大於 root
    private static CheckNode violationAboveRoot() {
        CheckNode root = new CheckNode(50);
        root.left = new CheckNode(30);
        root.right = new CheckNode(70);
        root.left.right = new CheckNode(55);   // 55 > 30 但 55 > 50
        return root;
    }

    // 違規二：45 在 root 的 right subtree，卻小於 root
    private static CheckNode violationBelowRoot() {
        CheckNode root = new CheckNode(50);
        root.left = new CheckNode(30);
        root.right = new CheckNode(70);
        root.right.left = new CheckNode(45);   // 45 < 70 但 45 < 50
        return root;
    }

    // 違規三：更深層，35 違反 ancestor 30 的上限
    private static CheckNode violationDeepAncestor() {
        CheckNode root = new CheckNode(50);
        root.left = new CheckNode(30);
        root.right = new CheckNode(70);
        root.left.left = new CheckNode(20);
        root.left.left.right = new CheckNode(35);  // 35 > 20 但 35 > 30
        return root;
    }

    public static void main(String[] args) {
        report("valid tree", validTree());
        report("violation 1: 55 in left subtree of 50", violationAboveRoot());
        report("violation 2: 45 in right subtree of 50", violationBelowRoot());
        report("violation 3: 35 breaks ancestor 30", violationDeepAncestor());
        report("empty tree", null);
    }
}
