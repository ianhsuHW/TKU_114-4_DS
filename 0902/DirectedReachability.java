// 課堂實作題三：Directed Reachability
// 需求：支援多組 from-to reachable 查詢。
//       含一般案例與 missing/empty 邊界案例。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class DirectedReachability {

    public record Query(String from, String to) {
        @Override
        public String toString() {
            return from + "->" + to;
        }
    }

    public record Result(Query query, boolean reachable, String note) {
    }

    private final Map<String, List<String>> outgoing;

    public DirectedReachability(Map<String, List<String>> outgoing) {
        this.outgoing = outgoing == null ? new LinkedHashMap<>() : outgoing;
    }

    // 找到 target 就立刻回傳，不必走完所有可到達的 vertex
    public boolean reachable(String from, String to) {
        if (from == null || to == null) return false;
        if (!outgoing.containsKey(from) || !outgoing.containsKey(to)) return false;
        if (from.equals(to)) return true;          // vertex 存在時，自己到自己視為可達

        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(from);
        visited.add(from);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String next : outgoing.getOrDefault(current, List.of())) {
                if (!outgoing.containsKey(next)) continue;
                if (next.equals(to)) return true;
                // visited.add 回傳 false 代表已排入過，cycle 才不會無限走訪
                if (visited.add(next)) queue.offer(next);
            }
        }
        return false;
    }

    public List<String> reachableFrom(String from) {
        List<String> result = new ArrayList<>();
        if (from == null || !outgoing.containsKey(from)) return result;
        Queue<String> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        queue.offer(from);
        visited.add(from);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String next : outgoing.getOrDefault(current, List.of())) {
                if (outgoing.containsKey(next) && visited.add(next)) {
                    result.add(next);
                    queue.offer(next);
                }
            }
        }
        return result;
    }

    public List<Result> runQueries(List<Query> queries) {
        List<Result> results = new ArrayList<>();
        if (queries == null) return results;
        for (Query query : queries) {
            if (query == null) continue;
            String note = describe(query);
            results.add(new Result(query, reachable(query.from(), query.to()), note));
        }
        return results;
    }

    private String describe(Query query) {
        if (query.from() == null || query.to() == null) return "null vertex";
        if (!outgoing.containsKey(query.from())) return "missing from";
        if (!outgoing.containsKey(query.to())) return "missing to";
        if (query.from().equals(query.to())) return "same vertex";
        return "";
    }

    public void printResults(List<Result> results) {
        if (results.isEmpty()) {
            System.out.println("  (no query)");
            return;
        }
        for (Result result : results) {
            System.out.printf("  %-12s reachable=%-5s %s%n",
                    result.query(), result.reachable(), result.note());
        }
    }

    private static Map<String, List<String>> sampleGraph() {
        Map<String, List<String>> graph = new LinkedHashMap<>();
        graph.put("A", List.of("B", "C"));
        graph.put("B", List.of("D"));
        graph.put("C", List.of("D"));
        graph.put("D", List.of("E"));
        graph.put("E", List.of("B"));      // cycle：E 回到 B
        graph.put("F", List.of("A"));
        graph.put("G", List.of());         // isolated vertex
        return graph;
    }

    public static void main(String[] args) {
        DirectedReachability system = new DirectedReachability(sampleGraph());

        System.out.println("[general queries]");
        system.printResults(system.runQueries(List.of(
                new Query("A", "E"),
                new Query("E", "A"),        // directed，回不去
                new Query("F", "E"),
                new Query("A", "G"),
                new Query("B", "B"),
                new Query("E", "D"))));     // 透過 cycle 可達

        System.out.println();
        System.out.println("[boundary queries]");
        system.printResults(system.runQueries(List.of(
                new Query("X", "A"),        // missing from
                new Query("A", "X"),        // missing to
                new Query("G", "G"),        // isolated 自己到自己
                new Query(null, "A"))));

        System.out.println();
        System.out.println("[reachableFrom]");
        for (String vertex : List.of("A", "E", "F", "G", "X")) {
            System.out.println("  " + vertex + " -> " + system.reachableFrom(vertex));
        }

        System.out.println();
        System.out.println("[empty graph]");
        DirectedReachability empty = new DirectedReachability(new LinkedHashMap<>());
        empty.printResults(empty.runQueries(List.of(new Query("A", "B"))));
        System.out.println("  reachableFrom(A)=" + empty.reachableFrom("A"));

        System.out.println();
        System.out.println("[null graph / null queries]");
        DirectedReachability nullGraph = new DirectedReachability(null);
        System.out.println("  reachable(A,B)=" + nullGraph.reachable("A", "B"));
        System.out.println("  runQueries(null)=" + nullGraph.runQueries(null));
    }
}
