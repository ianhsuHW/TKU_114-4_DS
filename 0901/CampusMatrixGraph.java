// 課堂實作題四：校園 Matrix Graph
// 需求：支援新增與移除 undirected edge、查詢 degree、neighbors 與 edge count，
//       重複 edge 不可重複計數。

import java.util.ArrayList;
import java.util.List;

public class CampusMatrixGraph {
    private final List<String> buildings;
    private final boolean[][] edges;

    public CampusMatrixGraph(List<String> buildings) {
        if (buildings == null || buildings.isEmpty()) {
            throw new IllegalArgumentException("buildings");
        }
        this.buildings = List.copyOf(buildings);
        this.edges = new boolean[buildings.size()][buildings.size()];
    }

    private int indexOf(String building) {
        int index = buildings.indexOf(building);
        if (index < 0) throw new IllegalArgumentException("unknown building: " + building);
        return index;
    }

    // matrix 是 boolean，重複 addEdge 只是再寫一次 true，
    // 回傳值告訴呼叫端這次有沒有真的新增 edge
    public boolean addEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (a == b) return false;               // 不接受自己連自己
        if (edges[a][b]) return false;
        edges[a][b] = true;
        edges[b][a] = true;                     // undirected 必須同步兩個方向
        return true;
    }

    public boolean removeEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (!edges[a][b]) return false;
        edges[a][b] = false;
        edges[b][a] = false;
        return true;
    }

    public boolean hasEdge(String first, String second) {
        return edges[indexOf(first)][indexOf(second)];
    }

    public int degree(String building) {
        int row = indexOf(building);
        int degree = 0;
        for (boolean connected : edges[row]) {
            if (connected) degree++;
        }
        return degree;
    }

    public List<String> neighbors(String building) {
        int row = indexOf(building);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < buildings.size(); column++) {
            if (edges[row][column]) result.add(buildings.get(column));
        }
        return result;
    }

    // 只數上三角，undirected edge 才不會被算兩次
    public int edgeCount() {
        int count = 0;
        for (int row = 0; row < buildings.size(); row++) {
            for (int column = row + 1; column < buildings.size(); column++) {
                if (edges[row][column]) count++;
            }
        }
        return count;
    }

    public void printMatrix() {
        System.out.print("     ");
        for (String building : buildings) System.out.printf("%-6s", building);
        System.out.println();
        for (int row = 0; row < buildings.size(); row++) {
            System.out.printf("%-5s", buildings.get(row));
            for (int column = 0; column < buildings.size(); column++) {
                System.out.printf("%-6d", edges[row][column] ? 1 : 0);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CampusMatrixGraph campus = new CampusMatrixGraph(
                List.of("圖書館", "工學館", "商管大樓", "體育館", "宿舍"));

        System.out.println("[add edges]");
        System.out.println("圖書館-工學館 = " + campus.addEdge("圖書館", "工學館"));
        System.out.println("圖書館-商管大樓 = " + campus.addEdge("圖書館", "商管大樓"));
        System.out.println("工學館-體育館 = " + campus.addEdge("工學館", "體育館"));
        System.out.println("商管大樓-宿舍 = " + campus.addEdge("商管大樓", "宿舍"));
        System.out.println("重複 圖書館-工學館 = " + campus.addEdge("圖書館", "工學館"));
        System.out.println("反向 工學館-圖書館 = " + campus.addEdge("工學館", "圖書館"));
        System.out.println("自己連自己 = " + campus.addEdge("宿舍", "宿舍"));
        System.out.println("edgeCount=" + campus.edgeCount() + " (重複不重複計數)");

        System.out.println();
        campus.printMatrix();

        System.out.println();
        System.out.println("[query]");
        for (String building : List.of("圖書館", "工學館", "商管大樓", "體育館", "宿舍")) {
            System.out.println("  " + building + " degree=" + campus.degree(building)
                    + " neighbors=" + campus.neighbors(building));
        }
        System.out.println("  hasEdge(圖書館,體育館)=" + campus.hasEdge("圖書館", "體育館"));

        System.out.println();
        System.out.println("[remove]");
        System.out.println("remove(圖書館,工學館)=" + campus.removeEdge("圖書館", "工學館"));
        System.out.println("remove(圖書館,工學館) again=" + campus.removeEdge("圖書館", "工學館"));
        System.out.println("圖書館 neighbors=" + campus.neighbors("圖書館"));
        System.out.println("工學館 neighbors=" + campus.neighbors("工學館")
                + " (兩個方向同步清除)");
        System.out.println("edgeCount=" + campus.edgeCount());

        System.out.println();
        try {
            campus.degree("不存在的館");
        } catch (IllegalArgumentException e) {
            System.out.println("unknown vertex -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
