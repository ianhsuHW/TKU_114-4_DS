// 課堂實作題五：Heap Validator
// 需求：提供 isMinHeap(List<Integer>) 與 isMaxHeap(List<Integer>)。
//       null 回傳 false，empty List 與單一元素回傳 true。
//       不能用排序後比較取代 parent-child 檢查。

import java.util.Arrays;
import java.util.List;

public class HeapPropertyValidator {

    // 逐一檢查每個 parent 與它實際存在的 child，
    // 只看 root 或改用排序比較都不能證明 heap invariant
    public static boolean isMinHeap(List<Integer> heap) {
        if (heap == null) return false;
        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < heap.size() && heap.get(parent) > heap.get(left)) return false;
            if (right < heap.size() && heap.get(parent) > heap.get(right)) return false;
        }
        return true;
    }

    public static boolean isMaxHeap(List<Integer> heap) {
        if (heap == null) return false;
        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < heap.size() && heap.get(parent) < heap.get(left)) return false;
            if (right < heap.size() && heap.get(parent) < heap.get(right)) return false;
        }
        return true;
    }

    private static void report(String label, List<Integer> heap) {
        System.out.printf("%-18s %-28s min=%-5s max=%s%n",
                label, heap == null ? "null" : heap.toString(),
                isMinHeap(heap), isMaxHeap(heap));
    }

    public static void main(String[] args) {
        report("null", null);
        report("empty", List.of());
        report("single", List.of(42));
        report("min heap", List.of(8, 12, 18, 45, 20, 30));
        report("max heap", List.of(50, 40, 30, 25, 10, 20));
        report("all equal", List.of(7, 7, 7, 7));
        report("bad right child", List.of(20, 30, 15));
        report("deep violation", List.of(10, 20, 30, 40, 5));
        report("sorted desc", List.of(90, 70, 50, 30));
        report("sorted asc", List.of(10, 20, 30, 40));

        // 排序過的 List 一定是 Min Heap，但 Min Heap 不一定排序過，
        // 所以「排序後比較」不能當成檢查方式
        List<Integer> unsortedButValid = Arrays.asList(8, 12, 18, 45, 20, 30);
        System.out.println();
        System.out.println("unsorted but valid min heap=" + isMinHeap(unsortedButValid));
    }
}
