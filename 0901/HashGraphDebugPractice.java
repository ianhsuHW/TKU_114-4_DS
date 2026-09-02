// 除錯練習（9/1）
// 練習一：相同 key 更新後 size 增加
// 練習二：自訂 key 查不到（equals 與 hashCode 不一致）
// 練習三：Undirected edge 只有單向
// 練習四：重複 add edge 造成 edge count 增加
// 練習五：Directed Graph 的 in-degree 錯誤
//
// md 沒有指定檔名，本檔把五個練習的「錯誤版」與「修正版」並列執行，
// 讓錯誤症狀可以實際重現。

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HashGraphDebugPractice {

    // ---------- 除錯練習一：相同 key 更新後 size 增加 ----------

    static final class DebugHashTable {
        private final List<List<String[]>> buckets = new ArrayList<>();
        private final boolean searchBeforeInsert;
        private int size;

        DebugHashTable(int bucketCount, boolean searchBeforeInsert) {
            this.searchBeforeInsert = searchBeforeInsert;
            for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
        }

        void put(String key, String value) {
            List<String[]> chain = buckets.get(
                    Math.floorMod(key.hashCode(), buckets.size()));
            // 錯誤版少了這段搜尋，把每次 put 都當成 insert，
            // 同一個 key 會在 chain 裡出現兩筆，size 也跟著多算
            if (searchBeforeInsert) {
                for (String[] entry : chain) {
                    if (entry[0].equals(key)) {
                        entry[1] = value;
                        return;
                    }
                }
            }
            chain.add(new String[]{key, value});
            size++;
        }

        String get(String key) {
            for (String[] entry : buckets.get(
                    Math.floorMod(key.hashCode(), buckets.size()))) {
                if (entry[0].equals(key)) return entry[1];
            }
            return null;
        }

        int size() {
            return size;
        }
    }

    private static void runExerciseOne() {
        System.out.println("[Debug 1] update counted as insert");

        DebugHashTable broken = new DebugHashTable(5, false);
        DebugHashTable fixed = new DebugHashTable(5, true);
        for (DebugHashTable table : List.of(broken, fixed)) {
            table.put("A01", "Amy");
            table.put("B02", "Ben");
            table.put("A01", "Amy2");
        }

        System.out.println("  broken size=" + broken.size()
                + " get(A01)=" + broken.get("A01"));
        System.out.println("  fixed  size=" + fixed.size()
                + " get(A01)=" + fixed.get("A01"));
        System.out.println("  symptom: 只放了 2 個 key，broken 卻算成 3，"
                + "而且 chain 裡留著舊值 Amy。");
        System.out.println();
    }

    // ---------- 除錯練習二：equals 與 hashCode 不一致 ----------

    // 錯誤：只覆寫 equals，hashCode 沿用 Object 的 identity hash。
    //       兩個 equals 為 true 的 key 會落在不同 bucket，查不回原本的 value。
    static final class BrokenKey {
        final String department;
        final String studentId;

        BrokenKey(String department, String studentId) {
            this.department = department;
            this.studentId = studentId;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof BrokenKey key)) return false;
            return department.equals(key.department) && studentId.equals(key.studentId);
        }

        @Override
        public String toString() {
            return department + "-" + studentId;
        }
    }

    // 修正：equals 與 hashCode 使用同一組欄位。
    static final class FixedKey {
        final String department;
        final String studentId;

        FixedKey(String department, String studentId) {
            this.department = department;
            this.studentId = studentId;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof FixedKey key)) return false;
            return department.equals(key.department) && studentId.equals(key.studentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(department, studentId);
        }

        @Override
        public String toString() {
            return department + "-" + studentId;
        }
    }

    private static void runExerciseTwo() {
        System.out.println("[Debug 2] equals without hashCode");

        Map<BrokenKey, String> brokenMap = new HashMap<>();
        brokenMap.put(new BrokenKey("IM", "412001"), "Amy");
        BrokenKey brokenLookup = new BrokenKey("IM", "412001");

        Map<FixedKey, String> fixedMap = new HashMap<>();
        fixedMap.put(new FixedKey("IM", "412001"), "Amy");
        FixedKey fixedLookup = new FixedKey("IM", "412001");

        System.out.println("  broken equals=" + new BrokenKey("IM", "412001")
                .equals(brokenLookup));
        System.out.println("  broken get=" + brokenMap.get(brokenLookup)
                + " containsKey=" + brokenMap.containsKey(brokenLookup));
        System.out.println("  fixed  get=" + fixedMap.get(fixedLookup)
                + " containsKey=" + fixedMap.containsKey(fixedLookup));
        System.out.println("  symptom: equals 說相等，hashCode 卻不同，"
                + "HashMap 連 bucket 都找錯。");
        System.out.println();
    }

    // ---------- 除錯練習三：Undirected edge 只有單向 ----------

    private static void runExerciseThree() {
        System.out.println("[Debug 3] undirected edge updated on one side only");

        Map<String, Set<String>> broken = newGraph();
        Map<String, Set<String>> fixed = newGraph();

        // 錯誤：只加 A 的 neighbor，B 那邊完全不知道這條 edge
        broken.get("A").add("B");

        fixed.get("A").add("B");
        fixed.get("B").add("A");

        System.out.println("  broken A=" + broken.get("A") + " B=" + broken.get("B"));
        System.out.println("  fixed  A=" + fixed.get("A") + " B=" + fixed.get("B"));
        System.out.println("  broken B contains A=" + broken.get("B").contains("A"));
        System.out.println("  symptom: 從 A 走得到 B，從 B 卻走不回 A，"
                + "走訪演算法會少掉一半路徑。");
        System.out.println();
    }

    private static Map<String, Set<String>> newGraph() {
        Map<String, Set<String>> graph = new LinkedHashMap<>();
        for (String vertex : List.of("A", "B", "C")) {
            graph.put(vertex, new LinkedHashSet<>());
        }
        return graph;
    }

    // ---------- 除錯練習四：重複 add edge 造成 edge count 增加 ----------

    private static void runExerciseFour() {
        System.out.println("[Debug 4] duplicate edges inflate the edge count");

        // 錯誤：neighbor 用 List 且未檢查重複，同一條 edge 可以加無數次
        Map<String, List<String>> brokenList = new LinkedHashMap<>();
        for (String vertex : List.of("A", "B")) brokenList.put(vertex, new ArrayList<>());
        for (int i = 0; i < 3; i++) {
            brokenList.get("A").add("B");
            brokenList.get("B").add("A");
        }

        // 修正：neighbor 用 Set，重複 add 自動被忽略
        Map<String, Set<String>> fixedSet = new LinkedHashMap<>();
        for (String vertex : List.of("A", "B")) fixedSet.put(vertex, new LinkedHashSet<>());
        for (int i = 0; i < 3; i++) {
            fixedSet.get("A").add("B");
            fixedSet.get("B").add("A");
        }

        int brokenDegreeSum = 0;
        for (List<String> neighbors : brokenList.values()) brokenDegreeSum += neighbors.size();
        int fixedDegreeSum = 0;
        for (Set<String> neighbors : fixedSet.values()) fixedDegreeSum += neighbors.size();

        System.out.println("  broken A=" + brokenList.get("A")
                + " edgeCount=" + brokenDegreeSum / 2);
        System.out.println("  fixed  A=" + fixedSet.get("A")
                + " edgeCount=" + fixedDegreeSum / 2);
        System.out.println("  symptom: 實際只有 1 條 edge，broken 卻算成 3 條。");
        System.out.println();
    }

    // ---------- 除錯練習五：Directed Graph 的 in-degree 錯誤 ----------

    // 錯誤：把自己的 outgoing size 當成 in-degree。
    static int brokenInDegree(Map<String, List<String>> outgoing, String vertex) {
        return outgoing.getOrDefault(vertex, List.of()).size();
    }

    // 修正：in-degree 必須掃描所有 vertex 的 outgoing，數有幾條指向自己。
    static int fixedInDegree(Map<String, List<String>> outgoing, String vertex) {
        int count = 0;
        for (List<String> targets : outgoing.values()) {
            for (String target : targets) {
                if (target.equals(vertex)) count++;
            }
        }
        return count;
    }

    private static void runExerciseFive() {
        System.out.println("[Debug 5] in-degree computed from outgoing size");

        Map<String, List<String>> outgoing = new LinkedHashMap<>();
        for (String vertex : List.of("A", "B", "C")) outgoing.put(vertex, new ArrayList<>());
        outgoing.get("A").add("B");     // A -> B
        outgoing.get("C").add("B");     // C -> B
        outgoing.get("A").add("C");     // A -> C

        System.out.println("  outgoing=" + outgoing);
        for (String vertex : List.of("A", "B", "C")) {
            System.out.println("  " + vertex
                    + " brokenIn=" + brokenInDegree(outgoing, vertex)
                    + " fixedIn=" + fixedInDegree(outgoing, vertex)
                    + " out=" + outgoing.get(vertex).size());
        }
        System.out.println("  symptom: B 有 2 條 incoming，broken 卻回傳 0，"
                + "因為 B 自己沒有 outgoing。");
    }

    public static void main(String[] args) {
        runExerciseOne();
        runExerciseTwo();
        runExerciseThree();
        runExerciseFour();
        runExerciseFive();
    }
}
