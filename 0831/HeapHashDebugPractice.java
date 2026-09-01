// 除錯練習（8/31）
// 練習一：Bubble-Down 選錯 Child
// 練習二：負數 Hash Index
// 練習三：直接列印 PriorityQueue
//
// md 沒有指定檔名，本檔把三個練習的「錯誤版」與「修正版」並列執行，
// 讓錯誤症狀可以實際重現。

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HeapHashDebugPractice {

    // ---------- 除錯練習一：Bubble-Down 選錯 Child ----------

    // 錯誤：只跟 left child 比較。[20,30,15] 的 left child 是 30，
    //       20 <= 30 就停止，但 right child 15 已經違反 Min Heap。
    static void brokenBubbleDown(List<Integer> heap, int index) {
        while (true) {
            int left = index * 2 + 1;
            if (left >= heap.size()) return;
            if (heap.get(index) <= heap.get(left)) return;
            swap(heap, index, left);
            index = left;
        }
    }

    // 修正：兩個 child 都存在時先選較小的，再決定是否交換。
    static void fixedBubbleDown(List<Integer> heap, int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= heap.size()) return;

            int smaller = left;
            if (right < heap.size() && heap.get(right) < heap.get(left)) smaller = right;
            if (heap.get(index) <= heap.get(smaller)) return;
            swap(heap, index, smaller);
            index = smaller;
        }
    }

    static boolean isMinHeap(List<Integer> heap) {
        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < heap.size() && heap.get(parent) > heap.get(left)) return false;
            if (right < heap.size() && heap.get(parent) > heap.get(right)) return false;
        }
        return true;
    }

    private static void swap(List<Integer> heap, int first, int second) {
        int temp = heap.get(first);
        heap.set(first, heap.get(second));
        heap.set(second, temp);
    }

    private static void runExerciseOne() {
        System.out.println("[Debug 1] bubble-down picks the wrong child");

        List<Integer> broken = new ArrayList<>(List.of(20, 30, 15));
        List<Integer> fixed = new ArrayList<>(List.of(20, 30, 15));
        System.out.println("  before=" + broken);

        brokenBubbleDown(broken, 0);
        fixedBubbleDown(fixed, 0);

        System.out.println("  broken=" + broken + " valid=" + isMinHeap(broken));
        System.out.println("  fixed =" + fixed + " valid=" + isMinHeap(fixed));
        System.out.println("  symptom: 只比 left child 30，right child 15 比 root 小卻沒有交換。");

        // 深一層的資料同樣會停在錯誤位置
        List<Integer> deepBroken = new ArrayList<>(List.of(50, 12, 8, 45, 20, 18, 30));
        List<Integer> deepFixed = new ArrayList<>(deepBroken);
        brokenBubbleDown(deepBroken, 0);
        fixedBubbleDown(deepFixed, 0);
        System.out.println("  deep broken=" + deepBroken + " valid=" + isMinHeap(deepBroken));
        System.out.println("  deep fixed =" + deepFixed + " valid=" + isMinHeap(deepFixed));
        System.out.println();
    }

    // ---------- 除錯練習二：負數 Hash Index ----------

    // 錯誤：Java 的 % 會保留被除數的正負號，負 key 得到負 index，
    //       拿去存取 List 會直接丟出 IndexOutOfBoundsException。
    static int brokenIndex(int key, int bucketCount) {
        return Integer.hashCode(key) % bucketCount;
    }

    // 修正：floorMod 一定回傳 0 到 bucketCount-1 的值。
    static int fixedIndex(int key, int bucketCount) {
        return Math.floorMod(Integer.hashCode(key), bucketCount);
    }

    private static void runExerciseTwo() {
        System.out.println("[Debug 2] negative hash index");
        int bucketCount = 5;

        List<List<Integer>> buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());

        for (int key : new int[]{12, 7, -3, -13}) {
            System.out.println("  key=" + key
                    + " brokenIndex=" + brokenIndex(key, bucketCount)
                    + " fixedIndex=" + fixedIndex(key, bucketCount));
        }

        try {
            int key = -3;
            buckets.get(brokenIndex(key, bucketCount)).add(key);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("  broken put(-3) -> IndexOutOfBoundsException: "
                    + e.getMessage());
        }

        for (int key : new int[]{12, 7, -3, -13}) {
            buckets.get(fixedIndex(key, bucketCount)).add(key);
        }
        System.out.println("  fixed buckets=" + buckets);
        System.out.println();
    }

    // ---------- 除錯練習三：直接列印 PriorityQueue ----------

    private static void runExerciseThree() {
        System.out.println("[Debug 3] printing a PriorityQueue proves nothing");

        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int value : new int[]{40, 10, 30, 20, 5, 35}) queue.offer(value);

        // 錯誤：toString() 與 iterator 只反映內部 heap 陣列，不是排序結果
        System.out.println("  toString=" + queue);

        List<Integer> iterated = new ArrayList<>();
        for (Integer value : queue) iterated.add(value);
        System.out.println("  iterator=" + iterated);

        // 修正：複製一份後反覆 poll()，才是真正的取出順序
        PriorityQueue<Integer> copy = new PriorityQueue<>(queue);
        List<Integer> pollOrder = new ArrayList<>();
        while (!copy.isEmpty()) pollOrder.add(copy.poll());
        System.out.println("  poll order=" + pollOrder);
        System.out.println("  original size unchanged=" + queue.size());
        System.out.println("  symptom: toString 的順序與 poll 順序不同，不能拿來驗證。");
    }

    public static void main(String[] args) {
        runExerciseOne();
        runExerciseTwo();
        runExerciseThree();
    }
}
