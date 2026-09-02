// 課堂實作題四：Metro Transfer Path
// 需求：輸出最少站數路徑及 edge count。
//       含一般案例與 missing/empty 邊界案例。

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

public class MetroTransferPath {
    private final Map<String, Set<String>> lines = new LinkedHashMap<>();

    public boolean addStation(String station) {
        if (station == null || station.isBlank()) return false;
        return lines.putIfAbsent(station.trim(), new LinkedHashSet<>()) == null;
    }

    // undirected：兩站互為 neighbor，兩邊都要更新
    public boolean connect(String first, String second) {
        if (!lines.containsKey(first) || !lines.containsKey(second)) return false;
        if (first.equals(second)) return false;
        boolean changed = lines.get(first).add(second);
        lines.get(second).add(first);
        return changed;
    }

    // BFS 第一次抵達 target 就是最少 edge 的走法，
    // previous 記錄「是誰帶我來的」，之後從 target 反向追回起點
    public List<String> shortestPath(String from, String to) {
        if (from == null || to == null) return List.of();
        if (!lines.containsKey(from) || !lines.containsKey(to)) return List.of();
        if (from.equals(to)) return List.of(from);

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        Map<String, String> previous = new HashMap<>();
        queue.offer(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(to)) break;
            for (String next : lines.getOrDefault(current, Set.of())) {
                if (visited.add(next)) {
                    previous.put(next, current);
                    queue.offer(next);
                }
            }
        }

        // 沒走到 target 就必須回傳 empty，不能回傳追一半的路徑
        if (!visited.contains(to)) return List.of();

        List<String> path = new ArrayList<>();
        for (String at = to; at != null; at = previous.get(at)) path.add(at);
        Collections.reverse(path);
        return path;
    }

    // edge count = 站數 - 1；無法到達時回傳 -1
    public int edgeCount(String from, String to) {
        List<String> path = shortestPath(from, to);
        return path.isEmpty() ? -1 : path.size() - 1;
    }

    public List<String> neighbors(String station) {
        Set<String> set = lines.get(station);
        return set == null ? List.of() : new ArrayList<>(set);
    }

    public int stationCount() {
        return lines.size();
    }

    public void printRoute(String from, String to) {
        List<String> path = shortestPath(from, to);
        if (path.isEmpty()) {
            System.out.printf("  %-10s -> %-10s no route (edges=%d)%n",
                    from, to, edgeCount(from, to));
            return;
        }
        System.out.printf("  %-10s -> %-10s edges=%d path=%s%n",
                from, to, edgeCount(from, to), String.join(" > ", path));
    }

    private static MetroTransferPath sampleMetro() {
        MetroTransferPath metro = new MetroTransferPath();
        for (String station : List.of("Tamsui", "Beitou", "Shilin", "TaipeiMain",
                "Ximen", "Banqiao", "Nangang", "Airport")) {
            metro.addStation(station);
        }
        metro.connect("Tamsui", "Beitou");
        metro.connect("Beitou", "Shilin");
        metro.connect("Shilin", "TaipeiMain");
        metro.connect("TaipeiMain", "Ximen");
        metro.connect("Ximen", "Banqiao");
        metro.connect("TaipeiMain", "Nangang");
        metro.connect("TaipeiMain", "Banqiao");   // 另一條較短的路線
        return metro;
    }

    public static void main(String[] args) {
        MetroTransferPath metro = sampleMetro();
        System.out.println("stations=" + metro.stationCount());

        System.out.println();
        System.out.println("[general case]");
        metro.printRoute("Tamsui", "Banqiao");
        metro.printRoute("Banqiao", "Tamsui");     // undirected，反向同樣可達
        metro.printRoute("Beitou", "Nangang");
        metro.printRoute("Ximen", "Nangang");

        System.out.println();
        System.out.println("[shortest wins]");
        System.out.println("  TaipeiMain->Banqiao edges="
                + metro.edgeCount("TaipeiMain", "Banqiao")
                + " path=" + metro.shortestPath("TaipeiMain", "Banqiao"));
        System.out.println("  (不會繞經 Ximen，因為 BFS 先找到 1 條 edge 的走法)");

        System.out.println();
        System.out.println("[boundary cases]");
        metro.printRoute("Tamsui", "Tamsui");      // start = target
        metro.printRoute("Tamsui", "Airport");     // isolated station
        metro.printRoute("Tamsui", "Kaohsiung");   // missing station
        metro.printRoute("Kaohsiung", "Tamsui");
        metro.printRoute("Tamsui", null);

        System.out.println();
        System.out.println("[invalid input]");
        System.out.println("  addStation(\"  \")=" + metro.addStation("  "));
        System.out.println("  connect(Tamsui,Tamsui)=" + metro.connect("Tamsui", "Tamsui"));
        System.out.println("  connect(Tamsui,Kaohsiung)="
                + metro.connect("Tamsui", "Kaohsiung"));
        System.out.println("  neighbors(Kaohsiung)=" + metro.neighbors("Kaohsiung"));

        System.out.println();
        System.out.println("[empty metro]");
        MetroTransferPath empty = new MetroTransferPath();
        empty.printRoute("A", "B");
        System.out.println("  stationCount=" + empty.stationCount());
    }
}
