// 課後作業一：學號索引
// 需求：Node 保存 Student，依 studentId 完成 search / insert / delete，
//       重複 id 不得加入。

class Student {
    final int studentId;
    final String name;
    int credit;

    Student(int studentId, String name, int credit) {
        this.studentId = studentId;
        this.name = name;
        this.credit = Math.max(0, credit);
    }

    @Override
    public String toString() {
        return studentId + " " + name + " credit=" + credit;
    }
}

class StudentIndexNode {
    Student data;
    StudentIndexNode left;
    StudentIndexNode right;

    StudentIndexNode(Student data) {
        this.data = data;
    }
}

class StudentIndex {
    private StudentIndexNode root;

    boolean insert(Student student) {
        if (student == null) return false;
        if (root == null) {
            root = new StudentIndexNode(student);
            return true;
        }
        StudentIndexNode current = root;
        while (true) {
            if (student.studentId == current.data.studentId) {
                return false;                       // 重複 id 不加入
            }
            if (student.studentId < current.data.studentId) {
                if (current.left == null) {
                    current.left = new StudentIndexNode(student);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new StudentIndexNode(student);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Student search(int studentId) {
        StudentIndexNode current = root;
        while (current != null) {
            if (studentId == current.data.studentId) return current.data;
            current = studentId < current.data.studentId
                    ? current.left
                    : current.right;
        }
        return null;
    }

    boolean delete(int studentId) {
        if (search(studentId) == null) return false;
        root = delete(root, studentId);
        return true;
    }

    private StudentIndexNode delete(StudentIndexNode node, int studentId) {
        if (node == null) return null;
        if (studentId < node.data.studentId) {
            node.left = delete(node.left, studentId);
        } else if (studentId > node.data.studentId) {
            node.right = delete(node.right, studentId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            StudentIndexNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = delete(node.right, successor.data.studentId);
        }
        return node;
    }

    private StudentIndexNode minimumNode(StudentIndexNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    int size() {
        return size(root);
    }

    private int size(StudentIndexNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    void report() {
        System.out.println("  size=" + size());
        report(root);
    }

    private void report(StudentIndexNode node) {
        if (node == null) return;
        report(node.left);
        System.out.println("  " + node.data);
        report(node.right);
    }
}

public class StudentBstIndex {
    public static void main(String[] args) {
        StudentIndex index = new StudentIndex();

        System.out.println("insert 410=" + index.insert(new Student(410, "Wei", 18)));
        System.out.println("insert 205=" + index.insert(new Student(205, "Ann", 21)));
        System.out.println("insert 630=" + index.insert(new Student(630, "Kai", 15)));
        System.out.println("insert 120=" + index.insert(new Student(120, "Bo", 24)));
        System.out.println("insert 320=" + index.insert(new Student(320, "Lin", 20)));
        System.out.println("insert 700=" + index.insert(new Student(700, "Mei", 12)));
        System.out.println("duplicate 205="
                + index.insert(new Student(205, "Duplicate", 9)));
        System.out.println("null=" + index.insert(null));

        System.out.println("[after insert]");
        index.report();

        System.out.println("search(320)=" + index.search(320));
        System.out.println("search(999)=" + index.search(999));

        System.out.println("delete leaf 120=" + index.delete(120));
        System.out.println("delete one-child 630=" + index.delete(630));
        System.out.println("delete two-children 410=" + index.delete(410));
        System.out.println("delete missing 999=" + index.delete(999));

        System.out.println("[after delete]");
        index.report();
    }
}
