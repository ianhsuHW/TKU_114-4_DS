class Equipment {
    private String id;
    private String name;
    private int availableCount;

    Equipment(String id, String name, int availableCount) {
        this.id = (id == null || id.isBlank()) ? "Unknown" : id;
        this.name = (name == null || name.isBlank()) ? "Unknown" : name;
        this.availableCount = Math.max(0, availableCount);
    }

    boolean borrowOne() {
        if (availableCount <= 0) {
            return false;
        }
        availableCount--;
        return true;
    }

    void returnItems(int quantity) {
        if (quantity > 0) {
            availableCount += quantity;
        }
    }

    int getAvailableCount() {
        return availableCount;
    }

    @Override
    public String toString() {
        return "設備編號=" + id + " 名稱=" + name + " 可借數量=" + availableCount;
    }
}

public class EquipmentInventory {
    public static void main(String[] args) {
        Equipment projector = new Equipment("EQ-01", "投影機", 2);
        Equipment laptop = new Equipment("  ", "", -5);

        System.out.println("=== 初始狀態 ===");
        System.out.println(projector);
        System.out.println(laptop);

        System.out.println();
        System.out.println("=== 借用成功 ===");
        System.out.println("borrowOne：" + projector.borrowOne());
        System.out.println("borrowOne：" + projector.borrowOne());
        System.out.println(projector);

        System.out.println();
        System.out.println("=== 借用失敗（庫存為 0）===");
        System.out.println("borrowOne：" + projector.borrowOne());
        System.out.println("borrowOne：" + laptop.borrowOne());
        System.out.println(projector);
        System.out.println(laptop);

        System.out.println();
        System.out.println("=== 歸還 ===");
        projector.returnItems(3);
        System.out.println("歸還 3 台後：" + projector);
        projector.returnItems(0);
        projector.returnItems(-2);
        System.out.println("歸還 0 與 -2 後（不變）：" + projector);

        System.out.println();
        System.out.println("最終可借數量不為負數：" + (projector.getAvailableCount() >= 0
                && laptop.getAvailableCount() >= 0));
    }
}
