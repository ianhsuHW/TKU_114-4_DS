final class WalletEvent {
    private final int sequence;
    private final String type;
    private final int amount;
    private final int balanceAfter;

    WalletEvent(int sequence, String type, int amount, int balanceAfter) {
        this.sequence = sequence;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
    }

    int getSequence() {
        return sequence;
    }

    String getType() {
        return type;
    }

    int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return sequence + " " + type + " " + amount + " balance=" + balanceAfter;
    }
}

class HistoryWallet {
    private final String walletId;
    private final String owner;
    private int balance;
    private final WalletEvent[] events;
    private int eventCount;

    HistoryWallet(String walletId, String owner, int historyCapacity) {
        this.walletId = (walletId == null || walletId.isBlank()) ? "UNKNOWN" : walletId;
        this.owner = (owner == null || owner.isBlank()) ? "Unknown" : owner;
        this.balance = 0;
        this.events = new WalletEvent[Math.max(1, historyCapacity)];
        this.eventCount = 0;
    }

    String getWalletId() {
        return walletId;
    }

    int getBalance() {
        return balance;
    }

    boolean isHistoryFull() {
        return eventCount >= events.length;
    }

    boolean deposit(int amount) {
        if (amount <= 0 || isHistoryFull()) {
            return false;
        }
        balance += amount;
        record("DEPOSIT", amount);
        return true;
    }

    boolean pay(int amount) {
        if (amount <= 0 || amount > balance || isHistoryFull()) {
            return false;
        }
        balance -= amount;
        record("PAY", amount);
        return true;
    }

    boolean refund(int amount) {
        if (amount <= 0 || isHistoryFull()) {
            return false;
        }
        balance += amount;
        record("REFUND", amount);
        return true;
    }

    boolean transferTo(HistoryWallet target, int amount) {
        if (target == null || target == this) {
            return false;
        }
        if (amount <= 0 || amount > balance) {
            return false;
        }
        if (isHistoryFull() || target.isHistoryFull()) {
            return false;
        }
        balance -= amount;
        record("TRANSFER_OUT", amount);
        target.balance += amount;
        target.record("TRANSFER_IN", amount);
        return true;
    }

    WalletEvent findTransaction(int sequence) {
        for (int i = 0; i < eventCount; i++) {
            if (events[i].getSequence() == sequence) {
                return events[i];
            }
        }
        return null;
    }

    int totalByType(String type) {
        int total = 0;
        for (int i = 0; i < eventCount; i++) {
            if (events[i].getType().equals(type)) {
                total += events[i].getAmount();
            }
        }
        return total;
    }

    private void record(String type, int amount) {
        events[eventCount] = new WalletEvent(eventCount + 1, type, amount, balance);
        eventCount++;
    }

    void printStatement() {
        System.out.println("--- " + walletId + " owner=" + owner
                + " balance=" + balance + " 交易筆數=" + eventCount + " ---");
        for (int i = 0; i < eventCount; i++) {
            System.out.println("  " + events[i]);
        }
    }
}

public class WalletHistoryManager {
    public static void main(String[] args) {
        HistoryWallet amy = new HistoryWallet("W001", "Amy", 6);
        HistoryWallet ben = new HistoryWallet("W002", "Ben", 3);

        System.out.println("=== 基本交易 ===");
        System.out.println("amy deposit 1000：" + amy.deposit(1000));
        System.out.println("amy pay 250：" + amy.pay(250));
        System.out.println("amy pay 9999：" + amy.pay(9999));
        System.out.println("amy refund 50：" + amy.refund(50));

        System.out.println();
        System.out.println("=== 3. transferTo：兩邊同時留下紀錄 ===");
        System.out.println("amy -> ben 400：" + amy.transferTo(ben, 400));
        System.out.println("amy -> amy 100（同一物件）：" + amy.transferTo(amy, 100));
        System.out.println("amy -> null 100：" + amy.transferTo(null, 100));
        System.out.println("amy -> ben 99999（餘額不足）：" + amy.transferTo(ben, 99999));

        System.out.println();
        System.out.println("=== 1. findTransaction ===");
        System.out.println("findTransaction(2)：" + amy.findTransaction(2));
        System.out.println("findTransaction(99)：" + amy.findTransaction(99));

        System.out.println();
        System.out.println("=== 2. totalByType ===");
        System.out.println("amy DEPOSIT 總額：" + amy.totalByType("DEPOSIT"));
        System.out.println("amy PAY 總額：" + amy.totalByType("PAY"));
        System.out.println("amy TRANSFER_OUT 總額：" + amy.totalByType("TRANSFER_OUT"));
        System.out.println("ben TRANSFER_IN 總額：" + ben.totalByType("TRANSFER_IN"));
        System.out.println("amy 不存在的類型 CASHBACK：" + amy.totalByType("CASHBACK"));

        System.out.println();
        System.out.println("=== 4. 交易陣列已滿時不得修改餘額 ===");
        System.out.println("ben 目前 historyFull：" + ben.isHistoryFull());
        ben.deposit(10);
        ben.deposit(20);
        System.out.println("ben 連續存款後 historyFull：" + ben.isHistoryFull());
        int benBalanceBefore = ben.getBalance();
        System.out.println("ben deposit 500（已滿）：" + ben.deposit(500));
        System.out.println("ben refund 500（已滿）：" + ben.refund(500));
        System.out.println("amy -> ben 100（目標已滿）：" + amy.transferTo(ben, 100));
        System.out.println("ben 餘額未改變：" + (ben.getBalance() == benBalanceBefore));

        System.out.println();
        System.out.println("=== 5. 完整 statement ===");
        amy.printStatement();
        ben.printStatement();

        System.out.println();
        System.out.println("main() 全程只呼叫 public 行為，沒有直接修改 balance 或 events。");
    }
}
