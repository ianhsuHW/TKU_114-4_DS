// 課堂實作題五：Network Components
// 需求：輸出 component、component count 與最大 component。
//       含一般案例與 missing/empty 邊界案例。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class NetworkComponents {

    // 依全部 vertex 逐一啟動 BFS，isolated vertex 才不會被漏掉
    public static List<List<String>> components(Map<String, List<String>> graph) {
        List<List<String>> result = new ArrayList<>();
        if (graph == null) return result;

        Set<String> visited = new HashSet<>();
        for (String start : graph.keySet()) {
            if (visited.contains(start)) continue;

            List<String> component = new ArrayList<>();
            Queue<String> queue = new ArrayDeque<>();
            queue.offer(start);
            visited.add(start);

            while (!queue.isEmpty()) {
                String current = queue.poll();
                component.add(current);
                for (String next : graph.getOrDefault(current, List.of())) {
                    if (graph.containsKey(next) && visited.add(next)) queue.offer(next);
                }
            }
            result.add(component);          // 一次 BFS 走完就是一個 component
        }
        return result;
    }

    public static int componentCount(Map<String, List<String>> graph) {
        return components(graph).size();
    }

    public static List<String> largestComponent(Map<String, List<String>> graph) {
        List<String> largest = List.of();
        for (List<String> component : components(graph)) {
            if (component.size() > largest.size()) largest = component;
        }
        return largest;
    }

    // 查某個 vertex 屬於哪個 component；不存在時回傳 empty List
    public static List<String> componentOf(Map<String, List<String>> graph, String vertex) {
        if (graph == null || vertex == null || !graph.containsKey(vertex)) return List.of();
        for (List<String> component : components(graph)) {
            if (component.contains(vertex)) return component;
        }
        return List.of();
    }

    public static List<String> isolatedVertices(Map<String, List<String>> graph) {
        List<String> result = new ArrayList<>();
        for (List<String> component : components(graph)) {
            if (component.size() == 1) result.add(component.get(0));
        }
        return result;
    }

    public static boolean isFullyConnected(Map<String, List<String>> graph) {
        return graph != null && !graph.isEmpty() && componentCount(graph) == 1;
    }

    public static void printReport(String label, Map<String, List<String>> graph) {
        System.out.println("[" + label + "]");
        List<List<String>> components = components(graph);
        if (components.isEmpty()) {
            System.out.println("  (no component)");
        }
        for (int i = 0; i < components.size(); i++) {
            System.out.println("  component " + (i + 1) + " size="
                    + components.get(i).size() + " " + components.get(i));
        }
        System.out.println("  count=" + componentCount(graph)
                + " largest=" + largestComponent(graph)
                + " isolated=" + isolatedVertices(graph)
                + " fullyConnected=" + isFullyConnected(graph));
        System.out.println();
    }

    private static Map<String, List<String>> sampleNetwork() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("srv-a", List.of("srv-b", "srv-c"));
        graph.put("srv-b", List.of("srv-a", "srv-c"));
        graph.put("srv-c", List.of("srv-a", "srv-b"));
        graph.put("srv-d", List.of("srv-e"));
        graph.put("srv-e", List.of("srv-d"));
        graph.put("srv-f", List.of());          // isolated
        graph.put("srv-g", List.of());          // isolated
        return graph;
    }

    public static void main(String[] args) {
        Map<String, List<String>> network = sampleNetwork();
        printReport("general case", network);

        System.out.println("[componentOf]");
        for (String vertex : List.of("srv-a", "srv-e", "srv-f", "srv-zz")) {
            System.out.println("  " + vertex + " -> " + componentOf(network, vertex));
        }
        System.out.println();

        Map<String, List<String>> connected = new LinkedHashMap<>();
        connected.put("A", List.of("B"));
        connected.put("B", List.of("A", "C"));
        connected.put("C", List.of("B"));
        printReport("fully connected", connected);

        Map<String, List<String>> single = new LinkedHashMap<>();
        single.put("only", List.of());
        printReport("single isolated vertex", single);

        printReport("empty graph", new LinkedHashMap<>());
        printReport("null graph", null);

        System.out.println("[missing vertex]");
        System.out.println("  componentOf(null graph, A)=" + componentOf(null, "A"));
        System.out.println("  componentOf(network, null)=" + componentOf(network, null));
    }
}
