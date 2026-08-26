// 課後作業二：課程代碼索引
// 需求：以 courseCode 作 key，完成 add、find、remove、updateCredit、
//       code range query 與排序報表。
//       重複 code 不得加入，credit 限制在 1 到 6。

import java.util.ArrayList;
import java.util.List;

class Course {
    final String courseCode;
    final String title;
    int credit;

    Course(String courseCode, String title, int credit) {
        this.courseCode = courseCode;
        this.title = title;
        this.credit = credit;
    }

    @Override
    public String toString() {
        return courseCode + " " + title + " credit=" + credit;
    }
}

class CourseNode {
    Course data;
    CourseNode left;
    CourseNode right;

    CourseNode(Course data) {
        this.data = data;
    }
}

class CourseIndex {

    static final int MIN_CREDIT = 1;
    static final int MAX_CREDIT = 6;

    private CourseNode root;

    static boolean validCredit(int credit) {
        return credit >= MIN_CREDIT && credit <= MAX_CREDIT;
    }

    boolean add(Course course) {
        if (course == null || course.courseCode == null) return false;
        if (!validCredit(course.credit)) return false;

        if (root == null) {
            root = new CourseNode(course);
            return true;
        }
        CourseNode current = root;
        while (true) {
            int order = course.courseCode.compareTo(current.data.courseCode);
            if (order == 0) return false;               // 重複 code 不加入
            if (order < 0) {
                if (current.left == null) {
                    current.left = new CourseNode(course);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CourseNode(course);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Course find(String courseCode) {
        if (courseCode == null) return null;
        CourseNode current = root;
        while (current != null) {
            int order = courseCode.compareTo(current.data.courseCode);
            if (order == 0) return current.data;
            current = order < 0 ? current.left : current.right;
        }
        return null;
    }

    boolean updateCredit(String courseCode, int credit) {
        if (!validCredit(credit)) return false;
        Course course = find(courseCode);
        if (course == null) return false;
        course.credit = credit;
        return true;
    }

    boolean remove(String courseCode) {
        if (find(courseCode) == null) return false;
        root = remove(root, courseCode);
        return true;
    }

    private CourseNode remove(CourseNode node, String courseCode) {
        if (node == null) return null;
        int order = courseCode.compareTo(node.data.courseCode);
        if (order < 0) {
            node.left = remove(node.left, courseCode);
        } else if (order > 0) {
            node.right = remove(node.right, courseCode);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            CourseNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.courseCode);
        }
        return node;
    }

    private CourseNode minimumNode(CourseNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // code 範圍查詢，含端點；low > high 回傳空結果
    List<Course> codeRange(String lowCode, String highCode) {
        List<Course> result = new ArrayList<>();
        if (lowCode == null || highCode == null) return result;
        if (lowCode.compareTo(highCode) > 0) return result;
        codeRange(root, lowCode, highCode, result);
        return result;
    }

    private void codeRange(CourseNode node, String low, String high,
                           List<Course> result) {
        if (node == null) return;
        String code = node.data.courseCode;
        if (low.compareTo(code) < 0) {
            codeRange(node.left, low, high, result);
        }
        if (low.compareTo(code) <= 0 && code.compareTo(high) <= 0) {
            result.add(node.data);
        }
        if (code.compareTo(high) < 0) {
            codeRange(node.right, low, high, result);
        }
    }

    List<Course> sortedCourses() {
        List<Course> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(CourseNode node, List<Course> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    void report(String title) {
        System.out.println("[" + title + "]");
        int totalCredit = 0;
        List<Course> courses = sortedCourses();
        for (Course course : courses) {
            System.out.println("  " + course);
            totalCredit += course.credit;
        }
        System.out.println("  count=" + courses.size()
                + " totalCredit=" + totalCredit);
        System.out.println();
    }
}

public class CourseBstIndex {
    public static void main(String[] args) {
        CourseIndex index = new CourseIndex();

        System.out.println("add CS201="
                + index.add(new Course("CS201", "Data Structures", 3)));
        System.out.println("add CS101="
                + index.add(new Course("CS101", "Programming I", 3)));
        System.out.println("add MA150="
                + index.add(new Course("MA150", "Discrete Math", 4)));
        System.out.println("add CS310="
                + index.add(new Course("CS310", "Algorithms", 3)));
        System.out.println("add EN100="
                + index.add(new Course("EN100", "English", 2)));
        System.out.println("add PE010="
                + index.add(new Course("PE010", "Physical Education", 1)));

        System.out.println("duplicate CS101="
                + index.add(new Course("CS101", "Copy", 3)));
        System.out.println("credit 0="
                + index.add(new Course("XX000", "Bad Credit", 0)));
        System.out.println("credit 7="
                + index.add(new Course("XX007", "Bad Credit", 7)));
        System.out.println("null course=" + index.add(null));

        index.report("after add");

        System.out.println("find(CS310)=" + index.find("CS310"));
        System.out.println("find(ZZ999)=" + index.find("ZZ999"));

        System.out.println("updateCredit(EN100, 3)="
                + index.updateCredit("EN100", 3));
        System.out.println("updateCredit(EN100, 9)="
                + index.updateCredit("EN100", 9));
        System.out.println("updateCredit(ZZ999, 3)="
                + index.updateCredit("ZZ999", 3));

        System.out.println("codeRange(CS101, CS310)="
                + index.codeRange("CS101", "CS310"));
        System.out.println("codeRange(CS999, EN999)="
                + index.codeRange("CS999", "EN999"));
        System.out.println("codeRange(MA150, CS101)="
                + index.codeRange("MA150", "CS101"));

        System.out.println("remove(PE010)=" + index.remove("PE010"));
        System.out.println("remove(CS201)=" + index.remove("CS201"));
        System.out.println("remove(ZZ999)=" + index.remove("ZZ999"));

        index.report("after update and remove");
    }
}
