import java.util.Arrays;
class InventorySnapshot {
    private final String item;
    private final int[] quantities;

    InventorySnapshot(String item, int[] quantities) {
        this.item = item;
        this.quantities = Arrays.copyOf(quantities, quantities.length);
    }
    int[] getQuantities() { return Arrays.copyOf(quantities, quantities.length); }
    double average() {
        int total = 0;
        for (int q : quantities) total += q;
        return quantities.length == 0 ? 0.0 : total / (double) quantities.length;
    }
}

public class InventorySnapshotPractice {
    public static void main(String[] args) {
        int[] original = {8, 9, 7};
        InventorySnapshot snapshot = new InventorySnapshot("Keyboard", original);
        original[0] = 0;
        System.out.println(snapshot.average());
        System.out.println(Arrays.toString(snapshot.getQuantities()));
    }
}
