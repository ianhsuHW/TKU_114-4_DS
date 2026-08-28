// 第 10 題：BST 搜尋與插入
// 重點：searchPath 收集比較過的 node，isValid 用 ancestor 的 low/high 邊界。

import java.util.ArrayList;
import java.util.List;

public class Q10_BstDirectory {

    private static class Node {
        int value;
        Node left;
        Node right;

        Node(int value) {
            this.value = value;
        }
    }

    private Node root;
    private int size;

    public boolean add(int value) {
        if (root == null) {
            root = new Node(value);
            size++;
            return true;
        }
        Node current = root;
        while (true) {
            if (value == current.value) return false;       // duplicate 不加入
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new Node(value);
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(value);
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    public boolean contains(int value) {
        Node current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public List<Integer> searchPath(int target) {
        List<Integer> path = new ArrayList<>();
        Node current = root;
        while (current != null) {
            path.add(current.value);                        // 只記錄實際比較過的 node
            if (target == current.value) break;
            current = target < current.value ? current.left : current.right;
        }
        return path;
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.value);
        inorderHelper(node.right, result);
    }

    public boolean isValid() {
        return isValidHelper(root, null, null);
    }

    private boolean isValidHelper(Node node, Integer low, Integer high) {
        if (node == null) return true;
        if (low != null && node.value <= low) return false;      // 必須大於所有左祖先邊界
        if (high != null && node.value >= high) return false;    // 必須小於所有右祖先邊界
        return isValidHelper(node.left, low, node.value)
                && isValidHelper(node.right, node.value, high);
    }

    public static void main(String[] args) {
        Q10_BstDirectory tree = new Q10_BstDirectory();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) tree.add(value);
        System.out.println(tree.add(40));
        System.out.println(tree.searchPath(60));
        System.out.println(tree.searchPath(65));
        System.out.println(tree.inorder());
        System.out.println(tree.isValid());

        System.out.println("--- 邊界測試 ---");
        Q10_BstDirectory empty = new Q10_BstDirectory();
        System.out.println(empty.inorder());
        System.out.println(empty.searchPath(1));
        System.out.println(empty.contains(1));
        System.out.println(empty.size());
        System.out.println(empty.isValid());

        System.out.println(tree.size());
        System.out.println(tree.contains(20) + " " + tree.contains(21));
        System.out.println(tree.searchPath(50));
        System.out.println(tree.searchPath(10));

        Q10_BstDirectory negative = new Q10_BstDirectory();
        for (int value : new int[]{0, -10, 10, -20}) negative.add(value);
        System.out.println(negative.inorder() + " " + negative.isValid());
    }
}
