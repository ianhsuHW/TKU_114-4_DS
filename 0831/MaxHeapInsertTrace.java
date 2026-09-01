// 課堂實作題一：Max Heap Insert Trace
// 需求：自行實作 Max Heap，提供 add(int)、peekMax()、snapshot()，
//       加入每筆資料後印出陣列，重複值允許存在。
//       測試 {25,40,10,50,30,50}，完成時 root 必須是 50。

import java.util.ArrayList;
import java.util.List;

class TraceMaxHeap {
    private final List<Integer> data = new ArrayList<>();

    // 先 append 到尾端維持 Complete Binary Tree，再 bubble-up
    void add(int value) {
        data.add(value);
        System.out.println("append " + value + " -> " + data);

        int index = data.size() - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            // parent 相等時就停止，重複值才不會來回交換
            if (data.get(parent) >= data.get(index)) break;
            swap(parent, index);
            System.out.println("swap  " + parent + "," + index + " -> " + data);
            index = parent;
        }
    }

    Integer peekMax() {
        return data.isEmpty() ? null : data.get(0);
    }

    List<Integer> snapshot() {
        return List.copyOf(data);
    }

    int size() {
        return data.size();
    }

    // 每個 parent 都不小於已存在的 child 才算合法 Max Heap
    boolean isMaxHeap() {
        for (int parent = 0; parent < data.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < data.size() && data.get(parent) < data.get(left)) return false;
            if (right < data.size() && data.get(parent) < data.get(right)) return false;
        }
        return true;
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }
}

public class MaxHeapInsertTrace {
    public static void main(String[] args) {
        TraceMaxHeap heap = new TraceMaxHeap();
        System.out.println("empty peekMax=" + heap.peekMax());

        for (int value : new int[]{25, 40, 10, 50, 30, 50}) {
            heap.add(value);
        }

        System.out.println();
        System.out.println("snapshot=" + heap.snapshot());
        System.out.println("size=" + heap.size());
        System.out.println("peekMax=" + heap.peekMax());
        System.out.println("root is 50 -> " + (heap.peekMax() == 50));
        System.out.println("valid max heap=" + heap.isMaxHeap());
    }
}
