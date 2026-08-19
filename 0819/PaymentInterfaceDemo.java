interface PaymentMethod { boolean pay(int amount); }
class CardPayment implements PaymentMethod { private int balance; CardPayment(int balance){ this.balance = balance; } @Override public boolean pay(int amount){ if(amount <= 0 || amount > balance) return false; balance -= amount; return true; } }
class WalletPayment implements PaymentMethod { private int balance; WalletPayment(int balance){ this.balance = balance; } @Override public boolean pay(int amount){ if(amount <= 0 || amount > balance) return false; balance -= amount; return true; } }
public class PaymentInterfaceDemo {
    public static void main(String[] args) {
        PaymentMethod card = new CardPayment(3000);
        PaymentMethod wallet = new WalletPayment(800);
        System.out.println("Card pay 1200：" + card.pay(1200));
        System.out.println("Wallet pay 500：" + wallet.pay(500));
        System.out.println("Wallet pay 500：" + wallet.pay(500));
    }
}
