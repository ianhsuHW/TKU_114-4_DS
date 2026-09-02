// 課後作業三：選課重複檢查
// 需求：以複合 key 表示學號與課號，找出重複紀錄、每人課程集合
//       與每門課修課人數。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class EnrollmentConflictSet {

    // 複合 key：學號 + 課號一起決定唯一性，
    // equals 與 hashCode 必須用同一組欄位，否則 Set 會查不到既有紀錄
    record EnrollmentKey(String studentId, String courseId) {
        EnrollmentKey {
            studentId = normalize(studentId, "studentId");
            courseId = normalize(courseId, "courseId");
        }

        private static String normalize(String value, String field) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(field);
            }
            return value.trim().toUpperCase();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof EnrollmentKey key)) return false;
            return studentId.equals(key.studentId) && courseId.equals(key.courseId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(studentId, courseId);
        }

        @Override
        public String toString() {
            return studentId + "-" + courseId;
        }
    }

    private final Set<EnrollmentKey> accepted = new HashSet<>();
    private final List<EnrollmentKey> duplicates = new ArrayList<>();
    private final Map<String, Set<String>> coursesPerStudent = new HashMap<>();
    private final Map<String, Set<String>> studentsPerCourse = new HashMap<>();

    // add() 回傳 false 就代表這筆複合 key 已經存在，是重複選課
    public boolean enroll(String studentId, String courseId) {
        EnrollmentKey key = new EnrollmentKey(studentId, courseId);
        if (!accepted.add(key)) {
            duplicates.add(key);
            return false;
        }
        coursesPerStudent.computeIfAbsent(key.studentId(), k -> new HashSet<>())
                .add(key.courseId());
        studentsPerCourse.computeIfAbsent(key.courseId(), k -> new HashSet<>())
                .add(key.studentId());
        return true;
    }

    public List<EnrollmentKey> duplicateRecords() {
        return List.copyOf(duplicates);
    }

    public Set<String> coursesOf(String studentId) {
        return new TreeSet<>(coursesPerStudent.getOrDefault(
                studentId.trim().toUpperCase(), Set.of()));
    }

    public int headcountOf(String courseId) {
        return studentsPerCourse.getOrDefault(
                courseId.trim().toUpperCase(), Set.of()).size();
    }

    public int enrollmentCount() {
        return accepted.size();
    }

    // TreeMap 讓報告依 key 排序輸出，不依賴 HashMap 的 iterator 順序
    public void printStudentReport() {
        System.out.println("student  | count | courses");
        for (Map.Entry<String, Set<String>> entry
                : new TreeMap<>(coursesPerStudent).entrySet()) {
            System.out.printf("%-8s | %5d | %s%n",
                    entry.getKey(), entry.getValue().size(),
                    new TreeSet<>(entry.getValue()));
        }
    }

    public void printCourseReport() {
        System.out.println("course   | headcount | students");
        List<Map.Entry<String, Set<String>>> entries =
                new ArrayList<>(studentsPerCourse.entrySet());
        entries.sort(Comparator
                .<Map.Entry<String, Set<String>>>comparingInt(e -> -e.getValue().size())
                .thenComparing(Map.Entry::getKey));
        for (Map.Entry<String, Set<String>> entry : entries) {
            System.out.printf("%-8s | %9d | %s%n",
                    entry.getKey(), entry.getValue().size(),
                    new TreeSet<>(entry.getValue()));
        }
    }

    public static void main(String[] args) {
        EnrollmentConflictSet system = new EnrollmentConflictSet();

        String[][] raw = {
                {"412001", "CS101"}, {"412001", "CS102"}, {"412002", "CS101"},
                {"412003", "IM201"}, {"412001", "cs101"},   // 重複（大小寫不同）
                {"412002", "IM201"}, {"412002", "CS101"},   // 重複
                {"412004", "CS102"}, {"412003", "CS101"},
                {" 412001 ", "CS102"},                       // 重複（前後空白）
                {"412004", "IM201"}};

        System.out.println("[enroll]");
        for (String[] pair : raw) {
            boolean ok = system.enroll(pair[0], pair[1]);
            System.out.printf("  %-9s %-6s -> %s%n",
                    pair[0], pair[1], ok ? "accepted" : "DUPLICATE");
        }

        System.out.println();
        System.out.println("input=" + raw.length
                + " accepted=" + system.enrollmentCount()
                + " duplicates=" + system.duplicateRecords().size());
        System.out.println("duplicate records=" + system.duplicateRecords());

        System.out.println();
        system.printStudentReport();

        System.out.println();
        system.printCourseReport();

        System.out.println();
        System.out.println("[query]");
        System.out.println("412001 courses=" + system.coursesOf("412001"));
        System.out.println("CS101 headcount=" + system.headcountOf("CS101"));
        System.out.println("unknown student=" + system.coursesOf("999999"));
        System.out.println("unknown course headcount=" + system.headcountOf("XX999"));

        System.out.println();
        try {
            system.enroll("412001", "  ");
        } catch (IllegalArgumentException e) {
            System.out.println("blank courseId -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
