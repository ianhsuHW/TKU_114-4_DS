import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListImplementationLab {

    static void appendAll(List<Integer> list, int[] values) {
        for (int v : values) {
            list.add(v);
        }
    }

    static void insertAt(List<Integer> list, int index, int value) {
        if (index < 0 || index > list.size()) {
            System.out.println("插入失敗，index 超出範圍：" + index);
            return;
        }
        list.add(index, value);
    }

    static int search(List<Integer> list, int value) {
        return list.indexOf(value);
    }

    static boolean removeValue(List<Integer> list, int value) {
        return list.remove(Integer.valueOf(value));
    }

    static int sum(List<Integer> list) {
        int total = 0;
        for (int v : list) {
            total += v;
        }
        return total;
    }

    static void runScenario(String label, List<Integer> list) {
        System.out.println("=== " + label + " ===");

        appendAll(list, new int[] { 10, 20, 30, 40 });
        System.out.println("尾端新增後：" + list);

        insertAt(list, 2, 25);
        System.out.println("index 2 插入 25：" + list);

        insertAt(list, 99, 99);

        System.out.println("搜尋 30 的位置：" + search(list, 30));
        System.out.println("搜尋 77 的位置：" + search(list, 77));

        System.out.println("刪除 20：" + removeValue(list, 20));
        System.out.println("刪除 77：" + removeValue(list, 77));
        System.out.println("刪除後：" + list);

        System.out.println("總和：" + sum(list));
        System.out.println("元素數：" + list.size());
        System.out.println();
    }

    public static void main(String[] args) {
        runScenario("ArrayList", new ArrayList<>());
        runScenario("LinkedList", new LinkedList<>());

        System.out.println("=== 內部成本差異說明 ===");
        System.out.println("兩種 implementation 的功能結果完全一致，因為 method 只依賴 List interface。");
        System.out.println("ArrayList 底層是連續陣列：");
        System.out.println("  get(index) 直接計算位址，成本固定，O(1)。");
        System.out.println("  尾端 add 平均 O(1)，但容量滿時要配置新陣列並複製，該次成本較高。");
        System.out.println("  中間 add/remove 需要搬移後方所有元素，O(n)。");
        System.out.println("LinkedList 底層是節點串接：");
        System.out.println("  get(index) 必須從頭或尾走訪，O(n)。");
        System.out.println("  頭尾 add/remove 只改指標，O(1)，且不需要擴容複製。");
        System.out.println("  中間 add/remove 的『改指標』很便宜，但要先走訪到該位置，整體仍是 O(n)。");
        System.out.println("  每個元素多一個節點物件與兩個參考，記憶體用量較大。");
        System.out.println("結論：以 index 隨機存取為主選 ArrayList，以頭尾頻繁增刪為主選 LinkedList。");
    }
}
