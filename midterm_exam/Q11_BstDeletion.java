// 第 11 題：BST 三種刪除
// 重點：leaf / one child / two children，two children 用 right subtree 的最小值當接班人。

import java.util.ArrayList;
import java.util.List;

public class Q11_BstDeletion {

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
        if (contains(value)) return false;      // duplicate 不加入
        root = addHelper(root, value);
        size++;
        return true;
    }

    private Node addHelper(Node node, int value) {
        if (node == null) return new Node(value);
        if (value < node.value) {
            node.left = addHelper(node.left, value);
        } else {
            node.right = addHelper(node.right, value);
        }
        return node;
    }

    public boolean remove(int value) {
        if (!contains(value)) return false;     // 找不到就不動 tree
        root = removeHelper(root, value);
        size--;
        return true;
    }

    private Node removeHelper(Node node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = removeHelper(node.left, value);
            return node;
        }
        if (value > node.value) {
            node.right = removeHelper(node.right, value);
            return node;
        }
        // 找到目標
        if (node.left == null && node.right == null) return null;      // case 1: leaf
        if (node.left == null) return node.right;                      // case 2: 只有右子
        if (node.right == null) return node.left;                      // case 2: 只有左子

        int successor = minValue(node.right);                          // case 3: inorder successor
        node.value = successor;
        node.right = removeHelper(node.right, successor);
        return node;
    }

    private int minValue(Node node) {
        Node current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
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
        if (low != null && node.value <= low) return false;
        if (high != null && node.value >= high) return false;
        return isValidHelper(node.left, low, node.value)
                && isValidHelper(node.right, node.value, high);
    }

    public static void main(String[] args) {
        Q11_BstDeletion tree = new Q11_BstDeletion();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) tree.add(value);
        System.out.println(tree.remove(20));
        System.out.println(tree.remove(30));
        System.out.println(tree.remove(50));
        System.out.println(tree.remove(999));
        System.out.println(tree.inorder());
        System.out.println(tree.size());
        System.out.println(tree.isValid());

        System.out.println("--- 邊界測試 ---");
        Q11_BstDeletion empty = new Q11_BstDeletion();
        System.out.println(empty.remove(1));
        System.out.println(empty.inorder() + " " + empty.size() + " " + empty.isValid());

        Q11_BstDeletion single = new Q11_BstDeletion();
        single.add(5);
        System.out.println(single.add(5));
        System.out.println(single.remove(5));            // 刪掉 root
        System.out.println(single.inorder() + " " + single.size() + " " + single.contains(5));

        Q11_BstDeletion full = new Q11_BstDeletion();
        for (int value : new int[]{50, 30, 70, 60, 65, 80}) full.add(value);
        System.out.println(full.remove(70));             // two children，successor = 80
        System.out.println(full.inorder() + " " + full.size() + " " + full.isValid());
        System.out.println(full.remove(50));             // root two children，successor = 60
        System.out.println(full.inorder() + " " + full.size() + " " + full.isValid());
    }
}
