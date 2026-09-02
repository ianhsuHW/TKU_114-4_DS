// 課堂實作題二：Iterative DFS Trace
// 需求：每次 push、pop 都輸出 Stack 與 visited。
//       含一般案例與 missing/empty 邊界案例。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class IterativeDfsTrace {

    public static List<String> traverse(Map<String, List<String>> graph, String start) {
        return traverse(graph, start, false);
    }

    // trace=true 時逐步輸出 stack 與 visited，方便對照 recursion 的走訪順序
    public static List<String> traverse(Map<String, List<String>> graph,
                                        String start, boolean trace) {
        List<String> order = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) {
            if (trace) System.out.println("  (no result: start missing or graph empty)");
            return order;
        }

        ArrayDeque<String> stack = new ArrayDeque<>();
        Set<String> visited = new LinkedHashSet<>();
        stack.push(start);
        if (trace) print("push " + start, stack, visited);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            // pop 出來才檢查 visited：同一個 vertex 可能被不同 neighbor 推入多次
            if (!visited.add(current)) {
                if (trace) print("pop  " + current + " (already visited)", stack, visited);
                continue;
            }
            order.add(current);
            if (trace) print("pop  " + current, stack, visited);

            List<String> neighbors = graph.getOrDefault(current, List.of());
            // 反向 push，pop 出來的順序才和 recursive DFS 一致
            for (int i = neighbors.size() - 1; i >= 0; i--) {
                String next = neighbors.get(i);
                if (graph.containsKey(next) && !visited.contains(next)) {
                    stack.push(next);
                    if (trace) print("push " + next, stack, visited);
                }
            }
        }
        return order;
    }

    // 對照用的 recursive 版本，確認 iterative 走訪順序相同
    public static List<String> recursive(Map<String, List<String>> graph, String start) {
        List<String> order = new ArrayList<>();
        if (graph == null || start == null || !graph.containsKey(start)) return order;
        visit(graph, start, new LinkedHashSet<>(), order);
        return order;
    }

    private static void visit(Map<String, List<String>> graph, String current,
                              Set<String> visited, List<String> order) {
        if (!visited.add(current)) return;
        order.add(current);
        for (String next : graph.getOrDefault(current, List.of())) {
            if (graph.containsKey(next)) visit(graph, next, visited, order);
        }
    }

    private static void print(String action, ArrayDeque<String> stack, Set<String> visited) {
        System.out.printf("  %-24s stack=%-20s visited=%s%n", action, stack, visited);
    }

    private static Map<String, List<String>> sampleGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D", "E"));
        graph.put("D", List.of("A"));       // cycle 回到 A
        graph.put("E", List.of());
        graph.put("Z", List.of());          // isolated vertex
        return graph;
    }

    public static void main(String[] args) {
        Map<String, List<String>> graph = sampleGraph();

        System.out.println("[trace from A]");
        List<String> order = traverse(graph, "A", true);
        System.out.println("  order=" + order);
        System.out.println("  recursive=" + recursive(graph, "A"));
        System.out.println("  same order=" + order.equals(recursive(graph, "A")));

        System.out.println();
        System.out.println("[cycle does not loop forever]");
        System.out.println("  from D order=" + traverse(graph, "D"));

        System.out.println();
        System.out.println("[isolated vertex]");
        System.out.println("  from Z trace:");
        System.out.println("  order=" + traverse(graph, "Z", true));

        System.out.println();
        System.out.println("[missing start]");
        System.out.println("  order=" + traverse(graph, "X", true));

        System.out.println();
        System.out.println("[null start]");
        System.out.println("  order=" + traverse(graph, null, true));

        System.out.println();
        System.out.println("[empty graph]");
        System.out.println("  order=" + traverse(new LinkedHashMap<>(), "A", true));

        System.out.println();
        System.out.println("[null graph]");
        System.out.println("  order=" + traverse(null, "A", true));
    }
}
