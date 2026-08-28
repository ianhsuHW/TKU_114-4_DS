// 第 9 題：Binary Tree 四種走訪
// 重點：前三種用 recursion，level-order 用 Queue，每次呼叫都建立新的 result。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Q09_TreeTraversal {

    public static class Node {
        public int value;
        public Node left;
        public Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    public static List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }

    private static void preorderHelper(Node node, List<Integer> result) {
        if (node == null) return;
        result.add(node.value);                 // 根 -> 左 -> 右
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }

    public static List<Integer> inorder(Node root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private static void inorderHelper(Node node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);       // 左 -> 根 -> 右
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public static List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }

    private static void postorderHelper(Node node, List<Integer> result) {
        if (node == null) return;
        postorderHelper(node.left, result);     // 左 -> 右 -> 根
        postorderHelper(node.right, result);
        result.add(node.value);
    }

    public static List<Integer> levelOrder(Node root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            result.add(current.value);
            if (current.left != null) queue.offer(current.left);
            if (current.right != null) queue.offer(current.right);
        }
        return result;
    }

    public static void main(String[] args) {
        //         8
        //       /   \
        //      4     12
        //     / \      \
        //    2   6      14
        Node root = new Node(8);
        root.left = new Node(4);
        root.right = new Node(12);
        root.left.left = new Node(2);
        root.left.right = new Node(6);
        root.right.right = new Node(14);

        System.out.println("preorder  = " + preorder(root));
        System.out.println("inorder   = " + inorder(root));
        System.out.println("postorder = " + postorder(root));
        System.out.println("level     = " + levelOrder(root));

        System.out.println("--- 邊界測試 ---");
        System.out.println(preorder(null));
        System.out.println(inorder(null));
        System.out.println(postorder(null));
        System.out.println(levelOrder(null));

        System.out.println(preorder(new Node(1)));          // 只有 root
        System.out.println("再呼叫一次不殘留：" + preorder(root));

        Node skewed = new Node(1);                          // 右斜樹
        skewed.right = new Node(2);
        skewed.right.right = new Node(3);
        System.out.println(inorder(skewed) + " " + levelOrder(skewed));
    }
}
