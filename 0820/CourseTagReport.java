import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CourseTagReport {
    public static void main(String[] args) {
        String[] input = {
            "Java", "資料結構", "Java", "演算法",
            "資料結構", "Java", "資料庫", "演算法", "Java"
        };

        List<String> ordered = new ArrayList<>();
        Set<String> unique = new LinkedHashSet<>();
        Map<String, Integer> counts = new HashMap<>();

        for (String tag : input) {
            ordered.add(tag);
            unique.add(tag);
            counts.put(tag, counts.getOrDefault(tag, 0) + 1);
        }

        System.out.println("=== 1. List：保存原始輸入順序 ===");
        System.out.println(ordered);
        System.out.println("筆數（含重複）：" + ordered.size());
        System.out.println("第 1 個標籤：" + ordered.get(0));
        System.out.println("最後一個標籤：" + ordered.get(ordered.size() - 1));
        System.out.println("用途：需要知道「使用者依序輸入了什麼」時使用，");
        System.out.println("      允許重複，而且可以用 index 取值。");

        System.out.println();
        System.out.println("=== 2. Set：保存不重複標籤 ===");
        System.out.println(unique);
        System.out.println("不重複標籤數：" + unique.size());
        System.out.println("是否包含 Java：" + unique.contains("Java"));
        System.out.println("是否包含 網路：" + unique.contains("網路"));
        System.out.println("用途：需要知道「總共出現過哪些標籤」時使用，");
        System.out.println("      自動排除重複，contains 查詢很快。");
        System.out.println("      這裡用 LinkedHashSet，所以還額外保留首次出現順序。");

        System.out.println();
        System.out.println("=== 3. Map：統計每個標籤出現次數 ===");
        for (String tag : unique) {
            System.out.println("  " + tag + "：" + counts.get(tag) + " 次");
        }
        System.out.println("Java 出現次數：" + counts.get("Java"));
        System.out.println("網路 出現次數（不存在）：" + counts.getOrDefault("網路", 0));
        System.out.println("用途：需要知道「每個標籤各出現幾次」時使用，");
        System.out.println("      key 是標籤、value 是次數，依 key 查詢很快。");

        System.out.println();
        System.out.println("=== 交叉驗證 ===");
        int sumOfCounts = 0;
        for (int count : counts.values()) {
            sumOfCounts += count;
        }
        System.out.println("Map 次數總和 = List 筆數：" + (sumOfCounts == ordered.size()));
        System.out.println("Map key 數 = Set 元素數：" + (counts.size() == unique.size()));

        System.out.println();
        System.out.println("=== 最常出現的標籤 ===");
        String hottest = null;
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            if (hottest == null || entry.getValue() > counts.get(hottest)) {
                hottest = entry.getKey();
            }
        }
        System.out.println(hottest + "，共 " + counts.get(hottest) + " 次");
    }
}
