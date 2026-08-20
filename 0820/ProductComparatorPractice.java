import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class StoreProduct implements Comparable<StoreProduct> {
    private final String id;
    private final String name;
    private final int price;
    private final int stock;

    StoreProduct(String id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = Math.max(0, price);
        this.stock = Math.max(0, stock);
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    int getStock() {
        return stock;
    }

    // Natural order：依 id 升冪
    @Override
    public int compareTo(StoreProduct other) {
        return id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return id + " " + name + " 價格=" + price + " 庫存=" + stock;
    }
}

public class ProductComparatorPractice {

    static void print(String title, List<StoreProduct> products) {
        System.out.println(title);
        for (StoreProduct product : products) {
            System.out.println("  " + product);
        }
    }

    public static void main(String[] args) {
        List<StoreProduct> products = new ArrayList<>(Arrays.asList(
            new StoreProduct("P003", "無線滑鼠", 690, 12),
            new StoreProduct("P001", "機械鍵盤", 2480, 5),
            new StoreProduct("P005", "USB 集線器", 690, 12),
            new StoreProduct("P002", "27 吋螢幕", 5990, 3),
            new StoreProduct("P004", "筆電支架", 890, 5)
        ));

        print("=== 原始順序 ===", products);

        System.out.println();
        List<StoreProduct> byId = new ArrayList<>(products);
        byId.sort(null);
        print("=== 1. Natural order：依 id 升冪（Comparable）===", byId);

        System.out.println();
        List<StoreProduct> byPrice = new ArrayList<>(products);
        byPrice.sort(Comparator.comparingInt(StoreProduct::getPrice)
                .thenComparing(StoreProduct::getName));
        print("=== 2. Comparator 一：price 升冪，同價依 name ===", byPrice);
        System.out.println("  P003 與 P005 同為 690 元，依名稱決定先後。");

        System.out.println();
        List<StoreProduct> byStock = new ArrayList<>(products);
        byStock.sort(Comparator.comparingInt(StoreProduct::getStock).reversed()
                .thenComparing(StoreProduct::getId));
        print("=== 3. Comparator 二：stock 降冪，同庫存依 id ===", byStock);
        System.out.println("  P001 與 P004 同為 5 件，依 id 決定先後。");
        System.out.println("  P003 與 P005 同為 12 件，依 id 決定先後。");

        System.out.println();
        print("=== 4. 原始 list 完全沒有被改動 ===", products);
        System.out.println("原始第一筆仍為：" + products.get(0));
        System.out.println("每次排序都先 new ArrayList<>(products) 建立 copy，");
        System.out.println("所以三種排序結果彼此獨立，原始順序也保留得下來。");

        System.out.println();
        System.out.println("=== Comparable 與 Comparator 的差別 ===");
        System.out.println("Comparable 寫在 StoreProduct 內部，只能有一種 natural order（依 id）。");
        System.out.println("Comparator 寫在外部，可以視需要建立任意多種排序規則，");
        System.out.println("而且用 thenComparing 就能處理同分時的第二排序條件。");
    }
}
