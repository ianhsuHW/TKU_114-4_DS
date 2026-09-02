// 期末綜合練習四：資料結構使用診斷
// 需求：依測試情境判斷 List、Queue、BST、Heap、Hash Table、Graph
//       的使用是否合理，並以程式輸出診斷。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class IntegratedStructureAudit {

    // 判斷依據是「主要操作」，不是資料看起來像什麼
    public enum Operation {
        INDEX_ACCESS("依 index 讀取"),
        FIFO("先進先出"),
        LIFO("後進先出"),
        SORTED_RANGE("排序與範圍查詢"),
        NEXT_PRIORITY("反覆取最高優先"),
        KEY_LOOKUP("依 key 直接查找"),
        MEMBERSHIP("是否存在"),
        RELATION_TRAVERSAL("多對多關係走訪"),
        SHORTEST_HOPS("最少 edge 路徑");

        private final String description;

        Operation(String description) {
            this.description = description;
        }

        public String description() {
            return description;
        }
    }

    private static final Map<Operation, String> RECOMMENDED = new LinkedHashMap<>();
    private static final Map<Operation, String> BIG_O = new LinkedHashMap<>();

    static {
        RECOMMENDED.put(Operation.INDEX_ACCESS, "ArrayList");
        RECOMMENDED.put(Operation.FIFO, "ArrayDeque as Queue");
        RECOMMENDED.put(Operation.LIFO, "ArrayDeque as Stack");
        RECOMMENDED.put(Operation.SORTED_RANGE, "BST / TreeMap");
        RECOMMENDED.put(Operation.NEXT_PRIORITY, "Heap / PriorityQueue");
        RECOMMENDED.put(Operation.KEY_LOOKUP, "HashMap");
        RECOMMENDED.put(Operation.MEMBERSHIP, "HashSet");
        RECOMMENDED.put(Operation.RELATION_TRAVERSAL, "Graph adjacency list");
        RECOMMENDED.put(Operation.SHORTEST_HOPS, "Graph + BFS");

        BIG_O.put(Operation.INDEX_ACCESS, "get O(1)");
        BIG_O.put(Operation.FIFO, "offer/poll O(1)");
        BIG_O.put(Operation.LIFO, "push/pop O(1)");
        BIG_O.put(Operation.SORTED_RANGE, "平衡時 O(log n)");
        BIG_O.put(Operation.NEXT_PRIORITY, "peek O(1)、poll O(log n)");
        BIG_O.put(Operation.KEY_LOOKUP, "平均 O(1)");
        BIG_O.put(Operation.MEMBERSHIP, "平均 O(1)");
        BIG_O.put(Operation.RELATION_TRAVERSAL, "BFS/DFS O(V+E)");
        BIG_O.put(Operation.SHORTEST_HOPS, "O(V+E)");
    }

    public record Scenario(String name, Operation operation, String usedStructure) {
        public Scenario {
            if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        }
    }

    public record Diagnosis(Scenario scenario, boolean acceptable,
                            String recommended, String bigO, String comment) {
    }

    public static Diagnosis audit(Scenario scenario) {
        if (scenario == null) {
            return new Diagnosis(null, false, "-", "-", "scenario 為 null，無法診斷");
        }
        if (scenario.operation() == null) {
            return new Diagnosis(scenario, false, "-", "-", "未指定主要操作，無法判斷");
        }
        String recommended = RECOMMENDED.get(scenario.operation());
        String bigO = BIG_O.get(scenario.operation());
        String used = scenario.usedStructure();
        if (used == null || used.isBlank()) {
            return new Diagnosis(scenario, false, recommended, bigO, "未說明使用的結構");
        }
        boolean acceptable = used.equalsIgnoreCase(recommended);
        String comment = acceptable
                ? "與主要操作相符"
                : "主要操作是「" + scenario.operation().description()
                        + "」，" + used + " 需要額外掃描或排序";
        return new Diagnosis(scenario, acceptable, recommended, bigO, comment);
    }

    public static List<Diagnosis> auditAll(List<Scenario> scenarios) {
        List<Diagnosis> results = new ArrayList<>();
        if (scenarios == null) return results;
        for (Scenario scenario : scenarios) results.add(audit(scenario));
        return results;
    }

    public static int problemCount(List<Diagnosis> diagnoses) {
        int count = 0;
        for (Diagnosis diagnosis : diagnoses) {
            if (!diagnosis.acceptable()) count++;
        }
        return count;
    }

    public static void printDiagnoses(List<Diagnosis> diagnoses) {
        if (diagnoses.isEmpty()) {
            System.out.println("  (no scenario)");
            return;
        }
        for (Diagnosis diagnosis : diagnoses) {
            String name = diagnosis.scenario() == null ? "(null)" : diagnosis.scenario().name();
            String used = diagnosis.scenario() == null ? "-"
                    : String.valueOf(diagnosis.scenario().usedStructure());
            System.out.printf("  %-22s used=%-22s %s%n",
                    name, used, diagnosis.acceptable() ? "OK" : "REVIEW");
            System.out.println("      recommended=" + diagnosis.recommended()
                    + "  bigO=" + diagnosis.bigO());
            System.out.println("      " + diagnosis.comment());
        }
    }

    private static List<Scenario> sampleScenarios() {
        List<Scenario> scenarios = new ArrayList<>();
        scenarios.add(new Scenario("商品清單依位置讀取",
                Operation.INDEX_ACCESS, "ArrayList"));
        scenarios.add(new Scenario("櫃檯排隊叫號",
                Operation.FIFO, "ArrayDeque as Queue"));
        scenarios.add(new Scenario("上一頁功能",
                Operation.LIFO, "ArrayList"));                  // 不合理
        scenarios.add(new Scenario("成績範圍查詢",
                Operation.SORTED_RANGE, "BST / TreeMap"));
        scenarios.add(new Scenario("每次取最急工單",
                Operation.NEXT_PRIORITY, "ArrayList"));         // 不合理：每次都要掃描
        scenarios.add(new Scenario("依學號查學生",
                Operation.KEY_LOOKUP, "HashMap"));
        scenarios.add(new Scenario("檢查是否已報名",
                Operation.MEMBERSHIP, "ArrayList"));            // 不合理：contains O(n)
        scenarios.add(new Scenario("好友關係走訪",
                Operation.RELATION_TRAVERSAL, "Graph adjacency list"));
        scenarios.add(new Scenario("最少轉乘路線",
                Operation.SHORTEST_HOPS, "Heap / PriorityQueue"));  // 無權重不需要 Heap
        scenarios.add(new Scenario("未填寫結構",
                Operation.KEY_LOOKUP, ""));                     // 邊界：空字串
        scenarios.add(new Scenario("未指定操作", null, "HashMap"));  // 邊界：null operation
        return scenarios;
    }

    public static void main(String[] args) {
        List<Scenario> scenarios = sampleScenarios();
        List<Diagnosis> diagnoses = auditAll(scenarios);

        System.out.println("[audit]");
        printDiagnoses(diagnoses);

        System.out.println();
        System.out.println("total=" + diagnoses.size()
                + " review=" + problemCount(diagnoses)
                + " ok=" + (diagnoses.size() - problemCount(diagnoses)));

        System.out.println();
        System.out.println("[reference table]");
        for (Operation operation : Operation.values()) {
            System.out.printf("  %-20s %-22s %s%n",
                    operation.description(), RECOMMENDED.get(operation), BIG_O.get(operation));
        }

        System.out.println();
        System.out.println("[boundary cases]");
        printDiagnoses(List.of(audit(null)));
        printDiagnoses(auditAll(List.of()));
        System.out.println("  auditAll(null)=" + auditAll(null));
    }
}
