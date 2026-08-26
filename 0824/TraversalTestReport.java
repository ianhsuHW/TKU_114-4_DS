// 課後作業六：Traversal 測試報告
// 需求：建立 empty、single-node、only-left、only-right、complete 與 irregular tree，
//       輸出四種 traversal 的預期與實際結果及是否相同。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class TestTreeNode {
    String value;
    TestTreeNode left;
    TestTreeNode right;

    TestTreeNode(String value) {
        this.value = value;
    }
}

public class TraversalTestReport {

    static List<String> preorder(TestTreeNode node) {
        List<String> result = new ArrayList<>();
        preorder(node, result);
        return result;
    }

    private static void preorder(TestTreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        result.add(node.value);
        preorder(node.left, result);
        preorder(node.right, result);
    }

    static List<String> inorder(TestTreeNode node) {
        List<String> result = new ArrayList<>();
        inorder(node, result);
        return result;
    }

    private static void inorder(TestTreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    static List<String> postorder(TestTreeNode node) {
        List<String> result = new ArrayList<>();
        postorder(node, result);
        return result;
    }

    private static void postorder(TestTreeNode node, List<String> result) {
        if (node == null) {
            return;
        }
        postorder(node.left, result);
        postorder(node.right, result);
        result.add(node.value);
    }

    static List<String> levelOrder(TestTreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        Queue<TestTreeNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TestTreeNode current = queue.poll();
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

    private static String join(List<String> values) {
        return String.join(" ", values);
    }

    private static void check(String label, String expected, String actual) {
        System.out.println("  " + label);
        System.out.println("    expected=[" + expected + "]");
        System.out.println("    actual  =[" + actual + "]");
        System.out.println("    same=" + expected.equals(actual));
    }

    private static void runCase(String title, TestTreeNode root,
                                String expectedPre, String expectedIn,
                                String expectedPost, String expectedLevel) {
        System.out.println("[" + title + "]");
        check("preorder", expectedPre, join(preorder(root)));
        check("inorder", expectedIn, join(inorder(root)));
        check("postorder", expectedPost, join(postorder(root)));
        check("levelOrder", expectedLevel, join(levelOrder(root)));
        System.out.println();
    }

    private static TestTreeNode onlyLeft() {
        TestTreeNode root = new TestTreeNode("A");
        root.left = new TestTreeNode("B");
        root.left.left = new TestTreeNode("C");
        return root;
    }

    private static TestTreeNode onlyRight() {
        TestTreeNode root = new TestTreeNode("A");
        root.right = new TestTreeNode("B");
        root.right.right = new TestTreeNode("C");
        return root;
    }

    private static TestTreeNode complete() {
        TestTreeNode root = new TestTreeNode("A");
        root.left = new TestTreeNode("B");
        root.right = new TestTreeNode("C");
        root.left.left = new TestTreeNode("D");
        root.left.right = new TestTreeNode("E");
        root.right.left = new TestTreeNode("F");
        root.right.right = new TestTreeNode("G");
        return root;
    }

    //        A
    //       / \
    //      B   C
    //       \   \
    //        D   E
    //           /
    //          F
    private static TestTreeNode irregular() {
        TestTreeNode root = new TestTreeNode("A");
        root.left = new TestTreeNode("B");
        root.right = new TestTreeNode("C");
        root.left.right = new TestTreeNode("D");
        root.right.right = new TestTreeNode("E");
        root.right.right.left = new TestTreeNode("F");
        return root;
    }

    public static void main(String[] args) {
        runCase("empty", null, "", "", "", "");
        runCase("single node", new TestTreeNode("A"), "A", "A", "A", "A");
        runCase("only left", onlyLeft(),
                "A B C", "C B A", "C B A", "A B C");
        runCase("only right", onlyRight(),
                "A B C", "A B C", "C B A", "A B C");
        runCase("complete", complete(),
                "A B D E C F G", "D B E A F C G", "D E B F G C A",
                "A B C D E F G");
        runCase("irregular", irregular(),
                "A B D C E F", "B D A C F E", "D B F E C A",
                "A B C D E F");
    }
}
