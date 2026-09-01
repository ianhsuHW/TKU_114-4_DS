// 課堂實作題二：Min Heap 完整操作
// 需求：實作 add()、peek()、removeMin()、size()、isEmpty()。
//       空 Heap 的 peek() 與 removeMin() 必須丟出 NoSuchElementException，
//       並驗證移除結果為非遞減順序。

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class IntegerMinHeap {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        bubbleUp(data.size() - 1);
    }

    public int peek() {
        if (data.isEmpty()) throw new NoSuchElementException("heap is empty");
        return data.get(0);
    }

    public int removeMin() {
        if (data.isEmpty()) throw new NoSuchElementException("heap is empty");
        int result = data.get(0);
        int last = data.remove(data.size() - 1);
        // 只剩一個元素時已經整個移除，不能再寫回 index 0
        if (!data.isEmpty()) {
            data.set(0, last);
            bubbleDown(0);
        }
        return result;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= data.size()) return;

            // 兩個 child 都存在時必須選較小的那一個
            int smaller = left;
            if (right < data.size() && data.get(right) < data.get(left)) smaller = right;
            if (data.get(index) <= data.get(smaller)) return;
            swap(index, smaller);
            index = smaller;
        }
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    private static void expectException(String label, Runnable action) {
        try {
            action.run();
            System.out.println(label + " -> no exception (WRONG)");
        } catch (NoSuchElementException e) {
            System.out.println(label + " -> NoSuchElementException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        IntegerMinHeap heap = new IntegerMinHeap();

        System.out.println("[empty heap]");
        System.out.println("size=" + heap.size() + " isEmpty=" + heap.isEmpty());
        expectException("peek()", heap::peek);
        expectException("removeMin()", heap::removeMin);

        System.out.println();
        System.out.println("[add]");
        for (int value : new int[]{35, 20, 45, 20, 10, 60, 15}) {
            heap.add(value);
            System.out.println("add " + value + " -> " + heap.snapshot()
                    + " peek=" + heap.peek());
        }

        System.out.println();
        System.out.println("[removeMin]");
        List<Integer> removed = new ArrayList<>();
        while (!heap.isEmpty()) {
            int min = heap.removeMin();
            removed.add(min);
            System.out.println("remove=" + min + " remaining=" + heap.snapshot());
        }

        boolean sorted = true;
        for (int i = 1; i < removed.size(); i++) {
            if (removed.get(i - 1) > removed.get(i)) sorted = false;
        }

        System.out.println();
        System.out.println("removed=" + removed);
        System.out.println("non-decreasing=" + sorted);
        System.out.println("size=" + heap.size() + " isEmpty=" + heap.isEmpty());
        expectException("removeMin() after drain", heap::removeMin);
    }
}
