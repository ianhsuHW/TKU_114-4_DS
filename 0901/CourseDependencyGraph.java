// 課堂實作題六：課程相依 Graph
// 需求：使用 directed adjacency list，輸出每門課的 prerequisites 與後續課程，
//       並計算 in/out degree。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseDependencyGraph {
    // edge 方向定義為「先修課 -> 後續課」：
    // outgoing 是這門課擋修的後續課，incoming 是它自己的 prerequisites
    private final Map<String, Set<String>> nextCourses = new LinkedHashMap<>();

    public boolean addCourse(String courseId) {
        if (courseId == null || courseId.isBlank()) return false;
        return nextCourses.putIfAbsent(courseId.trim(), new LinkedHashSet<>()) == null;
    }

    // directed edge 只更新 from 這一邊，不能自動補反向
    public boolean addPrerequisite(String course, String prerequisite) {
        if (!known(course) || !known(prerequisite)) return false;
        if (course.equals(prerequisite)) return false;
        return nextCourses.get(prerequisite).add(course);
    }

    public List<String> prerequisitesOf(String course) {
        if (!known(course)) return List.of();
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Set<String>> entry : nextCourses.entrySet()) {
            if (entry.getValue().contains(course)) result.add(entry.getKey());
        }
        return result;
    }

    public List<String> followingOf(String course) {
        Set<String> set = nextCourses.get(course);
        return set == null ? List.of() : new ArrayList<>(set);
    }

    // out-degree 直接是 outgoing set 的大小
    public int outDegree(String course) {
        return known(course) ? nextCourses.get(course).size() : 0;
    }

    // in-degree 必須掃描所有 outgoing set，不能拿 outgoing size 充數
    public int inDegree(String course) {
        if (!known(course)) return 0;
        int count = 0;
        for (Set<String> targets : nextCourses.values()) {
            if (targets.contains(course)) count++;
        }
        return count;
    }

    // in-degree 為 0 表示沒有先修限制，可以直接修
    public List<String> entryCourses() {
        List<String> result = new ArrayList<>();
        for (String course : nextCourses.keySet()) {
            if (inDegree(course) == 0) result.add(course);
        }
        return result;
    }

    public int edgeCount() {
        int total = 0;
        for (Set<String> targets : nextCourses.values()) total += targets.size();
        return total;
    }

    private boolean known(String course) {
        return course != null && nextCourses.containsKey(course);
    }

    public void printReport() {
        System.out.println("course   | in | out | prerequisites | following");
        for (String course : nextCourses.keySet()) {
            System.out.printf("%-8s | %2d | %3d | %-13s | %s%n",
                    course, inDegree(course), outDegree(course),
                    prerequisitesOf(course), followingOf(course));
        }
    }

    public static void main(String[] args) {
        CourseDependencyGraph graph = new CourseDependencyGraph();
        for (String course : List.of("CS101", "CS102", "CS201", "CS202", "CS301", "GE100")) {
            graph.addCourse(course);
        }

        System.out.println("[add prerequisites]");
        System.out.println("CS102 <- CS101 : " + graph.addPrerequisite("CS102", "CS101"));
        System.out.println("CS201 <- CS102 : " + graph.addPrerequisite("CS201", "CS102"));
        System.out.println("CS202 <- CS102 : " + graph.addPrerequisite("CS202", "CS102"));
        System.out.println("CS301 <- CS201 : " + graph.addPrerequisite("CS301", "CS201"));
        System.out.println("CS301 <- CS202 : " + graph.addPrerequisite("CS301", "CS202"));
        System.out.println("duplicate      : " + graph.addPrerequisite("CS102", "CS101"));
        System.out.println("self           : " + graph.addPrerequisite("CS101", "CS101"));
        System.out.println("unknown course : " + graph.addPrerequisite("CS999", "CS101"));
        System.out.println("edgeCount=" + graph.edgeCount());

        System.out.println();
        graph.printReport();

        System.out.println();
        System.out.println("[query]");
        System.out.println("CS301 prerequisites=" + graph.prerequisitesOf("CS301"));
        System.out.println("CS102 following=" + graph.followingOf("CS102"));
        System.out.println("CS101 in=" + graph.inDegree("CS101")
                + " out=" + graph.outDegree("CS101"));
        System.out.println("CS301 in=" + graph.inDegree("CS301")
                + " out=" + graph.outDegree("CS301") + " (最後一門課沒有後續)");
        System.out.println("no prerequisite courses=" + graph.entryCourses());
        System.out.println("GE100 following=" + graph.followingOf("GE100")
                + " (獨立課程)");
        System.out.println("missing course=" + graph.prerequisitesOf("CS999"));
    }
}
