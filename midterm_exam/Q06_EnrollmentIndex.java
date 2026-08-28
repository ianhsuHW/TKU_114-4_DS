// 第 6 題：Map 與 Set 選課索引
// 重點：Map<String, Set<String>> 一對多，TreeMap/TreeSet 自動維持字典順序。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Q06_EnrollmentIndex {

    private final Map<String, Set<String>> courseToStudents = new TreeMap<>();

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean enroll(String courseCode, String studentId) {
        if (isBlank(courseCode) || isBlank(studentId)) return false;
        String code = courseCode.trim();
        String id = studentId.trim();
        Set<String> students = courseToStudents.get(code);
        if (students == null) {
            students = new TreeSet<>();
            courseToStudents.put(code, students);
        }
        return students.add(id);                    // 重複選課時 Set.add() 回傳 false
    }

    public boolean drop(String courseCode, String studentId) {
        if (isBlank(courseCode) || isBlank(studentId)) return false;
        String code = courseCode.trim();
        Set<String> students = courseToStudents.get(code);
        if (students == null) return false;
        boolean removed = students.remove(studentId.trim());
        if (removed && students.isEmpty()) {
            courseToStudents.remove(code);          // 沒人選的課程要移除
        }
        return removed;
    }

    public int courseSize(String courseCode) {
        if (isBlank(courseCode)) return 0;
        Set<String> students = courseToStudents.get(courseCode.trim());
        return students == null ? 0 : students.size();
    }

    public List<String> studentsOf(String courseCode) {
        List<String> result = new ArrayList<>();
        if (isBlank(courseCode)) return result;
        Set<String> students = courseToStudents.get(courseCode.trim());
        if (students != null) {
            result.addAll(students);                // TreeSet 已是字典順序
        }
        return result;
    }

    public List<String> coursesOf(String studentId) {
        List<String> result = new ArrayList<>();
        if (isBlank(studentId)) return result;
        String id = studentId.trim();
        for (Map.Entry<String, Set<String>> entry : courseToStudents.entrySet()) {
            if (entry.getValue().contains(id)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public Map<String, Integer> summary() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> entry : courseToStudents.entrySet()) {
            result.put(entry.getKey(), entry.getValue().size());
        }
        return result;
    }

    public static void main(String[] args) {
        Q06_EnrollmentIndex index = new Q06_EnrollmentIndex();
        index.enroll("DS", "S02");
        index.enroll("DS", "S01");
        index.enroll("JAVA", "S01");
        System.out.println(index.studentsOf("DS"));
        System.out.println(index.coursesOf("S01"));
        System.out.println(index.summary());

        System.out.println("--- 邊界測試 ---");
        System.out.println(index.enroll("DS", "S01"));      // duplicate
        System.out.println(index.enroll(null, "S01"));
        System.out.println(index.enroll("DS", "  "));
        System.out.println(index.drop("DS", "S99"));        // 沒選過
        System.out.println(index.drop("NONE", "S01"));      // 沒這門課
        System.out.println(index.drop("JAVA", "S01"));      // 最後一人 -> 移除課程
        System.out.println(index.summary());
        System.out.println(index.courseSize("JAVA"));
        System.out.println(index.studentsOf("JAVA"));
        System.out.println(index.coursesOf("S404"));

        List<String> stolen = index.studentsOf("DS");
        stolen.clear();
        System.out.println(index.studentsOf("DS"));
    }
}
