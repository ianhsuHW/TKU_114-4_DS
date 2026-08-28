// 第 12 題：學籍 BST 綜合系統
// 重點：Node 存 Student object，以 id 當 key，整合 add/find/update/remove/range query。

import java.util.ArrayList;
import java.util.List;

public class Q12_StudentBstSystem {

    public static class Student {
        private final int id;
        private final String name;
        private int score;

        public Student(int id, String name, int score) {
            if (id <= 0) {
                throw new IllegalArgumentException("id 必須大於 0");
            }
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("name 不可為 null 或空字串");
            }
            this.id = id;
            this.name = name.trim();
            this.score = clampScore(score);
        }

        private static int clampScore(int score) {
            return Math.max(0, Math.min(100, score));       // 0 ~ 100
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }

        void setScore(int score) {                          // package-private，只給系統更新
            this.score = clampScore(score);
        }

        @Override
        public String toString() {
            return id + "|" + name + "|" + score;
        }
    }

    private static class Node {
        Student data;
        Node left;
        Node right;

        Node(Student data) {
            this.data = data;
        }
    }

    private Node root;

    public boolean add(Student student) {
        if (student == null) return false;
        if (root == null) {
            root = new Node(student);
            return true;
        }
        Node current = root;
        while (true) {
            if (student.getId() == current.data.getId()) return false;   // id 唯一
            if (student.getId() < current.data.getId()) {
                if (current.left == null) {
                    current.left = new Node(student);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new Node(student);
                    return true;
                }
                current = current.right;
            }
        }
    }

    public Student find(int id) {
        Node current = root;
        while (current != null) {
            if (id == current.data.getId()) return current.data;
            current = id < current.data.getId() ? current.left : current.right;
        }
        return null;
    }

    public boolean updateScore(int id, int score) {
        Student student = find(id);
        if (student == null) return false;
        student.setScore(score);
        return true;
    }

    public boolean remove(int id) {
        if (find(id) == null) return false;
        root = removeHelper(root, id);
        return true;
    }

    private Node removeHelper(Node node, int id) {
        if (node == null) return null;
        if (id < node.data.getId()) {
            node.left = removeHelper(node.left, id);
            return node;
        }
        if (id > node.data.getId()) {
            node.right = removeHelper(node.right, id);
            return node;
        }
        if (node.left == null && node.right == null) return null;   // leaf
        if (node.left == null) return node.right;                   // 只有右子
        if (node.right == null) return node.left;                   // 只有左子

        Node successor = node.right;                                // right subtree minimum
        while (successor.left != null) {
            successor = successor.left;
        }
        node.data = successor.data;
        node.right = removeHelper(node.right, successor.data.getId());
        return node;
    }

    public List<Student> studentsBetween(int lowId, int highId) {
        List<Student> result = new ArrayList<>();
        if (lowId > highId) return result;
        rangeHelper(root, lowId, highId, result);
        return result;
    }

    private void rangeHelper(Node node, int lowId, int highId, List<Student> result) {
        if (node == null) return;
        int id = node.data.getId();
        if (id > lowId) rangeHelper(node.left, lowId, highId, result);   // 只走可能有答案的子樹
        if (id >= lowId && id <= highId) result.add(node.data);
        if (id < highId) rangeHelper(node.right, lowId, highId, result);
    }

    public List<Student> inorder() {
        List<Student> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node node, List<Student> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add(node.data);
        inorderHelper(node.right, result);
    }

    public static void main(String[] args) {
        Q12_StudentBstSystem system = new Q12_StudentBstSystem();
        system.add(new Q12_StudentBstSystem.Student(300, "Mina", 78));
        system.add(new Q12_StudentBstSystem.Student(100, "Leo", 84));
        system.add(new Q12_StudentBstSystem.Student(500, "Nora", 105));
        system.add(new Q12_StudentBstSystem.Student(200, "Ivy", 69));
        System.out.println(system.updateScore(200, 88));
        System.out.println(system.studentsBetween(150, 500));
        System.out.println(system.remove(300));
        System.out.println(system.inorder());

        System.out.println("--- 邊界測試 ---");
        System.out.println(system.add(null));
        System.out.println(system.add(new Student(100, "Copy", 50)));    // duplicate id
        System.out.println(system.find(999));
        System.out.println(system.updateScore(999, 60));
        System.out.println(system.updateScore(100, -20) + " " + system.find(100));
        System.out.println(system.remove(999));
        System.out.println(system.studentsBetween(500, 150));            // low > high
        System.out.println(system.studentsBetween(1, 1000));
        System.out.println(system.studentsBetween(200, 200));

        try {
            new Student(0, "NoId", 60);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
        try {
            new Student(1, "  ", 60);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }

        Q12_StudentBstSystem empty = new Q12_StudentBstSystem();
        System.out.println(empty.inorder() + " " + empty.studentsBetween(1, 100) + " " + empty.find(1));
    }
}
