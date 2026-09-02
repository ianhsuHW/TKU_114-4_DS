// 課堂實作題二：課程成績統計
// 需求：使用 Map<String,List<Integer>> 管理課號與成績，
//       提供新增、平均、最高分與依課號排序報告。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class CourseGradeMap {
    private final Map<String, List<Integer>> scores = new HashMap<>();

    // computeIfAbsent：課號第一次出現時才建立 List
    public boolean addScore(String courseId, int score) {
        if (courseId == null || courseId.isBlank()) return false;
        if (score < 0 || score > 100) return false;
        scores.computeIfAbsent(courseId.trim().toUpperCase(),
                key -> new ArrayList<>()).add(score);
        return true;
    }

    public List<Integer> scoresOf(String courseId) {
        List<Integer> list = scores.get(normalize(courseId));
        return list == null ? List.of() : List.copyOf(list);
    }

    // 沒有成績時回傳 empty，不用 0 或 -1 這種需要另外解讀的值
    public OptionalDouble average(String courseId) {
        List<Integer> list = scores.get(normalize(courseId));
        if (list == null || list.isEmpty()) return OptionalDouble.empty();
        int total = 0;
        for (int score : list) total += score;
        return OptionalDouble.of((double) total / list.size());
    }

    public OptionalInt highest(String courseId) {
        List<Integer> list = scores.get(normalize(courseId));
        if (list == null || list.isEmpty()) return OptionalInt.empty();
        int max = list.get(0);
        for (int score : list) max = Math.max(max, score);
        return OptionalInt.of(max);
    }

    public int courseCount() {
        return scores.size();
    }

    private static String normalize(String courseId) {
        return courseId == null ? "" : courseId.trim().toUpperCase();
    }

    // HashMap 不保證順序，要排序報告必須自己把 key 取出來排
    public void printReport() {
        List<String> courseIds = new ArrayList<>(scores.keySet());
        courseIds.sort(String::compareTo);
        System.out.println("courseId | count | average | highest | scores");
        for (String courseId : courseIds) {
            List<Integer> list = scores.get(courseId);
            System.out.printf("%-8s | %5d | %7.2f | %7d | %s%n",
                    courseId, list.size(),
                    average(courseId).orElse(0.0),
                    highest(courseId).orElse(0),
                    list);
        }
    }

    public static void main(String[] args) {
        CourseGradeMap gradeMap = new CourseGradeMap();

        gradeMap.addScore("CS102", 88);
        gradeMap.addScore("cs102", 74);      // 大小寫視為同一門課
        gradeMap.addScore("IM201", 91);
        gradeMap.addScore("CS102", 95);
        gradeMap.addScore("BA110", 60);
        gradeMap.addScore("IM201", 78);
        gradeMap.addScore("IM201", 100);
        gradeMap.addScore("BA110", 72);

        System.out.println("[invalid input]");
        System.out.println("addScore(null,80)=" + gradeMap.addScore(null, 80));
        System.out.println("addScore(\"  \",80)=" + gradeMap.addScore("  ", 80));
        System.out.println("addScore(CS102,120)=" + gradeMap.addScore("CS102", 120));
        System.out.println("addScore(CS102,-5)=" + gradeMap.addScore("CS102", -5));

        System.out.println();
        System.out.println("courseCount=" + gradeMap.courseCount());
        System.out.println("raw map order=" + gradeMap.scores);

        System.out.println();
        gradeMap.printReport();

        System.out.println();
        System.out.println("[single course]");
        System.out.println("CS102 scores=" + gradeMap.scoresOf("CS102"));
        System.out.println("CS102 average=" + gradeMap.average("CS102").getAsDouble());
        System.out.println("CS102 highest=" + gradeMap.highest("CS102").getAsInt());

        System.out.println();
        System.out.println("[missing course]");
        System.out.println("XX999 scores=" + gradeMap.scoresOf("XX999"));
        System.out.println("XX999 average present=" + gradeMap.average("XX999").isPresent());
        System.out.println("XX999 highest present=" + gradeMap.highest("XX999").isPresent());
    }
}
