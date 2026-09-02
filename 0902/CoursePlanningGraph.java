// 期末綜合練習三：課程規劃 Graph
// 需求：directed Graph 保存先修關係，DFS 判斷 reachable
//       並列出所有受影響課程。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoursePlanningGraph {

    // edge 方向為「先修課 -> 後續課」：
    // 從一門課往下走，得到的就是所有受它影響的課
    private final Map<String, Set<String>> unlocks = new LinkedHashMap<>();

    public boolean addCourse(String courseId) {
        if (courseId == null || courseId.isBlank()) return false;
        return unlocks.putIfAbsent(courseId.trim(), new LinkedHashSet<>()) == null;
    }

    // directed edge 只加一個方向，不能自動補反向
    public boolean addPrerequisite(String course, String prerequisite) {
        if (!known(course) || !known(prerequisite) || course.equals(prerequisite)) {
            return false;
        }
        return unlocks.get(prerequisite).add(course);
    }

    // Recursive DFS：沿一條路徑深入，visited 保證 cycle 不會無限遞迴
    public boolean reachable(String from, String to) {
        if (!known(from) || !known(to)) return false;
        if (from.equals(to)) return true;
        return dfsReachable(from, to, new LinkedHashSet<>());
    }

    private boolean dfsReachable(String current, String target, Set<String> visited) {
        if (!visited.add(current)) return false;
        for (String next : unlocks.getOrDefault(current, Set.of())) {
            if (next.equals(target)) return true;
            if (dfsReachable(next, target, visited)) return true;
        }
        return false;
    }

    // 受影響課程：從這門課往下 DFS 能到達的全部課（不含自己）
    public List<String> affectedCourses(String courseId) {
        List<String> affected = new ArrayList<>();
        if (!known(courseId)) return affected;
        Set<String> visited = new LinkedHashSet<>();
        visited.add(courseId);
        collect(courseId, visited, affected);
        return affected;
    }

    private void collect(String current, Set<String> visited, List<String> affected) {
        for (String next : unlocks.getOrDefault(current, Set.of())) {
            if (visited.add(next)) {
                affected.add(next);
                collect(next, visited, affected);
            }
        }
    }

    // Iterative DFS 版本：Graph 很深時可避免 call stack 過深
    public List<String> affectedIterative(String courseId) {
        List<String> affected = new ArrayList<>();
        if (!known(courseId)) return affected;
        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        visited.add(courseId);
        pushAll(stack, unlocks.get(courseId));

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visited.add(current)) continue;
            affected.add(current);
            pushAll(stack, unlocks.getOrDefault(current, Set.of()));
        }
        return affected;
    }

    private void pushAll(ArrayDeque<String> stack, Set<String> next) {
        List<String> list = new ArrayList<>(next);
        for (int i = list.size() - 1; i >= 0; i--) stack.push(list.get(i));
    }

    public List<String> directPrerequisitesOf(String course) {
        List<String> result = new ArrayList<>();
        if (!known(course)) return result;
        for (Map.Entry<String, Set<String>> entry : unlocks.entrySet()) {
            if (entry.getValue().contains(course)) result.add(entry.getKey());
        }
        return result;
    }

    public List<String> directUnlocks(String course) {
        Set<String> set = unlocks.get(course);
        return set == null ? List.of() : new ArrayList<>(set);
    }

    public List<String> entryCourses() {
        List<String> result = new ArrayList<>();
        for (String course : unlocks.keySet()) {
            if (directPrerequisitesOf(course).isEmpty()) result.add(course);
        }
        return result;
    }

    private boolean known(String course) {
        return course != null && unlocks.containsKey(course);
    }

    private static CoursePlanningGraph samplePlan() {
        CoursePlanningGraph plan = new CoursePlanningGraph();
        for (String course : List.of("CS101", "CS102", "CS201", "CS202",
                "CS301", "CS302", "GE100")) {
            plan.addCourse(course);
        }
        plan.addPrerequisite("CS102", "CS101");
        plan.addPrerequisite("CS201", "CS102");
        plan.addPrerequisite("CS202", "CS102");
        plan.addPrerequisite("CS301", "CS201");
        plan.addPrerequisite("CS302", "CS201");
        plan.addPrerequisite("CS302", "CS202");
        return plan;
    }

    public static void main(String[] args) {
        CoursePlanningGraph plan = samplePlan();

        System.out.println("[structure]");
        for (String course : List.of("CS101", "CS102", "CS201", "CS302", "GE100")) {
            System.out.printf("  %-6s prerequisites=%-14s unlocks=%s%n",
                    course, plan.directPrerequisitesOf(course), plan.directUnlocks(course));
        }
        System.out.println("  entryCourses=" + plan.entryCourses());

        System.out.println();
        System.out.println("[DFS reachable]");
        System.out.println("  CS101 -> CS302 = " + plan.reachable("CS101", "CS302"));
        System.out.println("  CS302 -> CS101 = " + plan.reachable("CS302", "CS101")
                + " (directed，反向不可達)");
        System.out.println("  CS202 -> CS301 = " + plan.reachable("CS202", "CS301"));
        System.out.println("  GE100 -> CS101 = " + plan.reachable("GE100", "CS101"));
        System.out.println("  CS101 -> CS101 = " + plan.reachable("CS101", "CS101"));

        System.out.println();
        System.out.println("[affected courses if the course is delayed]");
        for (String course : List.of("CS101", "CS102", "CS201", "CS302", "GE100")) {
            System.out.printf("  %-6s recursive=%-26s iterative=%s%n",
                    course, plan.affectedCourses(course), plan.affectedIterative(course));
        }

        System.out.println();
        System.out.println("[boundary cases]");
        System.out.println("  missing course affected=" + plan.affectedCourses("CS999"));
        System.out.println("  null affected=" + plan.affectedCourses(null));
        System.out.println("  reachable with missing=" + plan.reachable("CS101", "CS999"));
        System.out.println("  duplicate prerequisite="
                + plan.addPrerequisite("CS102", "CS101"));
        System.out.println("  self prerequisite="
                + plan.addPrerequisite("CS101", "CS101"));
        System.out.println("  unknown prerequisite="
                + plan.addPrerequisite("CS999", "CS101"));
        System.out.println("  empty plan affected="
                + new CoursePlanningGraph().affectedCourses("CS101"));

        System.out.println();
        System.out.println("[cycle safety]");
        CoursePlanningGraph cyclic = new CoursePlanningGraph();
        cyclic.addCourse("X");
        cyclic.addCourse("Y");
        cyclic.addPrerequisite("Y", "X");     // X -> Y
        cyclic.addPrerequisite("X", "Y");     // Y -> X，形成 cycle
        System.out.println("  affected(X)=" + cyclic.affectedCourses("X"));
        System.out.println("  reachable(X,Y)=" + cyclic.reachable("X", "Y")
                + " (visited 讓 DFS 不會無限遞迴)");
    }
}
