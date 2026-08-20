// WalletTransaction 與 DigitalWallet 定義在同資料夾的
// WalletTransaction.java 與 DigitalWallet.java，本檔直接使用。

public class WalletTransactionSystem {
    public static void main(String[] args) {
        DigitalWallet wallet = new DigitalWallet("W001", "Amy", 5);

        System.out.println("deposit=" + wallet.deposit(1000));
        System.out.println("pay 250=" + wallet.pay(250));
        System.out.println("pay 900=" + wallet.pay(900));
        System.out.println("refund=" + wallet.refund(50));
        wallet.printStatement();
    }
}
