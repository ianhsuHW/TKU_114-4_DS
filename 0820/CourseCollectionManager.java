import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class ManagedEnrollment {
    private final String studentId;
    private final String name;
    private int score;
    private final Set<String> tags = new HashSet<>();

    ManagedEnrollment(String studentId, String name, int score) {
        this.studentId = studentId;
        this.name = name;
        this.score = clamp(score);
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    String getStudentId() {
        return studentId;
    }

    String getName() {
        return name;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = clamp(score);
    }

    void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            tags.add(tag.toLowerCase());
        }
    }

    boolean hasTag(String tag) {
        return tag != null && tags.contains(tag.toLowerCase());
    }

    String getLevel() {
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }

    @Override
    public String toString() {
        return studentId + " " + name + " score=" + score
                + " level=" + getLevel() + " tags=" + tags;
    }
}

class CourseRegistry {
    private final List<ManagedEnrollment> order = new ArrayList<>();
    private final Set<String> registeredIds = new HashSet<>();
    private final Map<String, ManagedEnrollment> byId = new HashMap<>();

    boolean enroll(ManagedEnrollment enrollment) {
        if (enrollment == null || !registeredIds.add(enrollment.getStudentId())) {
            return false;
        }
        order.add(enrollment);
        byId.put(enrollment.getStudentId(), enrollment);
        return true;
    }

    ManagedEnrollment find(String studentId) {
        return byId.get(studentId);
    }

    boolean updateScore(String studentId, int score) {
        ManagedEnrollment target = byId.get(studentId);
        if (target == null) {
            return false;
        }
        target.setScore(score);
        return true;
    }

    List<ManagedEnrollment> findByTag(String tag) {
        List<ManagedEnrollment> result = new ArrayList<>();
        for (ManagedEnrollment enrollment : order) {
            if (enrollment.hasTag(tag)) {
                result.add(enrollment);
            }
        }
        return result;
    }

    Map<String, Integer> scoreDistribution() {
        Map<String, Integer> distribution = new LinkedHashMap<>();
        distribution.put("A", 0);
        distribution.put("B", 0);
        distribution.put("C", 0);
        distribution.put("D", 0);
        distribution.put("F", 0);
        for (ManagedEnrollment enrollment : order) {
            String level = enrollment.getLevel();
            distribution.put(level, distribution.get(level) + 1);
        }
        return distribution;
    }

    List<ManagedEnrollment> ranking() {
        List<ManagedEnrollment> result = new ArrayList<>(order);
        result.sort(Comparator.comparingInt(ManagedEnrollment::getScore)
                .reversed()
                .thenComparing(ManagedEnrollment::getStudentId));
        return result;
    }

    List<ManagedEnrollment> top(int count) {
        List<ManagedEnrollment> ranked = ranking();
        if (count <= 0) {
            return new ArrayList<>();
        }
        if (count >= ranked.size()) {
            return ranked;
        }
        return new ArrayList<>(ranked.subList(0, count));
    }

    void removeBelow(int minimum) {
        order.removeIf(enrollment -> enrollment.getScore() < minimum);
        registeredIds.clear();
        byId.clear();
        for (ManagedEnrollment enrollment : order) {
            registeredIds.add(enrollment.getStudentId());
            byId.put(enrollment.getStudentId(), enrollment);
        }
    }

    int size() {
        return order.size();
    }

    boolean isConsistent() {
        return order.size() == registeredIds.size()
                && order.size() == byId.size();
    }

    void printState(String title) {
        System.out.println(title);
        System.out.println("  List（報名順序，" + order.size() + " 筆）：");
        for (ManagedEnrollment enrollment : order) {
            System.out.println("    " + enrollment);
        }
        System.out.println("  Set（學號，" + registeredIds.size() + " 筆）：" + registeredIds);
        System.out.println("  Map（key 數，" + byId.size() + " 筆）：" + byId.keySet());
        System.out.println("  三者一致：" + isConsistent());
    }
}

public class CourseCollectionManager {
    public static void main(String[] args) {
        CourseRegistry registry = new CourseRegistry();

        ManagedEnrollment amy = new ManagedEnrollment("S101", "Amy", 88);
        ManagedEnrollment ben = new ManagedEnrollment("S102", "Ben", 55);
        ManagedEnrollment cara = new ManagedEnrollment("S103", "Cara", 92);
        ManagedEnrollment dan = new ManagedEnrollment("S104", "Dan", 88);
        ManagedEnrollment eva = new ManagedEnrollment("S105", "Eva", 47);
        ManagedEnrollment finn = new ManagedEnrollment("S106", "Finn", 73);

        amy.addTag("Java");
        amy.addTag("java");
        amy.addTag("  ");
        ben.addTag(null);
        cara.addTag("Tree");
        cara.addTag("JAVA");
        dan.addTag("");
        eva.addTag("Tree");
        finn.addTag("Java");

        System.out.println("=== 報名（含重複學號）===");
        System.out.println("enroll Amy：" + registry.enroll(amy));
        System.out.println("enroll Ben：" + registry.enroll(ben));
        System.out.println("enroll Cara：" + registry.enroll(cara));
        System.out.println("enroll Dan：" + registry.enroll(dan));
        System.out.println("enroll Eva：" + registry.enroll(eva));
        System.out.println("enroll Finn：" + registry.enroll(finn));
        System.out.println("重複學號 S101：" + registry.enroll(
                new ManagedEnrollment("S101", "Amy2", 100)));
        System.out.println("null：" + registry.enroll(null));
        System.out.println("總筆數：" + registry.size());

        System.out.println();
        registry.printState("=== 初始狀態 ===");

        System.out.println();
        System.out.println("=== 1. updateScore ===");
        System.out.println("updateScore(S102, 78)：" + registry.updateScore("S102", 78));
        System.out.println("更新後：" + registry.find("S102"));
        System.out.println("updateScore(S105, 150) 會被限制為 100："
                + registry.updateScore("S105", 150));
        System.out.println("更新後：" + registry.find("S105"));
        System.out.println("updateScore(S105, 47) 改回原分數："
                + registry.updateScore("S105", 47));
        System.out.println("updateScore(S999, 60)：" + registry.updateScore("S999", 60));

        System.out.println();
        System.out.println("=== 2. findByTag ===");
        System.out.println("findByTag(\"java\")：");
        for (ManagedEnrollment enrollment : registry.findByTag("java")) {
            System.out.println("  " + enrollment);
        }
        System.out.println("findByTag(\"JAVA\") 大小寫不敏感，筆數："
                + registry.findByTag("JAVA").size());
        System.out.println("findByTag(\"Tree\") 筆數：" + registry.findByTag("Tree").size());
        System.out.println("findByTag(\"網路\") 筆數：" + registry.findByTag("網路").size());
        System.out.println("findByTag(null) 筆數：" + registry.findByTag(null).size());
        System.out.println("空白 tag 沒有被加入，所以 Dan 與 Ben 沒有任何 tag。");

        System.out.println();
        System.out.println("=== 3. scoreDistribution ===");
        Map<String, Integer> distribution = registry.scoreDistribution();
        for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
            System.out.println("  " + entry.getKey() + "：" + entry.getValue() + " 人");
        }
        int distributionTotal = 0;
        for (int count : distribution.values()) {
            distributionTotal += count;
        }
        System.out.println("人數總和 = 報名筆數：" + (distributionTotal == registry.size()));

        System.out.println();
        System.out.println("=== 4. top ===");
        System.out.println("top(3)：");
        for (ManagedEnrollment enrollment : registry.top(3)) {
            System.out.println("  " + enrollment);
        }
        System.out.println("Amy 與 Dan 同為 88 分，依學號決定先後。");
        System.out.println("top(99) 筆數（大於人數時回傳全部）：" + registry.top(99).size());
        System.out.println("top(0) 筆數：" + registry.top(0).size());
        System.out.println("top(-5) 筆數：" + registry.top(-5).size());

        System.out.println();
        System.out.println("=== 5. removeBelow(60) ===");
        registry.removeBelow(60);
        registry.printState("=== 清理後狀態 ===");
        System.out.println("find(S105) 已被移除：" + registry.find("S105"));
        System.out.println("清理後 findByTag(\"Tree\") 筆數："
                + registry.findByTag("Tree").size());

        System.out.println();
        System.out.println("=== 清理後的成績分布 ===");
        for (Map.Entry<String, Integer> entry : registry.scoreDistribution().entrySet()) {
            System.out.println("  " + entry.getKey() + "：" + entry.getValue() + " 人");
        }

        System.out.println();
        System.out.println("removeBelow() 之後必須同時重建 Set 與 Map，");
        System.out.println("否則被刪掉的學號還留在 Set 裡，之後就再也無法重新報名。");
    }
}
