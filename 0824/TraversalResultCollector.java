// 課後作業三：Traversal 結果集合
// 需求：讓四種 traversal 回傳 List<String>，不直接輸出。
//       測試 empty、single-node、left-skewed 與 complete tree。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class CollectorNode {
    String value;
    CollectorNode left;
    CollectorNode right;

    CollectorNode(String value) {
        this.value = value;
    }
}

public class TraversalResultCollector {

    static List<String> preorder(CollectorNode root) {
        List<String> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private static void preorder(CollectorNode node, List<String> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    static List<String> inorder(CollectorNode root) {
        List<String> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private static void inorder(CollectorNode node, List<String> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    static List<String> postorder(CollectorNode root) {
        List<String> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private static void postorder(CollectorNode node, List<String> result) {
        if (node == null) {
            return;
        }
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.value);
    }

    static List<String> levelOrder(CollectorNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<CollectorNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            CollectorNode current = queue.poll();
            result.add(current.value);
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
        return result;
    }

    private static void report(String title, CollectorNode root) {
        System.out.println("[" + title + "]");
        System.out.println("preorder=" + preorder(root));
        System.out.println("inorder=" + inorder(root));
        System.out.println("postorder=" + postorder(root));
        System.out.println("levelOrder=" + levelOrder(root));
        System.out.println();
    }

    private static CollectorNode leftSkewed() {
        CollectorNode root = new CollectorNode("A");
        root.left = new CollectorNode("B");
        root.left.left = new CollectorNode("C");
        root.left.left.left = new CollectorNode("D");
        return root;
    }

    private static CollectorNode completeTree() {
        CollectorNode root = new CollectorNode("A");
        root.left = new CollectorNode("B");
        root.right = new CollectorNode("C");
        root.left.left = new CollectorNode("D");
        root.left.right = new CollectorNode("E");
        root.right.left = new CollectorNode("F");
        root.right.right = new CollectorNode("G");
        return root;
    }

    public static void main(String[] args) {
        report("empty", null);
        report("single node", new CollectorNode("A"));
        report("left skewed", leftSkewed());
        report("complete", completeTree());
    }
}
