class TransferAccount {
    private String id; private int balance;
    TransferAccount(String id, int balance) { this.id = id; this.balance = Math.max(0, balance); }
    boolean withdraw(int amount) { if (amount <= 0 || amount > balance) return false; balance -= amount; return true; }
    void deposit(int amount) { if (amount > 0) balance += amount; }
    @Override public String toString() { return id + " balance=" + balance; }
}
public class AccountTransferService {
    public static void main(String[] args) {
        TransferAccount a = new TransferAccount("A", 1000); TransferAccount b = new TransferAccount("B", 200);
        a.withdraw(300); b.deposit(300);
        System.out.println(a); System.out.println(b);
    }
}
