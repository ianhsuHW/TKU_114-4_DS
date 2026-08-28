// 第 5 題：Generic 有界資料箱
// 重點：T extends Comparable<T> 才能呼叫 compareTo()，snapshot() 必須回傳複本。

import java.util.ArrayList;
import java.util.List;

public class Q05_BoundedBox<T extends Comparable<T>> {

    private final int capacity;
    private final List<T> values = new ArrayList<>();

    public Q05_BoundedBox(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity 必須大於 0");
        }
        this.capacity = capacity;
    }

    public boolean add(T value) {
        if (value == null) return false;
        if (isFull()) return false;
        values.add(value);
        return true;
    }

    public int size() {
        return values.size();
    }

    public boolean isFull() {
        return values.size() >= capacity;
    }

    public T minimum() {
        if (values.isEmpty()) return null;
        T best = values.get(0);
        for (T value : values) {
            if (value.compareTo(best) < 0) best = value;
        }
        return best;
    }

    public T maximum() {
        if (values.isEmpty()) return null;
        T best = values.get(0);
        for (T value : values) {
            if (value.compareTo(best) > 0) best = value;
        }
        return best;
    }

    public int countGreaterThan(T threshold) {
        if (threshold == null) return 0;
        int count = 0;
        for (T value : values) {
            if (value.compareTo(threshold) > 0) count++;      // 嚴格大於
        }
        return count;
    }

    public List<T> snapshot() {
        return new ArrayList<>(values);                       // 保留加入順序的複本
    }

    public static void main(String[] args) {
        Q05_BoundedBox<Integer> box = new Q05_BoundedBox<>(3);
        System.out.println(box.add(40));
        System.out.println(box.add(10));
        System.out.println(box.add(30));
        System.out.println(box.add(20));
        System.out.println(box.minimum());
        System.out.println(box.maximum());
        System.out.println(box.countGreaterThan(25));
        System.out.println(box.snapshot());

        System.out.println("--- 邊界測試 ---");
        Q05_BoundedBox<String> empty = new Q05_BoundedBox<>(2);
        System.out.println(empty.minimum());
        System.out.println(empty.maximum());
        System.out.println(empty.add(null));
        System.out.println(empty.countGreaterThan("A"));
        System.out.println(empty.isFull());

        empty.add("banana");
        empty.add("apple");
        System.out.println(empty.snapshot() + " min=" + empty.minimum() + " full=" + empty.isFull());

        List<Integer> stolen = box.snapshot();
        stolen.clear();
        System.out.println(box.snapshot() + " size=" + box.size());
        System.out.println(box.countGreaterThan(null));

        try {
            new Q05_BoundedBox<Integer>(0);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}
