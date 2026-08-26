// 課堂實作題六：樹狀選單搜尋
// 需求：完成 contains、findDepth、countLeaves 與 preorder display。
//       找不到時 findDepth 回傳 -1。

class MenuNode {
    String title;
    MenuNode left;
    MenuNode right;

    MenuNode(String title) {
        this.title = title;
    }
}

public class MenuTreeSearch {

    static boolean contains(MenuNode node, String target) {
        if (node == null || target == null) {
            return false;
        }
        return node.title.equals(target)
                || contains(node.left, target)
                || contains(node.right, target);
    }

    // 找不到回傳 -1；root 的 depth 為 0
    static int findDepth(MenuNode node, String target) {
        if (node == null || target == null) {
            return -1;
        }
        if (node.title.equals(target)) {
            return 0;
        }
        int leftDepth = findDepth(node.left, target);
        if (leftDepth >= 0) {
            return leftDepth + 1;
        }
        int rightDepth = findDepth(node.right, target);
        if (rightDepth >= 0) {
            return rightDepth + 1;
        }
        return -1;
    }

    static int countLeaves(MenuNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return countLeaves(node.left) + countLeaves(node.right);
    }

    static void display(MenuNode node, int depth) {
        if (node == null) {
            return;
        }
        System.out.println("  ".repeat(depth) + node.title);
        display(node.left, depth + 1);
        display(node.right, depth + 1);
    }

    //            Main
    //           /    \
    //      Product    Support
    //       /   \        \
    //   Phone  Laptop    Repair
    //                     /
    //                  Warranty
    private static MenuNode buildMenu() {
        MenuNode root = new MenuNode("Main");
        root.left = new MenuNode("Product");
        root.right = new MenuNode("Support");
        root.left.left = new MenuNode("Phone");
        root.left.right = new MenuNode("Laptop");
        root.right.right = new MenuNode("Repair");
        root.right.right.left = new MenuNode("Warranty");
        return root;
    }

    public static void main(String[] args) {
        MenuNode root = buildMenu();

        System.out.println("[preorder display]");
        display(root, 0);

        System.out.println("contains(Laptop)=" + contains(root, "Laptop"));
        System.out.println("contains(Refund)=" + contains(root, "Refund"));
        System.out.println("depth(Main)=" + findDepth(root, "Main"));
        System.out.println("depth(Warranty)=" + findDepth(root, "Warranty"));
        System.out.println("depth(Refund)=" + findDepth(root, "Refund"));
        System.out.println("countLeaves=" + countLeaves(root));

        System.out.println("empty contains=" + contains(null, "Main"));
        System.out.println("empty depth=" + findDepth(null, "Main"));
        System.out.println("empty leaves=" + countLeaves(null));
    }
}
