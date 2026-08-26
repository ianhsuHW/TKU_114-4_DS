// 課堂實作題四：三種 DFS traversal
// 需求：對 M(F(B,null),T(R,Z)) 完成 preorder、inorder、postorder。
//       三個 method 都要處理 null，不得寫死結果字串。

class PracticeNode {
    String value;
    PracticeNode left;
    PracticeNode right;

    PracticeNode(String value) {
        this.value = value;
    }
}

public class ThreeTraversalPractice {

    static void preorder(PracticeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preorder(node.left);
        preorder(node.right);
    }

    static void inorder(PracticeNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    static void postorder(PracticeNode node) {
        if (node == null) {
            return;
        }
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.value + " ");
    }

    //        M
    //       / \
    //      F   T
    //     /   / \
    //    B   R   Z
    private static PracticeNode buildTree() {
        PracticeNode root = new PracticeNode("M");
        root.left = new PracticeNode("F");
        root.right = new PracticeNode("T");
        root.left.left = new PracticeNode("B");
        root.right.left = new PracticeNode("R");
        root.right.right = new PracticeNode("Z");
        return root;
    }

    public static void main(String[] args) {
        PracticeNode root = buildTree();

        System.out.print("preorder: ");
        preorder(root);
        System.out.println();

        System.out.print("inorder: ");
        inorder(root);
        System.out.println();

        System.out.print("postorder: ");
        postorder(root);
        System.out.println();

        System.out.print("empty preorder: ");
        preorder(null);
        System.out.print("| empty inorder: ");
        inorder(null);
        System.out.print("| empty postorder: ");
        postorder(null);
        System.out.println("| done");
    }
}
