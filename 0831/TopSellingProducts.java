// 課後作業四：Top-K 熱門商品
// 需求：商品包含 id 與 sales。保留銷量最高 K 筆；
//       銷量相同時 id 字典序較小者優先。輸入含重複商品 id 時先合併銷量。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class TopSellingProducts {

    record Product(String id, int sales) {
        Product {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
            if (sales < 0) throw new IllegalArgumentException("sales");
        }

        @Override
        public String toString() {
            return id + "=" + sales;
        }
    }

    // 最終顯示順序：銷量由大到小，同銷量 id 字典序小的在前
    private static final Comparator<Product> BEST_FIRST = Comparator
            .comparingInt(Product::sales).reversed()
            .thenComparing(Product::id);

    // 固定大小 heap 要把「最差的」放在 head 才能丟掉，
    // 所以順序是 BEST_FIRST 的反向
    private static final Comparator<Product> WORST_FIRST = BEST_FIRST.reversed();

    // 相同 id 先合併銷量，否則同一商品會佔掉多個 Top-K 名額
    static List<Product> merge(List<Product> input) {
        Map<String, Integer> totals = new LinkedHashMap<>();
        for (Product product : input) {
            if (product == null) continue;
            totals.merge(product.id(), product.sales(), Integer::sum);
        }
        List<Product> merged = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totals.entrySet()) {
            merged.add(new Product(entry.getKey(), entry.getValue()));
        }
        return merged;
    }

    static List<Product> topK(List<Product> input, int k) {
        if (input == null || k <= 0) return List.of();

        PriorityQueue<Product> top = new PriorityQueue<>(WORST_FIRST);
        for (Product product : merge(input)) {
            top.offer(product);
            if (top.size() > k) {
                Product dropped = top.poll();
                System.out.println("  offer " + product + " drop " + dropped);
            } else {
                System.out.println("  offer " + product);
            }
        }

        List<Product> result = new ArrayList<>(top);
        result.sort(BEST_FIRST);
        return result;
    }

    public static void main(String[] args) {
        List<Product> sales = List.of(
                new Product("P-apple", 120),
                new Product("P-banana", 300),
                new Product("P-cherry", 300),
                new Product("P-apple", 200),      // 與第一筆合併成 320
                new Product("P-durian", 90),
                new Product("P-elder", 150),
                new Product("P-banana", 20),      // 合併成 320
                new Product("P-fig", 300));

        System.out.println("[merged]");
        System.out.println(merge(sales));

        System.out.println();
        System.out.println("[top 3]");
        System.out.println("result=" + topK(sales, 3));

        System.out.println();
        System.out.println("[top 5]");
        System.out.println("result=" + topK(sales, 5));

        System.out.println();
        System.out.println("k=0 -> " + topK(sales, 0));
        System.out.println("null -> " + topK(null, 3));
        System.out.println("empty -> " + topK(List.of(), 3));
    }
}
