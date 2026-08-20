// DigitalWallet 與 WalletTransaction 定義在同資料夾的
// DigitalWallet.java 與 WalletTransaction.java，本檔直接使用。

public class DigitalWalletSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W001", "Amy", 20);
        System.out.println("初始：" + wallet);

        System.out.println();
        System.out.println("=== 1. 正常儲值 ===");
        System.out.println("deposit(1000)：" + wallet.deposit(1000));
        System.out.println(wallet);

        System.out.println();
        System.out.println("=== 2. 正常付款 ===");
        System.out.println("pay(250)：" + wallet.pay(250));
        System.out.println(wallet);

        System.out.println();
        System.out.println("=== 3. 餘額不足 ===");
        int beforeBalance = wallet.getBalance();
        int beforeCount = wallet.getTransactionCount();
        System.out.println("pay(9999)：" + wallet.pay(9999));
        System.out.println(wallet);
        System.out.println("餘額未改變：" + (wallet.getBalance() == beforeBalance));
        System.out.println("交易次數未改變：" + (wallet.getTransactionCount() == beforeCount));

        System.out.println();
        System.out.println("=== 4. 負數與零金額 ===");
        beforeBalance = wallet.getBalance();
        beforeCount = wallet.getTransactionCount();
        System.out.println("deposit(-500)：" + wallet.deposit(-500));
        System.out.println("pay(-100)：" + wallet.pay(-100));
        System.out.println("pay(0)：" + wallet.pay(0));
        System.out.println("refund(-50)：" + wallet.refund(-50));
        System.out.println(wallet);
        System.out.println("餘額未改變：" + (wallet.getBalance() == beforeBalance));
        System.out.println("交易次數未改變：" + (wallet.getTransactionCount() == beforeCount));

        System.out.println();
        System.out.println("=== 5. 退款 ===");
        System.out.println("refund(100)：" + wallet.refund(100));
        System.out.println(wallet);

        System.out.println();
        System.out.println("=== 交易明細 ===");
        wallet.printStatement();

        System.out.println();
        System.out.println("=== 交易紀錄已滿時不得改變餘額 ===");
        DigitalWallet small = new DigitalWallet("W002", "Ben", 2);
        System.out.println("deposit(500)：" + small.deposit(500));
        System.out.println("deposit(300)：" + small.deposit(300));
        System.out.println("historyFull：" + small.isHistoryFull());
        int smallBefore = small.getBalance();
        System.out.println("deposit(100)（已滿）：" + small.deposit(100));
        System.out.println("pay(100)（已滿）：" + small.pay(100));
        System.out.println("餘額未改變：" + (small.getBalance() == smallBefore));
        System.out.println(small);

        System.out.println();
        System.out.println("=== 欄位驗證 ===");
        DigitalWallet other = new DigitalWallet("", "  ", 5);
        System.out.println(other);
    }
}
