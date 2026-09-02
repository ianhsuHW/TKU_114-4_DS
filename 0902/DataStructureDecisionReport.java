// 課堂實作題六：Data Structure Decision Report
// 需求：依 12 組需求輸出選擇、理由與主要 Big-O。
//       含一般案例與 missing/empty 邊界案例。

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataStructureDecisionReport {

    public record Decision(String structure, String reason, String bigO) {
        @Override
        public String toString() {
            return structure + " | " + bigO + " | " + reason;
        }
    }

    // 資料結構不是依名稱選，而是依「主要操作」選，
    // 因此每個需求都直接對應到它最頻繁的那個操作
    private static final Map<String, Decision> TABLE = new LinkedHashMap<>();

    static {
        TABLE.put("依 index 讀取商品清單",
                new Decision("ArrayList", "主要操作是依位置讀取，不常在中間插入",
                        "get O(1)，中間插入 O(n)"));
        TABLE.put("排隊叫號（先進先出）",
                new Decision("ArrayDeque as Queue", "只在兩端操作，不需要隨機存取",
                        "offer/poll O(1)"));
        TABLE.put("瀏覽紀錄上一頁（後進先出）",
                new Decision("ArrayDeque as Stack", "最後放入的最先取出",
                        "push/pop O(1)"));
        TABLE.put("成績範圍查詢與排序輸出",
                new Decision("Balanced BST / TreeMap", "需要排序與 range query",
                        "平衡時 O(log n)，最差 O(n)"));
        TABLE.put("反覆取出最高優先工作",
                new Decision("Heap / PriorityQueue", "只需要目前極值，不需要完整排序",
                        "peek O(1)，add/remove O(log n)"));
        TABLE.put("依學號直接查詢學生",
                new Decision("HashMap", "單一 key 直接定位，不需要順序",
                        "平均 O(1)，最差 O(n)"));
        TABLE.put("保存好友多對多關係並走訪",
                new Decision("Graph adjacency list", "關係是多對多，且以走訪為主",
                        "BFS/DFS O(V+E)"));
        TABLE.put("判斷學號是否已報名",
                new Decision("HashSet", "只在意是否存在，不需要 value",
                        "add/contains 平均 O(1)"));
        TABLE.put("保留最大 K 筆銷量",
                new Decision("固定大小 Heap", "只保留候選集合，不必排序全部資料",
                        "每筆 O(log K)"));
        TABLE.put("依課號累積多筆成績",
                new Decision("HashMap<String,List<Integer>>", "key 對應多筆值",
                        "查 key 平均 O(1)，append O(1)"));
        TABLE.put("找出最少轉乘路線",
                new Decision("Graph + BFS", "無權重最短路徑由 BFS 保證",
                        "O(V+E)"));
        TABLE.put("保留插入順序的設定項目",
                new Decision("LinkedHashMap", "需要固定的顯示順序",
                        "查 key 平均 O(1)，走訪 O(n)"));
    }

    private static final Decision UNKNOWN =
            new Decision("UNKNOWN", "需求未定義，無法選擇結構", "-");

    public static Decision decide(String requirement) {
        if (requirement == null || requirement.isBlank()) return UNKNOWN;
        return TABLE.getOrDefault(requirement.trim(), UNKNOWN);
    }

    public static List<String> requirements() {
        return new ArrayList<>(TABLE.keySet());
    }

    public static int requirementCount() {
        return TABLE.size();
    }

    public static void printReport(List<String> requirements) {
        if (requirements == null || requirements.isEmpty()) {
            System.out.println("  (no requirement)");
            return;
        }
        for (String requirement : requirements) {
            Decision decision = decide(requirement);
            System.out.printf("  %-22s -> %-28s %s%n",
                    requirement, decision.structure(), decision.bigO());
            System.out.println("      理由：" + decision.reason());
        }
    }

    public static void main(String[] args) {
        System.out.println("[全部 " + requirementCount() + " 組需求]");
        printReport(requirements());

        System.out.println();
        System.out.println("[單筆查詢]");
        for (String requirement : List.of("依學號直接查詢學生", "找出最少轉乘路線")) {
            System.out.println("  " + requirement + " -> " + decide(requirement));
        }

        System.out.println();
        System.out.println("[boundary cases]");
        System.out.println("  未登錄需求 -> " + decide("預測明天股價"));
        System.out.println("  null       -> " + decide(null));
        System.out.println("  blank      -> " + decide("   "));
        System.out.println("  前後空白仍可對應 -> "
                + decide("  依學號直接查詢學生  ").structure());

        System.out.println();
        System.out.println("[empty / null 需求清單]");
        printReport(List.of());
        printReport(null);
    }
}
