import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeightedDirectedGraph {
    public record Edge(String to, int weight) {
        public Edge {
            if (to == null || to.isBlank()) throw new IllegalArgumentException("to");
            if (weight < 0) throw new IllegalArgumentException("weight");
        }
    }

    private final Map<String, List<Edge>> outgoing = new LinkedHashMap<>();

    public void addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) throw new IllegalArgumentException("vertex");
        outgoing.putIfAbsent(vertex, new ArrayList<>());
    }

    public boolean addEdge(String from, String to, int weight) {
        if (!outgoing.containsKey(from) || !outgoing.containsKey(to)) return false;
        List<Edge> edges = outgoing.get(from);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).to().equals(to)) {
                edges.set(i, new Edge(to, weight));
                return false;
            }
        }
        edges.add(new Edge(to, weight));
        return true;
    }

    public int outDegree(String vertex) {
        return outgoing.getOrDefault(vertex, List.of()).size();
    }

    public int inDegree(String vertex) {
        if (!outgoing.containsKey(vertex)) return 0;
        int count = 0;
        for (List<Edge> edges : outgoing.values()) {
            for (Edge edge : edges) if (edge.to().equals(vertex)) count++;
        }
        return count;
    }

    public List<Edge> outgoingFrom(String vertex) {
        return List.copyOf(outgoing.getOrDefault(vertex, List.of()));
    }

    public static void main(String[] args) {
        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        for (String vertex : List.of("A", "B", "C")) graph.addVertex(vertex);
        graph.addEdge("A", "B", 5);
        graph.addEdge("C", "B", 2);
        graph.addEdge("A", "C", 4);
        System.out.println("A outgoing=" + graph.outgoingFrom("A"));
        System.out.println("B in=" + graph.inDegree("B"));
        System.out.println("B out=" + graph.outDegree("B"));
    }
}
