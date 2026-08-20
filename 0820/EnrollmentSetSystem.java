import java.util.HashSet;
import java.util.Set;

class Enrollment {
    private final String studentId;
    private final String courseCode;
    private final String studentName;

    Enrollment(String studentId, String courseCode, String studentName) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.studentName = studentName;
    }

    String getStudentId() {
        return studentId;
    }

    String getCourseCode() {
        return courseCode;
    }

    // 身分只由 studentId + courseCode 決定，studentName 不參與比較
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Enrollment)) {
            return false;
        }
        Enrollment another = (Enrollment) other;
        return studentId.equals(another.studentId)
                && courseCode.equals(another.courseCode);
    }

    @Override
    public int hashCode() {
        return studentId.hashCode() * 31 + courseCode.hashCode();
    }

    @Override
    public String toString() {
        return studentId + "@" + courseCode + "(" + studentName + ")";
    }
}

public class EnrollmentSetSystem {

    static boolean enroll(Set<Enrollment> book, Enrollment enrollment) {
        boolean added = book.add(enrollment);
        System.out.println("  報名 " + enrollment + " -> " + added
                + (added ? "" : "（已報名過同一課程）"));
        return added;
    }

    static boolean cancel(Set<Enrollment> book, Enrollment enrollment) {
        boolean removed = book.remove(enrollment);
        System.out.println("  取消 " + enrollment + " -> " + removed
                + (removed ? "" : "（原本就沒有這筆報名）"));
        return removed;
    }

    public static void main(String[] args) {
        Set<Enrollment> book = new HashSet<>();

        System.out.println("=== 1. 同一人報名不同課程（應全部成功）===");
        enroll(book, new Enrollment("S101", "CS201", "Amy"));
        enroll(book, new Enrollment("S101", "CS202", "Amy"));
        enroll(book, new Enrollment("S101", "CS301", "Amy"));
        System.out.println("目前筆數：" + book.size());

        System.out.println();
        System.out.println("=== 2. 同一人重複報名同一課程（應失敗）===");
        enroll(book, new Enrollment("S101", "CS201", "Amy"));
        enroll(book, new Enrollment("S101", "CS201", "Amy Chen"));
        System.out.println("筆數仍為：" + book.size());
        System.out.println("即使 studentName 不同也視為同一筆，因為身分只看 studentId + courseCode。");

        System.out.println();
        System.out.println("=== 3. 不同人報名同一課程（應成功）===");
        enroll(book, new Enrollment("S102", "CS201", "Ben"));
        enroll(book, new Enrollment("S103", "CS201", "Cindy"));
        System.out.println("目前筆數：" + book.size());

        System.out.println();
        System.out.println("=== 4. 用新建立但身分相同的物件測試 contains() ===");
        Enrollment probe = new Enrollment("S101", "CS202", "完全不同的名字");
        System.out.println("probe 是新物件：" + probe);
        System.out.println("contains(probe)：" + book.contains(probe));

        Enrollment missing = new Enrollment("S999", "CS202", "不存在");
        System.out.println("contains(不存在的 S999)：" + book.contains(missing));

        System.out.println();
        System.out.println("=== 5. 用新建立但身分相同的物件測試 remove() ===");
        cancel(book, new Enrollment("S101", "CS202", "另一個名字"));
        System.out.println("移除後筆數：" + book.size());
        System.out.println("再次 contains(probe)：" + book.contains(probe));

        System.out.println();
        System.out.println("=== 6. 取消不存在的報名 ===");
        cancel(book, new Enrollment("S101", "CS202", "Amy"));
        cancel(book, new Enrollment("S999", "CS999", "不存在"));

        System.out.println();
        System.out.println("=== 最終報名表 ===");
        for (Enrollment enrollment : book) {
            System.out.println("  " + enrollment);
        }
        System.out.println("總筆數：" + book.size());

        System.out.println();
        System.out.println("=== hashCode 一致性 ===");
        Enrollment a = new Enrollment("S101", "CS201", "Amy");
        Enrollment b = new Enrollment("S101", "CS201", "Amy Chen");
        System.out.println("a.equals(b)：" + a.equals(b));
        System.out.println("hashCode 相同：" + (a.hashCode() == b.hashCode()));
        System.out.println("如果只 override equals 而不 override hashCode，");
        System.out.println("HashSet 會把兩者放進不同 bucket，重複報名就擋不住。");
    }
}
