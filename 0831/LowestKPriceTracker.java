// 課堂實作題四：Top-K 最低價格
// 需求：使用固定大小 Max Heap 保留最低 K 個有效價格。
//       忽略 null 與負數；K 小於等於 0 時回傳 empty List；結果依價格遞增排列。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class LowestKPriceTracker {

    // 保留「最低 K 筆」要用 Max Heap：
    // heap 的 head 是目前候選中最大的價格，超過 K 就把它丟掉
    static List<Integer> lowestK(List<Integer> prices, int k) {
        if (prices == null || k <= 0) return List.of();

        PriorityQueue<Integer> lowest = new PriorityQueue<>(Comparator.reverseOrder());
        for (Integer price : prices) {
            if (price == null || price < 0) {
                System.out.println("skip=" + price);
                continue;
            }
            lowest.offer(price);
            if (lowest.size() > k) {
                int dropped = lowest.poll();
                System.out.println("price=" + price + " drop=" + dropped
                        + " heap head=" + lowest.peek());
            } else {
                System.out.println("price=" + price + " heap head=" + lowest.peek());
            }
        }

        List<Integer> result = new ArrayList<>(lowest);
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public static void main(String[] args) {
        List<Integer> prices = new ArrayList<>(
                List.of(320, 150, 480, 90, 250, 90, 610));
        prices.add(null);
        prices.add(-50);

        System.out.println("[lowest 3]");
        System.out.println("result=" + lowestK(prices, 3));

        System.out.println();
        System.out.println("[k larger than data]");
        System.out.println("result=" + lowestK(List.of(300, 100), 5));

        System.out.println();
        System.out.println("k=0 -> " + lowestK(prices, 0));
        System.out.println("k=-2 -> " + lowestK(prices, -2));
        System.out.println("null -> " + lowestK(null, 3));
        System.out.println("empty -> " + lowestK(List.of(), 3));
    }
}
