// 課後作業五：整數 Hash Table
// 需求：使用 separate chaining 實作 put(int,String)、get(int)、containsKey(int)、
//       remove(int)、size() 與 bucketReport()。
//       相同 key 必須更新，size 不增加。

import java.util.ArrayList;
import java.util.List;

public class IntegerStringHashTable {

    private static final class Entry {
        final int key;
        String value;

        Entry(int key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private final List<List<Entry>> buckets = new ArrayList<>();
    private int size;

    public IntegerStringHashTable(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    // 負 key 的 hash 也是負數，必須用 floorMod 折回合法範圍
    private int index(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    // 相同 key 更新 value，size 不變；不同 key 即使 collision 也要一起保留
    public String put(int key, String value) {
        List<Entry> chain = buckets.get(index(key));
        for (Entry entry : chain) {
            if (entry.key == key) {
                String old = entry.value;
                entry.value = value;
                return old;
            }
        }
        chain.add(new Entry(key, value));
        size++;
        return null;
    }

    public String get(int key) {
        for (Entry entry : buckets.get(index(key))) {
            if (entry.key == key) return entry.value;
        }
        return null;
    }

    public boolean containsKey(int key) {
        for (Entry entry : buckets.get(index(key))) {
            if (entry.key == key) return true;
        }
        return false;
    }

    public boolean remove(int key) {
        List<Entry> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key == key) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void bucketReport() {
        System.out.println("bucketCount=" + buckets.size() + " size=" + size);
        for (int i = 0; i < buckets.size(); i++) {
            List<Entry> chain = buckets.get(i);
            System.out.println("  " + i + " -> " + chain
                    + (chain.size() > 1 ? "  <- chain length " + chain.size() : ""));
        }
    }

    public static void main(String[] args) {
        IntegerStringHashTable table = new IntegerStringHashTable(5);

        System.out.println("[put]");
        System.out.println("put(12,A) old=" + table.put(12, "A"));
        System.out.println("put(7,B)  old=" + table.put(7, "B"));
        System.out.println("put(22,C) old=" + table.put(22, "C"));   // 與 12、7 同 bucket
        System.out.println("put(-3,D) old=" + table.put(-3, "D"));   // 負 key
        System.out.println("put(40,E) old=" + table.put(40, "E"));
        System.out.println("size=" + table.size());

        System.out.println();
        System.out.println("[update same key]");
        System.out.println("put(7,B2) old=" + table.put(7, "B2"));
        System.out.println("size=" + table.size() + " (不變)");
        System.out.println("get(7)=" + table.get(7));

        System.out.println();
        table.bucketReport();

        System.out.println();
        System.out.println("[get / containsKey]");
        for (int key : new int[]{12, 7, 22, -3, 99}) {
            System.out.println("key=" + key + " contains=" + table.containsKey(key)
                    + " value=" + table.get(key));
        }

        System.out.println();
        System.out.println("[remove]");
        System.out.println("remove(12)=" + table.remove(12) + " size=" + table.size());
        System.out.println("remove(12)=" + table.remove(12) + " (已刪除)");
        System.out.println("remove(99)=" + table.remove(99) + " (不存在)");
        System.out.println("get(22)=" + table.get(22) + " (同 bucket 資料仍在)");

        System.out.println();
        table.bucketReport();
        System.out.println("isEmpty=" + table.isEmpty());
    }
}
