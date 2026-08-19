final class WalletEvent {
    private final int sequence;
    private final String type;
    private final int amount;
    private final int balance;

    WalletEvent(int sequence, String type, int amount, int balance) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return sequence + " " + type + " " + amount + " balance=" + balance;
    }
}

public class WalletHistoryManager {
    private final WalletEvent[] history = new WalletEvent[5];
    private int count = 0;

    void add(String type, int amount, int balance) {
        if (count < history.length) {
            history[count++] = new WalletEvent(count, type, amount, balance);
        }
    }

    void print() {
        for (int i = 0; i < count; i++) {
            System.out.println(history[i]);
        }
    }

    public static void main(String[] args) {
        WalletHistoryManager manager = new WalletHistoryManager();
        manager.add("DEPOSIT", 1000, 1000);
        manager.add("PAY", 250, 750);
        manager.print();
    }
}
