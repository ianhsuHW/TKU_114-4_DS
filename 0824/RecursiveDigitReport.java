// 課堂實作題一：遞迴數位統計
// 需求：完成 digitSum、digitCount 與 countDigit。
//       負數先轉絕對值，digitCount(0) 回傳 1。
//       核心計算不得使用 loop，也不得轉成 String。

public class RecursiveDigitReport {

    // ---- digitSum：各位數字加總 ----
    static int digitSum(int number) {
        return digitSum(absoluteValue(number));
    }

    private static int digitSum(long number) {
        if (number < 10) {
            return (int) number;
        }
        return (int) (number % 10) + digitSum(number / 10);
    }

    // ---- digitCount：位數個數，digitCount(0) 定義為 1 ----
    static int digitCount(int number) {
        return digitCount(absoluteValue(number));
    }

    private static int digitCount(long number) {
        if (number < 10) {
            return 1;
        }
        return 1 + digitCount(number / 10);
    }

    // ---- countDigit：指定數字出現次數 ----
    static int countDigit(int number, int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("digit must be 0 to 9");
        }
        return countDigit(absoluteValue(number), digit);
    }

    private static int countDigit(long number, int digit) {
        int current = number % 10 == digit ? 1 : 0;
        if (number < 10) {
            return current;
        }
        return current + countDigit(number / 10, digit);
    }

    // 使用 long 取絕對值，Integer.MIN_VALUE 才不會溢位
    private static long absoluteValue(int number) {
        long value = number;
        return value < 0 ? -value : value;
    }

    private static void report(int number) {
        System.out.println("number=" + number);
        System.out.println("  digitSum=" + digitSum(number));
        System.out.println("  digitCount=" + digitCount(number));
        System.out.println("  countDigit(5)=" + countDigit(number, 5));
        System.out.println("  countDigit(0)=" + countDigit(number, 0));
    }

    public static void main(String[] args) {
        report(50205);
        report(0);
        report(-731);
    }
}
