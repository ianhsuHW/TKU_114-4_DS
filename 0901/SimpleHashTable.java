import java.util.ArrayList;
import java.util.List;

public class SimpleHashTable<K, V> {
    private record Entry<K, V>(K key, V value) {}

    private final List<List<Entry<K, V>>> buckets;
    private int size;

    public SimpleHashTable(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    private int index(K key) {
        if (key == null) throw new IllegalArgumentException("key");
        return Math.floorMod(key.hashCode(), buckets.size());
    }

    public void put(K key, V value) {
        List<Entry<K, V>> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                chain.set(i, new Entry<>(key, value));
                return;
            }
        }
        chain.add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        for (Entry<K, V> entry : buckets.get(index(key))) {
            if (entry.key().equals(key)) return entry.value();
        }
        return null;
    }

    public boolean remove(K key) {
        List<Entry<K, V>> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
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

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    public void printBuckets() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println(i + " -> " + buckets.get(i));
        }
    }

    public static void main(String[] args) {
        SimpleHashTable<Integer, String> table = new SimpleHashTable<>(5);
        table.put(12, "A");
        table.put(7, "B");
        table.put(22, "C");
        table.put(12, "A2");
        table.printBuckets();
        System.out.println("size=" + table.size());
        System.out.println("get12=" + table.get(12));
        System.out.println("remove7=" + table.remove(7));
        System.out.printf("load=%.2f%n", table.loadFactor());
    }
}
