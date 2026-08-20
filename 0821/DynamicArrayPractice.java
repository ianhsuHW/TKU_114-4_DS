import java.util.Arrays;

class DynamicArray<T> {
    private Object[] data;
    private int size;

    DynamicArray() {
        this(2);
    }

    DynamicArray(int capacity) {
        data = new Object[Math.max(1, capacity)];
    }

    void add(T value) {
        ensureCapacity();
        data[size] = value;
        size++;
    }

    void add(int index, T value) {
        if (index < 0 || index > size) {
            System.out.println("add 失敗，index 超出範圍：" + index);
            return;
        }
        ensureCapacity();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    T get(int index) {
        if (!isValidIndex(index)) {
            System.out.println("get 失敗，index 超出範圍：" + index);
            return null;
        }
        return (T) data[index];
    }

    @SuppressWarnings("unchecked")
    T set(int index, T value) {
        if (!isValidIndex(index)) {
            System.out.println("set 失敗，index 超出範圍：" + index);
            return null;
        }
        T old = (T) data[index];
        data[index] = value;
        return old;
    }

    @SuppressWarnings("unchecked")
    T remove(int index) {
        if (!isValidIndex(index)) {
            System.out.println("remove 失敗，index 超出範圍：" + index);
            return null;
        }
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        size--;
        data[size] = null;
        return removed;
    }

    int size() {
        return size;
    }

    int capacity() {
        return data.length;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }

    private void ensureCapacity() {
        if (size < data.length) {
            return;
        }
        int newCapacity = data.length * 2;
        data = Arrays.copyOf(data, newCapacity);
        System.out.println("  [擴容] 容量 " + (newCapacity / 2) + " -> " + newCapacity);
    }

    @Override
    public String toString() {
        return "內容=" + Arrays.toString(Arrays.copyOf(data, size))
                + " 底層=" + Arrays.toString(data)
                + " size=" + size + " capacity=" + capacity();
    }
}

public class DynamicArrayPractice {
    public static void main(String[] args) {
        System.out.println("=== DynamicArray<String> ===");
        DynamicArray<String> names = new DynamicArray<>(2);
        names.add("Amy");
        names.add("Ben");
        names.add("Cindy");
        System.out.println(names);

        names.add(1, "Bob");
        System.out.println("index 1 插入 Bob：" + names);

        System.out.println("get(2)：" + names.get(2));
        System.out.println("set(0, Anna) 回傳舊值：" + names.set(0, "Anna"));
        System.out.println("remove(3) 回傳：" + names.remove(3));
        System.out.println(names);

        System.out.println();
        System.out.println("=== 邊界測試 ===");
        System.out.println("get(-1)：" + names.get(-1));
        System.out.println("get(size)：" + names.get(names.size()));
        System.out.println("set(-1, X)：" + names.set(-1, "X"));
        System.out.println("add(-1, X)：");
        names.add(-1, "X");
        System.out.println("add(size, Zoe)：");
        names.add(names.size(), "Zoe");
        System.out.println(names);

        System.out.println();
        System.out.println("=== 空結構刪除 ===");
        DynamicArray<String> empty = new DynamicArray<>();
        System.out.println("remove(0)：" + empty.remove(0));
        System.out.println(empty);

        System.out.println();
        System.out.println("=== DynamicArray<Integer> ===");
        DynamicArray<Integer> scores = new DynamicArray<>(1);
        for (int i = 1; i <= 5; i++) {
            scores.add(i * 10);
        }
        System.out.println(scores);
        System.out.println("remove(0) 回傳：" + scores.remove(0));
        System.out.println("remove(size-1) 回傳：" + scores.remove(scores.size() - 1));
        System.out.println(scores);

        int total = 0;
        for (int i = 0; i < scores.size(); i++) {
            total += scores.get(i);
        }
        System.out.println("總和：" + total);
    }
}
