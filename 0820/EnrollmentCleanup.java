import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EnrollmentCleanup {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(Arrays.asList(
            "Amy", "Ben", null, "  ", "Cindy", "Amy",
            "", "Dora", "Ben", null, "Amy", "Eric"
        ));

        System.out.println("=== 清理前 ===");
        System.out.println(names);
        System.out.println("筆數：" + names.size());

        System.out.println();
        System.out.println("=== 使用 Iterator 移除不合法資料 ===");
        Iterator<String> iterator = names.iterator();
        int removed = 0;
        while (iterator.hasNext()) {
            String name = iterator.next();
            if (name == null || name.isBlank()) {
                System.out.println("  移除不合法資料：" + (name == null ? "null" : "\"" + name + "\""));
                iterator.remove();
                removed++;
            }
        }
        System.out.println("共移除 " + removed + " 筆");

        System.out.println();
        System.out.println("=== 清理後 ===");
        System.out.println(names);
        System.out.println("筆數：" + names.size());

        System.out.println();
        System.out.println("=== 使用 Set 找出重複姓名 ===");
        Set<String> seen = new LinkedHashSet<>();
        Set<String> duplicated = new TreeSet<>();
        for (String name : names) {
            if (!seen.add(name)) {
                duplicated.add(name);
            }
        }
        System.out.println("不重複姓名：" + seen + "（共 " + seen.size() + " 位）");
        System.out.println("重複出現的姓名：" + duplicated + "（共 " + duplicated.size() + " 位）");

        System.out.println();
        System.out.println("=== 重複報告 ===");
        for (String name : duplicated) {
            int count = 0;
            for (String candidate : names) {
                if (candidate.equals(name)) {
                    count++;
                }
            }
            System.out.println("  " + name + " 出現 " + count + " 次，需移除 " + (count - 1) + " 筆");
        }
        if (duplicated.isEmpty()) {
            System.out.println("  （沒有重複資料）");
        }

        System.out.println();
        System.out.println("=== 去重後的最終名單 ===");
        List<String> finalList = new ArrayList<>(seen);
        System.out.println(finalList);
        System.out.println("原始 " + 12 + " 筆 -> 清理不合法後 " + names.size()
                + " 筆 -> 去除重複後 " + finalList.size() + " 筆");

        System.out.println();
        System.out.println("=== 為什麼一定要用 Iterator.remove() ===");
        System.out.println("在 for-each 走訪的過程中直接呼叫 list.remove(...)，");
        System.out.println("會讓 modCount 與 iterator 的期望值不一致，");
        System.out.println("下一次 next() 就會丟出 ConcurrentModificationException。");
        List<String> demo = new ArrayList<>(Arrays.asList("A", "B", "C"));
        try {
            for (String value : demo) {
                if (value.equals("B")) {
                    demo.remove(value);
                }
            }
        } catch (java.util.ConcurrentModificationException e) {
            System.out.println("實際示範捕捉到：" + e.getClass().getSimpleName());
        }
        System.out.println("改用 iterator.remove() 或 list.removeIf(...) 才安全。");
    }
}
