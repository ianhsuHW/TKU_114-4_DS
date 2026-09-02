// 課後作業四：捷運 Matrix
// 需求：建立固定站點的 undirected matrix，輸出鄰站、degree、edge count
//       與 matrix report。

import java.util.ArrayList;
import java.util.List;

public class MetroMatrixGraph {
    private final List<String> stations;
    private final boolean[][] connected;

    public MetroMatrixGraph(List<String> stations) {
        if (stations == null || stations.size() < 2) {
            throw new IllegalArgumentException("stations");
        }
        this.stations = List.copyOf(stations);
        this.connected = new boolean[stations.size()][stations.size()];
    }

    private int indexOf(String station) {
        int index = stations.indexOf(station);
        if (index < 0) throw new IllegalArgumentException("unknown station: " + station);
        return index;
    }

    // undirected：[a][b] 與 [b][a] 一起設定，matrix 才會沿對角線對稱
    public boolean connect(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (a == b || connected[a][b]) return false;
        connected[a][b] = true;
        connected[b][a] = true;
        return true;
    }

    public boolean disconnect(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        if (!connected[a][b]) return false;
        connected[a][b] = false;
        connected[b][a] = false;
        return true;
    }

    public boolean isConnected(String first, String second) {
        return connected[indexOf(first)][indexOf(second)];
    }

    public List<String> neighbors(String station) {
        int row = indexOf(station);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < stations.size(); column++) {
            if (connected[row][column]) result.add(stations.get(column));
        }
        return result;
    }

    public int degree(String station) {
        return neighbors(station).size();
    }

    // 只走上三角，一條路線不會被算成兩條
    public int edgeCount() {
        int count = 0;
        for (int row = 0; row < stations.size(); row++) {
            for (int column = row + 1; column < stations.size(); column++) {
                if (connected[row][column]) count++;
            }
        }
        return count;
    }

    // degree 1 表示端點站，degree >= 3 表示轉乘站
    public List<String> terminals() {
        List<String> result = new ArrayList<>();
        for (String station : stations) {
            if (degree(station) == 1) result.add(station);
        }
        return result;
    }

    public List<String> transferStations() {
        List<String> result = new ArrayList<>();
        for (String station : stations) {
            if (degree(station) >= 3) result.add(station);
        }
        return result;
    }

    public boolean isSymmetric() {
        for (int row = 0; row < stations.size(); row++) {
            for (int column = 0; column < stations.size(); column++) {
                if (connected[row][column] != connected[column][row]) return false;
            }
        }
        return true;
    }

    public void printMatrix() {
        System.out.printf("%-10s", "");
        for (String station : stations) System.out.printf("%-10s", station);
        System.out.println();
        for (int row = 0; row < stations.size(); row++) {
            System.out.printf("%-10s", stations.get(row));
            for (int column = 0; column < stations.size(); column++) {
                System.out.printf("%-10d", connected[row][column] ? 1 : 0);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        MetroMatrixGraph metro = new MetroMatrixGraph(
                List.of("Tamsui", "Beitou", "Shilin", "TaipeiMain", "Ximen", "Banqiao"));

        System.out.println("[connect]");
        System.out.println("Tamsui-Beitou      = " + metro.connect("Tamsui", "Beitou"));
        System.out.println("Beitou-Shilin      = " + metro.connect("Beitou", "Shilin"));
        System.out.println("Shilin-TaipeiMain  = " + metro.connect("Shilin", "TaipeiMain"));
        System.out.println("TaipeiMain-Ximen   = " + metro.connect("TaipeiMain", "Ximen"));
        System.out.println("Ximen-Banqiao      = " + metro.connect("Ximen", "Banqiao"));
        System.out.println("TaipeiMain-Banqiao = " + metro.connect("TaipeiMain", "Banqiao"));
        System.out.println("duplicate          = " + metro.connect("Beitou", "Tamsui"));
        System.out.println("self loop          = " + metro.connect("Ximen", "Ximen"));

        System.out.println();
        System.out.println("[matrix report]");
        metro.printMatrix();
        System.out.println("symmetric=" + metro.isSymmetric());
        System.out.println("edgeCount=" + metro.edgeCount());

        System.out.println();
        System.out.println("[stations]");
        for (String station : List.of("Tamsui", "Beitou", "Shilin",
                "TaipeiMain", "Ximen", "Banqiao")) {
            System.out.printf("  %-11s degree=%d neighbors=%s%n",
                    station, metro.degree(station), metro.neighbors(station));
        }
        System.out.println("  terminals=" + metro.terminals());
        System.out.println("  transfer=" + metro.transferStations());

        System.out.println();
        System.out.println("[disconnect]");
        System.out.println("disconnect(Ximen,Banqiao)=" + metro.disconnect("Ximen", "Banqiao"));
        System.out.println("disconnect again=" + metro.disconnect("Ximen", "Banqiao"));
        System.out.println("Ximen neighbors=" + metro.neighbors("Ximen"));
        System.out.println("Banqiao neighbors=" + metro.neighbors("Banqiao"));
        System.out.println("edgeCount=" + metro.edgeCount()
                + " symmetric=" + metro.isSymmetric());

        System.out.println();
        try {
            metro.neighbors("Kaohsiung");
        } catch (IllegalArgumentException e) {
            System.out.println("unknown station -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
