// 課堂實作題三：共同興趣
// 需求：使用 Set 計算 union、intersection、first-only 與 second-only，
//       不修改輸入 Set。

import java.util.LinkedHashSet;
import java.util.Set;

public class InterestSetComparison {

    // 每個運算都先複製一份再操作，輸入的 Set 保持原狀
    static Set<String> union(Set<String> first, Set<String> second) {
        Set<String> result = new LinkedHashSet<>(safe(first));
        result.addAll(safe(second));
        return result;
    }

    static Set<String> intersection(Set<String> first, Set<String> second) {
        Set<String> result = new LinkedHashSet<>(safe(first));
        result.retainAll(safe(second));
        return result;
    }

    static Set<String> firstOnly(Set<String> first, Set<String> second) {
        Set<String> result = new LinkedHashSet<>(safe(first));
        result.removeAll(safe(second));
        return result;
    }

    static Set<String> secondOnly(Set<String> first, Set<String> second) {
        return firstOnly(second, first);
    }

    private static Set<String> safe(Set<String> input) {
        return input == null ? Set.of() : input;
    }

    private static void compare(String label, Set<String> amy, Set<String> ben) {
        System.out.println("[" + label + "]");
        System.out.println("  amy          =" + amy);
        System.out.println("  ben          =" + ben);
        System.out.println("  union        =" + union(amy, ben));
        System.out.println("  intersection =" + intersection(amy, ben));
        System.out.println("  amy only     =" + firstOnly(amy, ben));
        System.out.println("  ben only     =" + secondOnly(amy, ben));
        System.out.println();
    }

    public static void main(String[] args) {
        // LinkedHashSet 保留插入順序，輸出才穩定好比對
        Set<String> amy = new LinkedHashSet<>(
                java.util.List.of("reading", "hiking", "coding", "music"));
        Set<String> ben = new LinkedHashSet<>(
                java.util.List.of("coding", "gaming", "music", "cooking"));

        Set<String> amyBefore = new LinkedHashSet<>(amy);
        Set<String> benBefore = new LinkedHashSet<>(ben);

        compare("two students", amy, ben);

        System.out.println("[inputs unchanged]");
        System.out.println("  amy unchanged=" + amy.equals(amyBefore));
        System.out.println("  ben unchanged=" + ben.equals(benBefore));
        System.out.println();

        compare("no overlap", new LinkedHashSet<>(java.util.List.of("chess")),
                new LinkedHashSet<>(java.util.List.of("tennis")));
        compare("identical", amy, new LinkedHashSet<>(amy));
        compare("empty second", amy, new LinkedHashSet<>());
        compare("null second", amy, null);
    }
}
