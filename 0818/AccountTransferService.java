class Account {
    private String accountId;
    private String owner;
    private int balance;

    Account(String accountId, String owner, int balance) {
        this.accountId = accountId;
        this.owner = owner;
        this.balance = Math.max(0, balance);
    }

    String getAccountId() {
        return accountId;
    }

    int getBalance() {
        return balance;
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    boolean deposit(int amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    @Override
    public String toString() {
        return accountId + " " + owner + " 餘額=" + balance;
    }
}

class TransferService {

    boolean transfer(Account source, Account target, int amount) {
        if (source == null || target == null) {
            System.out.println("轉帳失敗：來源或目標帳戶為 null");
            return false;
        }
        if (source == target) {
            System.out.println("轉帳失敗：不可轉入同一個帳戶");
            return false;
        }
        if (amount <= 0) {
            System.out.println("轉帳失敗：金額必須大於 0，收到 " + amount);
            return false;
        }
        if (amount > source.getBalance()) {
            System.out.println("轉帳失敗：來源餘額不足，餘額 "
                    + source.getBalance() + "，欲轉 " + amount);
            return false;
        }

        source.withdraw(amount);
        target.deposit(amount);
        System.out.println("轉帳成功：" + source.getAccountId() + " -> "
                + target.getAccountId() + " 金額 " + amount);
        return true;
    }
}

public class AccountTransferService {
    public static void main(String[] args) {
        Account amy = new Account("A001", "Amy", 1000);
        Account ben = new Account("A002", "Ben", 200);
        TransferService service = new TransferService();

        System.out.println("=== 初始狀態 ===");
        System.out.println(amy);
        System.out.println(ben);

        System.out.println();
        System.out.println("=== 1. 成功轉帳 ===");
        System.out.println("結果：" + service.transfer(amy, ben, 300));
        System.out.println(amy);
        System.out.println(ben);

        System.out.println();
        System.out.println("=== 2. 餘額不足 ===");
        int amyBefore = amy.getBalance();
        int benBefore = ben.getBalance();
        System.out.println("結果：" + service.transfer(amy, ben, 99999));
        System.out.println("兩個帳戶都未改變："
                + (amy.getBalance() == amyBefore && ben.getBalance() == benBefore));

        System.out.println();
        System.out.println("=== 3. 同帳戶轉帳 ===");
        amyBefore = amy.getBalance();
        System.out.println("結果：" + service.transfer(amy, amy, 100));
        System.out.println("餘額未改變：" + (amy.getBalance() == amyBefore));

        System.out.println();
        System.out.println("=== 4. null 目標 ===");
        amyBefore = amy.getBalance();
        System.out.println("結果：" + service.transfer(amy, null, 100));
        System.out.println("結果：" + service.transfer(null, ben, 100));
        System.out.println("餘額未改變：" + (amy.getBalance() == amyBefore));

        System.out.println();
        System.out.println("=== 5. 金額不合法 ===");
        amyBefore = amy.getBalance();
        benBefore = ben.getBalance();
        System.out.println("結果：" + service.transfer(amy, ben, 0));
        System.out.println("結果：" + service.transfer(amy, ben, -500));
        System.out.println("兩個帳戶都未改變："
                + (amy.getBalance() == amyBefore && ben.getBalance() == benBefore));

        System.out.println();
        System.out.println("=== 最終狀態 ===");
        System.out.println(amy);
        System.out.println(ben);
    }
}
