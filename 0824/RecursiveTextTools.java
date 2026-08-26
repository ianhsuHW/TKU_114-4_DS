// 課後作業一：遞迴字串工具
// 需求：完成 reverse、isPalindrome 與 countCharacter。
//       Palindrome 忽略英文大小寫與空白。
//       測試 empty、single character、Level 與一般字串。

public class RecursiveTextTools {

    static String reverse(String text) {
        if (text == null || text.length() <= 1) {
            return text;
        }
        return reverse(text.substring(1)) + text.charAt(0);
    }

    static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        String cleaned = clean(text, 0);
        return isPalindrome(cleaned, 0, cleaned.length() - 1);
    }

    // 遞迴移除空白並轉小寫，得到比對用字串
    private static String clean(String text, int index) {
        if (index >= text.length()) {
            return "";
        }
        char current = text.charAt(index);
        String head = Character.isWhitespace(current)
                ? ""
                : String.valueOf(Character.toLowerCase(current));
        return head + clean(text, index + 1);
    }

    private static boolean isPalindrome(String text, int low, int high) {
        if (low >= high) {
            return true;
        }
        if (text.charAt(low) != text.charAt(high)) {
            return false;
        }
        return isPalindrome(text, low + 1, high - 1);
    }

    static int countCharacter(String text, char target) {
        if (text == null || text.isEmpty()) {
            return 0;
        }
        int current = text.charAt(0) == target ? 1 : 0;
        return current + countCharacter(text.substring(1), target);
    }

    private static void report(String text) {
        System.out.println("text=\"" + text + "\"");
        System.out.println("  reverse=\"" + reverse(text) + "\"");
        System.out.println("  isPalindrome=" + isPalindrome(text));
        System.out.println("  countCharacter('e')=" + countCharacter(text, 'e'));
    }

    public static void main(String[] args) {
        report("");
        report("a");
        report("Level");
        report("Never odd or even");
        report("data structure");
    }
}
