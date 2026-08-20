// DigitalWallet 與 WalletTransaction 已定義於同資料夾的
// WalletTransactionSystem.java（概念 12），本檔直接使用。

public class DigitalWalletSystem {

    private static int successCount = 0;

    static boolean record(String label, boolean ok) {
        if (ok) {
            successCount++;
        }
        System.out.println(label + "：" + ok);
        return ok;
    }

    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W001", "Amy", 20);

        System.out.println("=== 1. 正常儲值 ===");
        record("deposit(1000)", wallet.deposit(1000));
        record("deposit(500)", wallet.deposit(500));
        wallet.printStatement();

        System.out.println();
        System.out.println("=== 2. 正常付款 ===");
        record("pay(250)", wallet.pay(250));
        wallet.printStatement();

        System.out.println();
        System.out.println("=== 3. 餘額不足 ===");
        int before = successCount;
        record("pay(9999)", wallet.pay(9999));
        System.out.println("交易次數未增加：" + (successCount == before));

        System.out.println();
        System.out.println("=== 4. 負數與零金額 ===");
        before = successCount;
        record("deposit(-500)", wallet.deposit(-500));
        record("deposit(0)", wallet.deposit(0));
        record("pay(-100)", wallet.pay(-100));
        record("pay(0)", wallet.pay(0));
        record("refund(-50)", wallet.refund(-50));
        System.out.println("交易次數未增加：" + (successCount == before));

        System.out.println();
        System.out.println("=== 5. 退款 ===");
        record("refund(100)", wallet.refund(100));

        System.out.println();
        System.out.println("=== 交易次數統計 ===");
        System.out.println("成功交易次數：" + successCount);
        wallet.printStatement();

        System.out.println();
        System.out.println("=== 交易紀錄已滿時不得改變狀態 ===");
        DigitalWallet small = new DigitalWallet("W002", "Ben", 2);
        System.out.println("deposit(500)：" + small.deposit(500));
        System.out.println("deposit(300)：" + small.deposit(300));
        System.out.println("deposit(100)（紀錄已滿）：" + small.deposit(100));
        System.out.println("pay(100)（紀錄已滿）：" + small.pay(100));
        small.printStatement();

        System.out.println();
        System.out.println("=== 欄位驗證：空白 walletId 與 owner ===");
        DigitalWallet other = new DigitalWallet("", "  ", 5);
        other.printStatement();

        System.out.println();
        System.out.println("main() 全程只呼叫 deposit / pay / refund，");
        System.out.println("沒有直接修改 balance 或 transaction array。");
    }
}
