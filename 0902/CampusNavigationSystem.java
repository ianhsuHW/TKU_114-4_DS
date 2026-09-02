// 期末綜合練習一：校園導航系統
// 需求：使用 HashMap 保存地點、adjacency list 保存道路、
//       BFS 還原最少 edge 路徑。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CampusNavigationSystem {

    public record Location(String id, String name, String category) {
        public Location {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
            if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        }

        @Override
        public String toString() {
            return id + "(" + name + ")";
        }
    }

    // HashMap 負責「依 id 直接查地點」，adjacency list 負責「保存道路關係」，
    // 兩份結構分工不同，新增地點時必須同時建立
    private final Map<String, Location> locations = new HashMap<>();
    private final Map<String, Set<String>> roads = new LinkedHashMap<>();

    public boolean addLocation(String id, String name, String category) {
        if (id == null || id.isBlank() || locations.containsKey(id)) return false;
        locations.put(id, new Location(id, name, category));
        roads.put(id, new LinkedHashSet<>());
        return true;
    }

    // 校園道路是雙向的，兩邊 adjacency 都要更新
    public boolean addRoad(String first, String second) {
        if (!known(first) || !known(second) || first.equals(second)) return false;
        boolean changed = roads.get(first).add(second);
        roads.get(second).add(first);
        return changed;
    }

    public Location locationOf(String id) {
        return locations.get(id);
    }

    public List<String> neighbors(String id) {
        Set<String> set = roads.get(id);
        return set == null ? List.of() : new ArrayList<>(set);
    }

    // BFS：第一次抵達 target 的走法 edge 數最少，
    // previous 保存前一站，找到後反向追回起點再 reverse
    public List<String> route(String from, String to) {
        if (!known(from) || !known(to)) return List.of();
        if (from.equals(to)) return List.of(from);

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        Map<String, String> previous = new HashMap<>();
        queue.offer(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(to)) break;
            for (String next : roads.getOrDefault(current, Set.of())) {
                if (visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }

        if (!visited.contains(to)) return List.of();      // 沒到達就回 empty
        List<String> path = new ArrayList<>();
        for (String at = to; at != null; at = previous.get(at)) path.add(at);
        Collections.reverse(path);
        return path;
    }

    public int distance(String from, String to) {
        List<String> route = route(from, to);
        return route.isEmpty() ? -1 : route.size() - 1;
    }

    public List<String> reachableFrom(String from) {
        List<String> result = new ArrayList<>();
        if (!known(from)) return result;
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        queue.offer(from);
        visited.add(from);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            result.add(current);
            for (String next : roads.getOrDefault(current, Set.of())) {
                if (visited.add(next)) queue.offer(next);
            }
        }
        return result;
    }

    private boolean known(String id) {
        return id != null && locations.containsKey(id);
    }

    public void printRoute(String from, String to) {
        List<String> route = route(from, to);
        if (route.isEmpty()) {
            System.out.printf("  %-5s -> %-5s no route (distance=%d)%n",
                    from, to, distance(from, to));
            return;
        }
        List<String> names = new ArrayList<>();
        for (String id : route) names.add(locations.get(id).name());
        System.out.printf("  %-5s -> %-5s distance=%d path=%s%n",
                from, to, distance(from, to), String.join(" > ", names));
    }

    private static CampusNavigationSystem sampleCampus() {
        CampusNavigationSystem campus = new CampusNavigationSystem();
        campus.addLocation("L1", "校門口", "gate");
        campus.addLocation("L2", "圖書館", "study");
        campus.addLocation("L3", "工學館", "class");
        campus.addLocation("L4", "商管大樓", "class");
        campus.addLocation("L5", "體育館", "sport");
        campus.addLocation("L6", "學生宿舍", "dorm");
        campus.addLocation("L7", "實驗農場", "farm");     // 尚未有道路連接

        campus.addRoad("L1", "L2");
        campus.addRoad("L1", "L3");
        campus.addRoad("L2", "L4");
        campus.addRoad("L3", "L5");
        campus.addRoad("L4", "L6");
        campus.addRoad("L5", "L6");
        return campus;
    }

    public static void main(String[] args) {
        CampusNavigationSystem campus = sampleCampus();

        System.out.println("[locations]");
        for (String id : List.of("L1", "L4", "L7", "L9")) {
            System.out.println("  " + id + " -> " + campus.locationOf(id));
        }

        System.out.println();
        System.out.println("[roads]");
        for (String id : List.of("L1", "L6", "L7")) {
            System.out.println("  " + id + " neighbors=" + campus.neighbors(id));
        }

        System.out.println();
        System.out.println("[shortest route]");
        campus.printRoute("L1", "L6");
        campus.printRoute("L6", "L1");
        campus.printRoute("L2", "L5");
        campus.printRoute("L1", "L1");

        System.out.println();
        System.out.println("[boundary cases]");
        campus.printRoute("L1", "L7");     // 有地點但沒有道路
        campus.printRoute("L1", "L9");     // 地點不存在
        campus.printRoute("L9", "L1");
        campus.printRoute("L1", null);

        System.out.println();
        System.out.println("[reachable]");
        System.out.println("  from L1 = " + campus.reachableFrom("L1"));
        System.out.println("  from L7 = " + campus.reachableFrom("L7"));
        System.out.println("  from L9 = " + campus.reachableFrom("L9"));

        System.out.println();
        System.out.println("[new road changes the answer]");
        System.out.println("  addRoad(L2,L6)=" + campus.addRoad("L2", "L6"));
        campus.printRoute("L1", "L6");
        System.out.println("  duplicate addRoad=" + campus.addRoad("L2", "L6"));
        System.out.println("  addRoad to unknown=" + campus.addRoad("L1", "L9"));
        System.out.println("  duplicate addLocation=" + campus.addLocation("L1", "X", "y"));
    }
}
