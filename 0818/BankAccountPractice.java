class SavingsAccount {
    private String accountId;
    private int balance;

    SavingsAccount(String accountId, int openingBalance) {
        this.accountId = accountId;
        this.balance = Math.max(0, openingBalance);
    }

    void deposit(int amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    boolean withdraw(int amount) {
        if (amount <= 0 || amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    String getAccountId() {
        return accountId;
    }

    int getBalance() {
        return balance;
    }
}

public class BankAccountPractice {
    public static void main(String[] args) {
        SavingsAccount account = new SavingsAccount("S-001", 1500);

        account.deposit(400);
        System.out.println("存款後餘額：" + account.getBalance());

        System.out.println("提款 800：" + account.withdraw(800));
        System.out.println("提款 2000：" + account.withdraw(2000));
        System.out.println("最終餘額：" + account.getBalance());
    }
}
