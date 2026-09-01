// 課後作業六：學號 Collision 分析
// 需求：輸入一組學號與 bucket count，統計每個 bucket 筆數、總 collision 次數、
//       最大 chain 與平均 chain 長度，並比較兩種 bucket count 的結果。

import java.util.ArrayList;
import java.util.List;

public class StudentIdHashAnalysis {

    record Analysis(int bucketCount, int keyCount, int usedBuckets,
                    int collisions, int maxChain, double averageChain) {
    }

    // 學號是字串，先取 hashCode 再 floorMod 才能得到合法 bucket index
    static int bucketOf(String studentId, int bucketCount) {
        return Math.floorMod(studentId.hashCode(), bucketCount);
    }

    static Analysis analyze(List<String> studentIds, int bucketCount, boolean verbose) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");

        List<List<String>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

        for (String id : studentIds) {
            if (id == null || id.isBlank()) continue;
            List<String> chain = buckets.get(bucketOf(id, bucketCount));
            if (chain.contains(id)) continue;       // 重複學號只算一筆
            chain.add(id);
        }

        int keyCount = 0;
        int usedBuckets = 0;
        int collisions = 0;
        int maxChain = 0;
        for (List<String> chain : buckets) {
            keyCount += chain.size();
            if (!chain.isEmpty()) usedBuckets++;
            // 一個 bucket 有 n 筆就代表 n-1 次 collision
            if (chain.size() > 1) collisions += chain.size() - 1;
            maxChain = Math.max(maxChain, chain.size());
        }
        // 平均 chain 長度只計算有資料的 bucket
        double averageChain = usedBuckets == 0 ? 0.0 : (double) keyCount / usedBuckets;

        if (verbose) {
            System.out.println("[bucketCount=" + bucketCount + "]");
            for (int i = 0; i < bucketCount; i++) {
                System.out.printf("  bucket %2d  count=%d  %s%n",
                        i, buckets.get(i).size(), buckets.get(i));
            }
        }
        return new Analysis(bucketCount, keyCount, usedBuckets,
                collisions, maxChain, averageChain);
    }

    static void printSummary(Analysis analysis) {
        System.out.printf(
                "  bucketCount=%-3d keys=%-3d usedBuckets=%-3d collisions=%-3d "
                        + "maxChain=%-2d averageChain=%.2f%n",
                analysis.bucketCount(), analysis.keyCount(), analysis.usedBuckets(),
                analysis.collisions(), analysis.maxChain(), analysis.averageChain());
    }

    public static void main(String[] args) {
        List<String> studentIds = List.of(
                "411630001", "411630002", "411630003", "411630004", "411630005",
                "411630006", "411630007", "411630008", "411630009", "411630010",
                "411630011", "411630012", "411630013", "411630014", "411630015",
                "411630002", "411630009");     // 兩筆重複學號

        Analysis small = analyze(studentIds, 5, true);
        System.out.println();
        Analysis large = analyze(studentIds, 16, true);

        System.out.println();
        System.out.println("[compare]");
        printSummary(small);
        printSummary(large);

        System.out.println();
        System.out.printf("collision 減少 %d 次，最大 chain 由 %d 降到 %d%n",
                small.collisions() - large.collisions(),
                small.maxChain(), large.maxChain());
        System.out.println("bucket 數越多，chain 越短，但空 bucket 也越多："
                + " usedBuckets " + small.usedBuckets() + "/" + small.bucketCount()
                + " -> " + large.usedBuckets() + "/" + large.bucketCount());

        System.out.println();
        System.out.println("[empty input]");
        printSummary(analyze(List.of(), 5, false));
    }
}
