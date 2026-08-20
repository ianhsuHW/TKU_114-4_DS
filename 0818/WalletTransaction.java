// 共用類別：由 WalletTransactionSystem.java（概念 12）與
// DigitalWalletSystem.java（課後作業一）共同使用，
// 因此獨立成一個檔案，避免同一個資料夾內重複宣告。
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

    int getSequence() {
        return sequence;
    }

    String getType() {
        return type;
    }

    int getAmount() {
        return amount;
    }

    int getBalanceAfter() {
        return balanceAfter;
    }

    @Override
    public String toString() {
        return sequence + " " + type + " " + amount
                + " balance=" + balanceAfter;
    }
}
