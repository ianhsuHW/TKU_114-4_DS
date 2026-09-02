// 課後作業六：物流成本網路
// 需求：支援 weighted directed edge 的新增、更新、移除與查詢，
//       拒絕負權重及不存在 vertex。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LogisticsWeightedGraph {

    public record Route(String to, int cost) {
        public Route {
            if (to == null || to.isBlank()) throw new IllegalArgumentException("to");
            if (cost < 0) throw new IllegalArgumentException("cost must not be negative");
        }

        @Override
        public String toString() {
            return to + "(" + cost + ")";
        }
    }

    // weighted neighbor 不能只存站名，必須連 cost 一起保存
    private final Map<String, List<Route>> routes = new LinkedHashMap<>();

    public boolean addWarehouse(String name) {
        if (name == null || name.isBlank()) return false;
        return routes.putIfAbsent(name.trim(), new ArrayList<>()) == null;
    }

    // 新增回傳 true；已存在的 edge 改成更新 cost 並回傳 false，edge 數不變
    public boolean addRoute(String from, String to, int cost) {
        requireKnown(from);
        requireKnown(to);
        if (cost < 0) throw new IllegalArgumentException("cost must not be negative");
        if (from.equals(to)) return false;

        List<Route> edges = routes.get(from);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).to().equals(to)) {
                edges.set(i, new Route(to, cost));
                return false;
            }
        }
        edges.add(new Route(to, cost));
        return true;
    }

    public boolean removeRoute(String from, String to) {
        requireKnown(from);
        requireKnown(to);
        return routes.get(from).removeIf(route -> route.to().equals(to));
    }

    // 查不到時回傳 -1 當作「沒有這條路線」，與 cost 0 的免費路線區分開
    public int costOf(String from, String to) {
        requireKnown(from);
        requireKnown(to);
        for (Route route : routes.get(from)) {
            if (route.to().equals(to)) return route.cost();
        }
        return -1;
    }

    public List<Route> routesFrom(String warehouse) {
        requireKnown(warehouse);
        return List.copyOf(routes.get(warehouse));
    }

    public int outDegree(String warehouse) {
        requireKnown(warehouse);
        return routes.get(warehouse).size();
    }

    // in-degree 與 incoming cost 都要掃描全部 outgoing list
    public int inDegree(String warehouse) {
        requireKnown(warehouse);
        int count = 0;
        for (List<Route> edges : routes.values()) {
            for (Route route : edges) {
                if (route.to().equals(warehouse)) count++;
            }
        }
        return count;
    }

    public int totalCost() {
        int total = 0;
        for (List<Route> edges : routes.values()) {
            for (Route route : edges) total += route.cost();
        }
        return total;
    }

    public int routeCount() {
        int total = 0;
        for (List<Route> edges : routes.values()) total += edges.size();
        return total;
    }

    private void requireKnown(String warehouse) {
        if (warehouse == null || !routes.containsKey(warehouse)) {
            throw new IllegalArgumentException("unknown warehouse: " + warehouse);
        }
    }

    public void printReport() {
        System.out.println("warehouse | out | in | routes");
        for (String warehouse : routes.keySet()) {
            System.out.printf("%-9s | %3d | %2d | %s%n",
                    warehouse, outDegree(warehouse), inDegree(warehouse),
                    routes.get(warehouse));
        }
    }

    private static void expectRejected(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  " + label + " -> accepted (WRONG)");
        } catch (IllegalArgumentException e) {
            System.out.println("  " + label + " -> rejected: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LogisticsWeightedGraph network = new LogisticsWeightedGraph();
        for (String warehouse : List.of("Taipei", "Taichung", "Tainan", "Kaohsiung", "Hualien")) {
            network.addWarehouse(warehouse);
        }

        System.out.println("[add routes]");
        System.out.println("Taipei->Taichung 250 = " + network.addRoute("Taipei", "Taichung", 250));
        System.out.println("Taichung->Tainan 180 = " + network.addRoute("Taichung", "Tainan", 180));
        System.out.println("Tainan->Kaohsiung 60 = " + network.addRoute("Tainan", "Kaohsiung", 60));
        System.out.println("Taipei->Hualien 200  = " + network.addRoute("Taipei", "Hualien", 200));
        System.out.println("Taichung->Kaohsiung 300 = "
                + network.addRoute("Taichung", "Kaohsiung", 300));
        System.out.println("routeCount=" + network.routeCount()
                + " totalCost=" + network.totalCost());

        System.out.println();
        System.out.println("[update]");
        System.out.println("Taipei->Taichung 220 = "
                + network.addRoute("Taipei", "Taichung", 220) + " (false 代表更新)");
        System.out.println("cost=" + network.costOf("Taipei", "Taichung"));
        System.out.println("routeCount=" + network.routeCount() + " (更新不增加 edge)");

        System.out.println();
        network.printReport();

        System.out.println();
        System.out.println("[query]");
        System.out.println("Taipei routes=" + network.routesFrom("Taipei"));
        System.out.println("cost(Tainan,Kaohsiung)=" + network.costOf("Tainan", "Kaohsiung"));
        System.out.println("cost(Kaohsiung,Taipei)=" + network.costOf("Kaohsiung", "Taipei")
                + " (-1 代表沒有這條 directed 路線)");
        System.out.println("Kaohsiung in=" + network.inDegree("Kaohsiung")
                + " out=" + network.outDegree("Kaohsiung"));

        System.out.println();
        System.out.println("[rejected input]");
        expectRejected("negative cost",
                () -> network.addRoute("Taipei", "Tainan", -100));
        expectRejected("unknown from",
                () -> network.addRoute("Keelung", "Taipei", 100));
        expectRejected("unknown to",
                () -> network.addRoute("Taipei", "Keelung", 100));
        expectRejected("query unknown",
                () -> network.costOf("Taipei", "Keelung"));
        System.out.println("  self route -> " + network.addRoute("Taipei", "Taipei", 0));
        System.out.println("  addWarehouse(\"  \") -> " + network.addWarehouse("  "));

        System.out.println();
        System.out.println("[remove]");
        System.out.println("removeRoute(Taipei,Hualien)="
                + network.removeRoute("Taipei", "Hualien"));
        System.out.println("removeRoute again="
                + network.removeRoute("Taipei", "Hualien"));
        System.out.println("Hualien in=" + network.inDegree("Hualien"));
        System.out.println("routeCount=" + network.routeCount()
                + " totalCost=" + network.totalCost());
    }
}
