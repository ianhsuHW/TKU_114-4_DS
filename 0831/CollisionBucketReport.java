// 課堂實作題六：Collision Bucket Report
// 需求：將整數 key 放入固定數量 bucket，輸出每個 bucket 的 key 清單、
//       collision 數量與最長 chain。必須正確處理負 key、重複 key 與空輸入。

import java.util.ArrayList;
import java.util.List;

public class CollisionBucketReport {
    private final List<List<Integer>> buckets = new ArrayList<>();

    public CollisionBucketReport(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    // hash value 可能是負數，必須用 floorMod 轉成合法 index
    private int index(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    // 重複 key 視為同一筆資料，不再加入 chain
    public boolean add(int key) {
        List<Integer> chain = buckets.get(index(key));
        if (chain.contains(key)) return false;
        chain.add(key);
        return true;
    }

    public int keyCount() {
        int total = 0;
        for (List<Integer> chain : buckets) total += chain.size();
        return total;
    }

    // 一個 bucket 有 n 筆資料就代表 n-1 次 collision
    public int collisionCount() {
        int collisions = 0;
        for (List<Integer> chain : buckets) {
            if (chain.size() > 1) collisions += chain.size() - 1;
        }
        return collisions;
    }

    public int longestChain() {
        int longest = 0;
        for (List<Integer> chain : buckets) longest = Math.max(longest, chain.size());
        return longest;
    }

    public int usedBuckets() {
        int used = 0;
        for (List<Integer> chain : buckets) {
            if (!chain.isEmpty()) used++;
        }
        return used;
    }

    public void printReport(String label) {
        System.out.println("[" + label + "] bucketCount=" + buckets.size());
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("  " + i + " -> " + buckets.get(i)
                    + (buckets.get(i).size() > 1 ? "  <- collision" : ""));
        }
        System.out.println("  keys=" + keyCount()
                + " usedBuckets=" + usedBuckets()
                + " collisions=" + collisionCount()
                + " longestChain=" + longestChain());
        System.out.println();
    }

    private static CollisionBucketReport build(int bucketCount, int[] keys) {
        CollisionBucketReport table = new CollisionBucketReport(bucketCount);
        for (int key : keys) {
            boolean added = table.add(key);
            if (!added) System.out.println("  duplicate key ignored: " + key);
        }
        return table;
    }

    public static void main(String[] args) {
        int[] keys = {12, 7, 22, -3, 7, 40, 18, -13, 5};

        build(5, keys).printReport("sample keys");
        build(8, keys).printReport("sample keys, more buckets");
        build(3, new int[]{}).printReport("empty input");

        try {
            new CollisionBucketReport(0);
        } catch (IllegalArgumentException e) {
            System.out.println("bucketCount=0 -> IllegalArgumentException: "
                    + e.getMessage());
        }
    }
}
