// 課堂實作題一：可擴充 Hash Table
// 需求：使用 separate chaining；load factor 大於 0.75 時
//       將 bucket 數擴充為原本兩倍加一，重新配置全部 entry。

import java.util.ArrayList;
import java.util.List;

public class ResizableStringMap {
    private static final double MAX_LOAD_FACTOR = 0.75;

    private static final class MapEntry {
        final String key;
        String value;

        MapEntry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }

    private List<List<MapEntry>> buckets;
    private int size;

    public ResizableStringMap() {
        this(4);
    }

    public ResizableStringMap(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        buckets = createBuckets(bucketCount);
    }

    private static List<List<MapEntry>> createBuckets(int bucketCount) {
        List<List<MapEntry>> created = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) created.add(new ArrayList<>());
        return created;
    }

    // 字串 hashCode 可能是負數，一律用 floorMod 折回合法範圍
    private static int indexOf(String key, int bucketCount) {
        if (key == null) throw new IllegalArgumentException("key");
        return Math.floorMod(key.hashCode(), bucketCount);
    }

    public String put(String key, String value) {
        List<MapEntry> chain = buckets.get(indexOf(key, buckets.size()));
        for (MapEntry entry : chain) {
            if (entry.key.equals(key)) {       // 更新既有 key，size 不變也不觸發 rehash
                String old = entry.value;
                entry.value = value;
                return old;
            }
        }
        chain.add(new MapEntry(key, value));
        size++;
        if (loadFactor() > MAX_LOAD_FACTOR) rehash();
        return null;
    }

    public String get(String key) {
        for (MapEntry entry : buckets.get(indexOf(key, buckets.size()))) {
            if (entry.key.equals(key)) return entry.value;
        }
        return null;
    }

    public boolean containsKey(String key) {
        return get(key) != null;
    }

    public boolean remove(String key) {
        List<MapEntry> chain = buckets.get(indexOf(key, buckets.size()));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key.equals(key)) {
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

    public int bucketCount() {
        return buckets.size();
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    // 擴充時不能沿用舊 index，必須依新的 bucket 數重新計算每一筆 entry
    private void rehash() {
        int newBucketCount = buckets.size() * 2 + 1;
        List<List<MapEntry>> old = buckets;
        List<List<MapEntry>> resized = createBuckets(newBucketCount);
        for (List<MapEntry> chain : old) {
            for (MapEntry entry : chain) {
                resized.get(indexOf(entry.key, newBucketCount)).add(entry);
            }
        }
        buckets = resized;
        System.out.printf("  rehash %d -> %d buckets (size=%d load=%.2f)%n",
                old.size(), newBucketCount, size, loadFactor());
    }

    public void bucketReport() {
        System.out.printf("bucketCount=%d size=%d load=%.2f%n",
                buckets.size(), size, loadFactor());
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println("  " + i + " -> " + buckets.get(i));
        }
    }

    public static void main(String[] args) {
        ResizableStringMap map = new ResizableStringMap(4);

        System.out.println("[put]");
        String[][] data = {
                {"A01", "Amy"}, {"B02", "Ben"}, {"C03", "Cara"},
                {"D04", "Dan"}, {"E05", "Eve"}, {"F06", "Fay"},
                {"G07", "Gus"}, {"H08", "Hana"}};
        for (String[] pair : data) {
            map.put(pair[0], pair[1]);
            System.out.printf("put %s size=%d buckets=%d load=%.2f%n",
                    pair[0], map.size(), map.bucketCount(), map.loadFactor());
        }

        System.out.println();
        map.bucketReport();

        System.out.println();
        System.out.println("[update]");
        System.out.println("put(C03,Cara2) old=" + map.put("C03", "Cara2"));
        System.out.println("size=" + map.size() + " (更新不增加 size)");
        System.out.println("get(C03)=" + map.get("C03"));

        System.out.println();
        System.out.println("[lookup after rehash]");
        for (String[] pair : data) {
            System.out.println("  get(" + pair[0] + ")=" + map.get(pair[0]));
        }

        System.out.println();
        System.out.println("[remove]");
        System.out.println("remove(A01)=" + map.remove("A01") + " size=" + map.size());
        System.out.println("remove(A01)=" + map.remove("A01") + " (已刪除)");
        System.out.println("remove(ZZ9)=" + map.remove("ZZ9") + " (不存在)");
        System.out.println("containsKey(B02)=" + map.containsKey("B02"));
    }
}
