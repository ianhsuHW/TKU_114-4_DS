final class WalletTransaction {
    private final int sequence;
    private final String type;
    private final int amount;
    private final int balanceAfter;

    WalletTransaction(int sequence, String type, int amount, int balanceAfter) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    @Override public String toString() { return sequence + " " + type + " " + amount + " balance=" + balanceAfter; }
}

class DigitalWallet {
    private String walletId;
    private int balance;
    private WalletTransaction[] history;
    private int count;

    DigitalWallet(String walletId, int capacity) {
        this.walletId = walletId;
        this.balance = 0;
        this.history = new WalletTransaction[Math.max(1, capacity)];
    }

    boolean deposit(int amount) {
        if (amount <= 0 || count >= history.length) return false;
        balance += amount;
        history[count++] = new WalletTransaction(count, "DEPOSIT", amount, balance);
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance || count >= history.length) return false;
        balance -= amount;
        history[count++] = new WalletTransaction(count, "PAY", amount, balance);
        return true;
    }

    int getBalance() { return balance; }
    void printHistory() { for (int i = 0; i < count; i++) System.out.println(history[i]); }
}

public class DigitalWalletSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W-01", 5);
        System.out.println(wallet.deposit(1000));
        System.out.println(wallet.pay(200));
        System.out.println(wallet.pay(2000));
        System.out.println(wallet.getBalance());
        wallet.printHistory();
    }
}
