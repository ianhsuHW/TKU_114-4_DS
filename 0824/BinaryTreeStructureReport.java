// 課堂實作題三：Binary Tree 結構報表
// 需求：建立至少 7 個 node，輸出 root、所有 leaf、size、leaf count 與 height，
//       另外測試 empty tree 與 single-node tree。

class StructureNode {
    String value;
    StructureNode left;
    StructureNode right;

    StructureNode(String value) {
        this.value = value;
    }
}

public class BinaryTreeStructureReport {

    static int size(StructureNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + size(node.left) + size(node.right);
    }

    static int leafCount(StructureNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leafCount(node.left) + leafCount(node.right);
    }

    // empty tree = -1、leaf = 0
    static int height(StructureNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    static void printLeaves(StructureNode node) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            System.out.print(node.value + " ");
            return;
        }
        printLeaves(node.left);
        printLeaves(node.right);
    }

    static void report(String title, StructureNode root) {
        System.out.println("[" + title + "]");
        System.out.println("root=" + (root == null ? "null" : root.value));
        System.out.print("leaves=");
        printLeaves(root);
        System.out.println();
        System.out.println("size=" + size(root));
        System.out.println("leafCount=" + leafCount(root));
        System.out.println("height=" + height(root));
        System.out.println();
    }

    private static StructureNode buildTree() {
        StructureNode root = new StructureNode("A");
        root.left = new StructureNode("B");
        root.right = new StructureNode("C");
        root.left.left = new StructureNode("D");
        root.left.right = new StructureNode("E");
        root.right.right = new StructureNode("F");
        root.left.left.left = new StructureNode("G");
        root.right.right.right = new StructureNode("H");
        return root;
    }

    public static void main(String[] args) {
        report("eight nodes", buildTree());
        report("empty tree", null);
        report("single node", new StructureNode("X"));
    }
}
