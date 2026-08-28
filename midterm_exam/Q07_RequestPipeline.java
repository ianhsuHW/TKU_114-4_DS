// 第 7 題：Stack 與 Queue 請求流程
// 重點：Deque 當 Stack 用 push/pop，當 Queue 用 offerLast/pollFirst。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Q07_RequestPipeline {

    public static boolean isBalanced(String text) {
        if (text == null) return false;
        Deque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '(' || ch == '[' || ch == '{') {
                stack.push(ch);
            } else if (ch == ')' || ch == ']' || ch == '}') {
                if (stack.isEmpty()) return false;
                char open = stack.pop();
                if (!matches(open, ch)) return false;      // 型別要相符才算配對
            }
        }
        return stack.isEmpty();
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
                || (open == '[' && close == ']')
                || (open == '{' && close == '}');
    }

    public static List<String> process(String[] commands) {
        List<String> result = new ArrayList<>();
        if (commands == null) return result;

        Deque<String> normal = new ArrayDeque<>();
        Deque<String> urgent = new ArrayDeque<>();

        for (String command : commands) {
            if (command == null) continue;
            String trimmed = command.trim();
            if (trimmed.isEmpty()) continue;
            String[] parts = trimmed.split("\s+");

            if (parts.length == 1 && parts[0].equals("PROCESS")) {
                if (!urgent.isEmpty()) {
                    result.add(urgent.pollFirst());        // 緊急優先
                } else if (!normal.isEmpty()) {
                    result.add(normal.pollFirst());
                } else {
                    result.add("EMPTY");
                }
            } else if (parts.length == 2 && parts[0].equals("NORMAL")) {
                normal.offerLast(parts[1]);
            } else if (parts.length == 2 && parts[0].equals("URGENT")) {
                urgent.offerLast(parts[1]);
            }
            // 其他格式一律忽略
        }
        return result;
    }

    public static void main(String[] args) {
        String[] commands = {
                "NORMAL N1", "URGENT U1", "NORMAL N2", "PROCESS", "PROCESS", "PROCESS"
        };
        System.out.println(Q07_RequestPipeline.isBalanced("a{b[c](d)}"));
        System.out.println(Q07_RequestPipeline.isBalanced("([)]"));
        System.out.println(Q07_RequestPipeline.process(commands));

        System.out.println("--- 邊界測試 ---");
        System.out.println(isBalanced(null));
        System.out.println(isBalanced(""));
        System.out.println(isBalanced("("));
        System.out.println(isBalanced(")("));
        System.out.println(isBalanced("no brackets here"));

        String[] messy = {
                "PROCESS", null, "   ", "NORMAL", "URGENT U9 extra",
                "SUPER X1", "normal n1", "URGENT U1", "PROCESS", "PROCESS"
        };
        System.out.println(process(messy));
        System.out.println(process(null));
        System.out.println(process(new String[0]));
    }
}
