// 課後作業三：可調整容量 Min Heap
// 需求：使用自行管理的 int[]，不可使用 PriorityQueue。
//       容量不足時擴充為兩倍，支援 add、remove、peek 與 snapshot，
//       測試至少 20 筆資料。

import java.util.Arrays;
import java.util.NoSuchElementException;

public class ArrayMinHeap {
    private int[] data;
    private int size;

    public ArrayMinHeap() {
        this(4);
    }

    public ArrayMinHeap(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity");
        data = new int[capacity];
    }

    public void add(int value) {
        if (size == data.length) grow();
        data[size] = value;
        bubbleUp(size);
        size++;
    }

    public int peek() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        return data[0];
    }

    public int remove() {
        if (size == 0) throw new NoSuchElementException("heap is empty");
        int result = data[0];
        size--;
        // 用最後一個值補上 root，再往下修正；
        // 只剩一個元素時 size 已歸零，不需要 bubble-down
        if (size > 0) {
            data[0] = data[size];
            bubbleDown(0);
        }
        data[size] = 0;
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int capacity() {
        return data.length;
    }

    public int[] snapshot() {
        return Arrays.copyOf(data, size);
    }

    private void grow() {
        int newCapacity = data.length * 2;
        data = Arrays.copyOf(data, newCapacity);
        System.out.println("  grow capacity -> " + newCapacity);
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data[parent] <= data[index]) break;
            swap(parent, index);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= size) return;

            int smaller = left;
            if (right < size && data[right] < data[left]) smaller = right;
            if (data[index] <= data[smaller]) return;
            swap(index, smaller);
            index = smaller;
        }
    }

    private void swap(int first, int second) {
        int temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    private boolean isMinHeap() {
        for (int parent = 0; parent < size; parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < size && data[parent] > data[left]) return false;
            if (right < size && data[parent] > data[right]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        ArrayMinHeap heap = new ArrayMinHeap(4);

        System.out.println("[empty]");
        System.out.println("size=" + heap.size() + " capacity=" + heap.capacity());
        try {
            heap.peek();
        } catch (NoSuchElementException e) {
            System.out.println("peek() -> NoSuchElementException: " + e.getMessage());
        }
        try {
            heap.remove();
        } catch (NoSuchElementException e) {
            System.out.println("remove() -> NoSuchElementException: " + e.getMessage());
        }

        int[] values = {45, 12, 30, 8, 20, 18, 77, 3, 64, 51,
                        29, 14, 90, 5, 33, 41, 60, 22, 11, 38,
                        7, 26};

        System.out.println();
        System.out.println("[add " + values.length + " values]");
        for (int value : values) {
            heap.add(value);
            System.out.println("add " + value + " size=" + heap.size()
                    + " capacity=" + heap.capacity() + " min=" + heap.peek());
        }

        System.out.println();
        System.out.println("snapshot=" + Arrays.toString(heap.snapshot()));
        System.out.println("valid=" + heap.isMinHeap());

        System.out.println();
        System.out.println("[remove all]");
        int[] removed = new int[values.length];
        int count = 0;
        while (!heap.isEmpty()) {
            removed[count++] = heap.remove();
        }
        System.out.println("removed=" + Arrays.toString(removed));

        int[] expected = Arrays.copyOf(values, values.length);
        Arrays.sort(expected);
        System.out.println("sorted =" + Arrays.toString(expected));
        System.out.println("match=" + Arrays.equals(removed, expected));
        System.out.println("size=" + heap.size() + " capacity=" + heap.capacity());
    }
}
