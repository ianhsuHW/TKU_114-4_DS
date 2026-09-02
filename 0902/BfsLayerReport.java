// 課堂實作題一：BFS Layer Report
// 需求：輸出每個 vertex 距離 start 的最少 edge 數。
//       含一般案例與 missing/empty 邊界案例。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BfsLayerReport {

    // BFS 第一次發現某個 vertex 時，用掉的 edge 數就是最少 edge 數，
    // 因此 distance 只在 vertex 進入 queue 的當下寫入一次
    public static Map<String, Integer> layers(Map<String, List<String>> graph, String start) {
        Map<String, Integer> distance = new LinkedHashMap<>();
        if (graph == null || start == null || !graph.containsKey(start)) return distance;

        Queue<String> queue = new ArrayDeque<>();
        queue.offer(start);
        distance.put(start, 0);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            int next = distance.get(current) + 1;
            for (String neighbor : graph.getOrDefault(current, List.of())) {
                // offer 時就記錄距離，等到 poll 才記會讓同一個 vertex 重複入列
                if (graph.containsKey(neighbor) && !distance.containsKey(neighbor)) {
                    distance.put(neighbor, next);
                    queue.offer(neighbor);
                }
            }
        }
        return distance;
    }

    public static List<String> verticesAtLayer(Map<String, List<String>> graph,
                                               String start, int layer) {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : layers(graph, start).entrySet()) {
            if (entry.getValue() == layer) result.add(entry.getKey());
        }
        return result;
    }

    // 走訪不到的 vertex 不能當成距離 0，要單獨列出來
    public static List<String> unreachable(Map<String, List<String>> graph, String start) {
        List<String> result = new ArrayList<>();
        if (graph == null) return result;
        Map<String, Integer> distance = layers(graph, start);
        for (String vertex : graph.keySet()) {
            if (!distance.containsKey(vertex)) result.add(vertex);
        }
        return result;
    }

    public static int maxLayer(Map<String, List<String>> graph, String start) {
        int max = -1;
        for (int layer : layers(graph, start).values()) max = Math.max(max, layer);
        return max;
    }

    public static void printReport(Map<String, List<String>> graph, String start) {
        System.out.println("start=" + start);
        Map<String, Integer> distance = layers(graph, start);
        if (distance.isEmpty()) {
            System.out.println("  (no result: start missing or graph empty)");
        }
        for (Map.Entry<String, Integer> entry : distance.entrySet()) {
            System.out.println("  " + entry.getKey() + " layer=" + entry.getValue());
        }
        System.out.println("  maxLayer=" + maxLayer(graph, start)
                + " unreachable=" + unreachable(graph, start));
        System.out.println();
    }

    private static Map<String, List<String>> sampleGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("A", "D"));
        graph.put("C", List.of("A", "D", "E"));
        graph.put("D", List.of("B", "C", "F"));
        graph.put("E", List.of("C"));
        graph.put("F", List.of("D"));
        graph.put("G", List.of("H"));      // 另一個 component
        graph.put("H", List.of("G"));
        graph.put("I", List.of());         // isolated vertex
        return graph;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = sampleGraph();

        System.out.println("[general case]");
        printReport(graph, "A");

        System.out.println("[layer query]");
        for (int layer = 0; layer <= 3; layer++) {
            System.out.println("  layer " + layer + " = "
                    + verticesAtLayer(graph, "A", layer));
        }

        System.out.println();
        System.out.println("[other component]");
        printReport(graph, "G");

        System.out.println("[isolated vertex]");
        printReport(graph, "I");

        System.out.println("[missing start]");
        printReport(graph, "Z");

        System.out.println("[null start]");
        printReport(graph, null);

        System.out.println("[empty graph]");
        printReport(new LinkedHashMap<>(), "A");

        System.out.println("[null graph]");
        printReport(null, "A");
    }
}
