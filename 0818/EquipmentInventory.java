class Equipment {
    private String code;
    private String name;
    private int stock;

    Equipment(String code, String name, int stock) {
        this.code = code;
        this.name = name;
        this.stock = Math.max(0, stock);
    }

    boolean sell(int quantity) {
        if (quantity <= 0 || quantity > stock) return false;
        stock -= quantity;
        return true;
    }

    void restock(int quantity) { if (quantity > 0) stock += quantity; }
    int getStock() { return stock; }
    String summary() { return code + " " + name + " stock=" + stock; }
}

public class EquipmentInventory {
    public static void main(String[] args) {
        Equipment projector = new Equipment("EQ-01", "Projector", 3);
        System.out.println(projector.summary());
        System.out.println(projector.sell(2));
        projector.restock(1);
        System.out.println(projector.summary());
    }
}
