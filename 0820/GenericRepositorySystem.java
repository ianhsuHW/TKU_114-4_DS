import java.util.ArrayList;
import java.util.List;

class Repository<T> {
    private final String name;
    private final List<T> items = new ArrayList<>();

    Repository(String name) {
        this.name = name;
    }

    boolean add(T item) {
        if (item == null) {
            return false;
        }
        return items.add(item);
    }

    T get(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.get(index);
    }

    boolean remove(T item) {
        return items.remove(item);
    }

    T removeAt(int index) {
        if (index < 0 || index >= items.size()) {
            return null;
        }
        return items.remove(index);
    }

    boolean contains(T item) {
        return items.contains(item);
    }

    int size() {
        return items.size();
    }

    boolean isEmpty() {
        return items.isEmpty();
    }

    void printAll() {
        System.out.println(name + "（" + items.size() + " 筆）：");
        if (items.isEmpty()) {
            System.out.println("  （沒有資料）");
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.println("  [" + i + "] " + items.get(i));
        }
    }
}

// Product 定義在同資料夾的 Product.java，本檔直接使用。

public class GenericRepositorySystem {
    public static void main(String[] args) {
        System.out.println("=== Repository<String> ===");
        Repository<String> tags = new Repository<>("課程標籤");
        tags.printAll();
        System.out.println("add Java：" + tags.add("Java"));
        System.out.println("add 資料結構：" + tags.add("資料結構"));
        System.out.println("add 演算法：" + tags.add("演算法"));
        System.out.println("add null：" + tags.add(null));
        tags.printAll();

        System.out.println("get(1)：" + tags.get(1));
        System.out.println("get(-1)：" + tags.get(-1));
        System.out.println("get(99)：" + tags.get(99));
        // 取出即為 String，不需要 cast
        String first = tags.get(0);
        System.out.println("第一筆長度：" + first.length());

        System.out.println("remove(資料結構)：" + tags.remove("資料結構"));
        System.out.println("remove(不存在)：" + tags.remove("網路"));
        System.out.println("removeAt(0)：" + tags.removeAt(0));
        System.out.println("removeAt(99)：" + tags.removeAt(99));
        tags.printAll();
        System.out.println("size：" + tags.size());

        System.out.println();
        System.out.println("=== Repository<Product> ===");
        Repository<Product> products = new Repository<>("商品清單");
        products.add(new Product("P001", "機械鍵盤", 2480));
        products.add(new Product("P002", "無線滑鼠", 690));
        products.add(new Product("P003", "27 吋螢幕", 5990));
        products.printAll();

        // 取出即為 Product，可以直接呼叫 getPrice()
        Product second = products.get(1);
        System.out.println("第 2 筆售價：" + second.getPrice());

        System.out.println("contains(P002 同 id 的新物件)："
                + products.contains(new Product("P002", "名稱不同", 999)));
        System.out.println("remove(P002 同 id 的新物件)："
                + products.remove(new Product("P002", "名稱不同", 999)));
        System.out.println("remove(不存在的 P099)："
                + products.remove(new Product("P099", "不存在", 0)));
        products.printAll();

        System.out.println();
        System.out.println("=== 統計 ===");
        int total = 0;
        for (int i = 0; i < products.size(); i++) {
            total += products.get(i).getPrice();
        }
        System.out.println("商品總價：" + total);
        System.out.println("標籤是否為空：" + tags.isEmpty());

        System.out.println();
        System.out.println("同一個 Repository<T> 同時服務 String 與 Product，");
        System.out.println("取值不需要 cast，放錯型態在編譯階段就會被擋下來：");
        System.out.println("  // products.add(\"這是字串\");  無法編譯");
        // products.add("這是字串");
    }
}
