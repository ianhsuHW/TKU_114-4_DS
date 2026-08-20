import java.util.Arrays;

final class InventorySnapshot {
    private final String warehouseId;
    private final int[] quantities;

    InventorySnapshot(String warehouseId, int[] quantities) {
        this.warehouseId = warehouseId;
        this.quantities = (quantities == null)
                ? new int[0]
                : Arrays.copyOf(quantities, quantities.length);
    }

    String getWarehouseId() {
        return warehouseId;
    }

    int[] getQuantities() {
        return Arrays.copyOf(quantities, quantities.length);
    }

    int totalQuantity() {
        int total = 0;
        for (int q : quantities) {
            total += q;
        }
        return total;
    }

    int outOfStockCount() {
        int count = 0;
        for (int q : quantities) {
            if (q == 0) {
                count++;
            }
        }
        return count;
    }

    @Override
    public String toString() {
        return warehouseId + " " + Arrays.toString(quantities)
                + " total=" + totalQuantity()
                + " outOfStock=" + outOfStockCount();
    }
}

public class InventorySnapshotPractice {
    public static void main(String[] args) {
        int[] source = { 5, 0, 3, 0 };
        InventorySnapshot snapshot = new InventorySnapshot("W-01", source);

        System.out.println("=== 基本結果 ===");
        System.out.println(snapshot);
        System.out.println("totalQuantity()：" + snapshot.totalQuantity() + "（預期 8）");
        System.out.println("outOfStockCount()：" + snapshot.outOfStockCount() + "（預期 2）");

        System.out.println();
        System.out.println("=== Constructor defensive copy ===");
        source[0] = 999;
        System.out.println("修改外部陣列後 source：" + Arrays.toString(source));
        System.out.println("snapshot 內容不受影響：" + Arrays.toString(snapshot.getQuantities()));
        System.out.println("總數仍為：" + snapshot.totalQuantity());

        System.out.println();
        System.out.println("=== Getter defensive copy ===");
        int[] copy = snapshot.getQuantities();
        copy[1] = 100;
        System.out.println("修改 getter 回傳的陣列：" + Arrays.toString(copy));
        System.out.println("snapshot 內容不受影響：" + Arrays.toString(snapshot.getQuantities()));
        System.out.println("缺貨品項數仍為：" + snapshot.outOfStockCount());
        System.out.println("每次 getter 都回傳新陣列："
                + (snapshot.getQuantities() != snapshot.getQuantities()));

        System.out.println();
        System.out.println("=== 邊界條件：null 陣列 ===");
        InventorySnapshot empty = new InventorySnapshot("W-02", null);
        System.out.println(empty);
        System.out.println("內部陣列長度：" + empty.getQuantities().length + "（預期 0）");
        System.out.println("totalQuantity()：" + empty.totalQuantity());
        System.out.println("outOfStockCount()：" + empty.outOfStockCount());
    }
}
