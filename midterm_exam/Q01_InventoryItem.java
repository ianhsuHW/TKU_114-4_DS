// 第 1 題：封裝的庫存項目
// 重點：final 欄位、constructor validation、只能透過 method 修改 stock。

public class Q01_InventoryItem {

    private final String id;
    private final String name;
    private int stock;

    public Q01_InventoryItem(String id, String name, int stock) {
        if (isBlank(id)) {
            throw new IllegalArgumentException("id 不可為 null 或空字串");
        }
        if (isBlank(name)) {
            throw new IllegalArgumentException("name 不可為 null 或空字串");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.stock = Math.max(0, stock);        // 負數庫存以 0 儲存
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public boolean restock(int amount) {
        if (amount <= 0) return false;
        stock += amount;
        return true;
    }

    public boolean sell(int amount) {
        if (amount <= 0) return false;
        if (amount > stock) return false;       // 庫存不足不修改
        stock -= amount;
        return true;
    }

    public String status() {
        return id + "|" + name + "|" + stock;
    }

    public static void main(String[] args) {
        Q01_InventoryItem item = new Q01_InventoryItem(" P100 ", " Keyboard ", 5);
        System.out.println(item.restock(3));
        System.out.println(item.sell(6));
        System.out.println(item.sell(3));
        System.out.println(item.status());

        System.out.println("--- 邊界測試 ---");
        Q01_InventoryItem negative = new Q01_InventoryItem("P200", "Mouse", -10);
        System.out.println(negative.status());
        System.out.println(negative.restock(0));
        System.out.println(negative.sell(1));
        try {
            new Q01_InventoryItem("   ", "Bad", 1);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}
