// 第 8 題：Recursive 資料稽核
// 重點：三個 method 都只能用 recursion，沒有任何 loop 或 collection。

public class Q08_RecursiveAudit {

    public static int sumValid(int[] data, int index) {
        if (data == null) return 0;
        if (index < 0) return sumValid(data, 0);            // 負數 index 從 0 開始
        if (index >= data.length) return 0;                 // base case
        int value = data[index];
        int keep = (value >= 0 && value <= 100) ? value : 0;
        return keep + sumValid(data, index + 1);
    }

    public static int countOccurrences(int[] data, int index, int target) {
        if (data == null) return 0;
        if (index < 0) return countOccurrences(data, 0, target);
        if (index >= data.length) return 0;
        int hit = (data[index] == target) ? 1 : 0;
        return hit + countOccurrences(data, index + 1, target);
    }

    public static boolean isPalindrome(String text, int left, int right) {
        if (text == null) return false;
        if (left >= right) return true;                     // 交錯或同一格即完成
        if (left < 0 || right >= text.length()) return false;
        char leftChar = Character.toLowerCase(text.charAt(left));
        char rightChar = Character.toLowerCase(text.charAt(right));
        if (leftChar != rightChar) return false;
        return isPalindrome(text, left + 1, right - 1);
    }

    public static void main(String[] args) {
        int[] data = {10, -1, 20, 101, 20};
        System.out.println(Q08_RecursiveAudit.sumValid(data, 0));
        System.out.println(Q08_RecursiveAudit.countOccurrences(data, 0, 20));
        System.out.println(Q08_RecursiveAudit.isPalindrome("Level", 0, 4));

        System.out.println("--- 邊界測試 ---");
        System.out.println(sumValid(null, 0));
        System.out.println(sumValid(new int[0], 0));
        System.out.println(sumValid(data, -3));             // 等同從 0 開始
        System.out.println(sumValid(data, 99));
        System.out.println(sumValid(data, 2));
        System.out.println(countOccurrences(null, 0, 20));
        System.out.println(countOccurrences(data, 3, 20));
        System.out.println(countOccurrences(data, 0, 7));
        System.out.println(isPalindrome(null, 0, 3));
        System.out.println(isPalindrome("", 0, -1));
        System.out.println(isPalindrome("A", 0, 0));
        System.out.println(isPalindrome("RaceCar", 0, 6));
        System.out.println(isPalindrome("ab ba", 0, 4));
        System.out.println(isPalindrome("abba!", 0, 4));
    }
}
