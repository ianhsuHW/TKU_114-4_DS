import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class WordIndexSystem {

    static String normalize(String raw) {
        StringBuilder sb = new StringBuilder();
        for (char c : raw.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String[] sentences = {
            "Java is a programming language.",
            "Data structures matter, and Java collections matter too.",
            "A list, a set, and a map are Java collections.",
            "Programming with Java collections is easier than programming with arrays."
        };

        Map<String, Integer> wordCounts = new TreeMap<>();
        Set<String> uniqueWords = new LinkedHashSet<>();
        int totalWords = 0;

        for (String sentence : sentences) {
            for (String token : sentence.split("\\s+")) {
                String word = normalize(token);
                if (word.isEmpty()) {
                    continue;
                }
                totalWords++;
                uniqueWords.add(word);
                wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
            }
        }

        System.out.println("=== 原始句子 ===");
        for (String sentence : sentences) {
            System.out.println("  " + sentence);
        }

        System.out.println();
        System.out.println("=== 統計摘要 ===");
        System.out.println("總單字數（含重複）：" + totalWords);
        System.out.println("不重複單字數：" + uniqueWords.size());

        System.out.println();
        System.out.println("=== Set：不重複單字（依首次出現順序）===");
        System.out.println(uniqueWords);

        System.out.println();
        System.out.println("=== Map：每個單字的出現次數（依字母排序）===");
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println();
        System.out.println("=== 出現至少兩次的單字 ===");
        List<Map.Entry<String, Integer>> repeated = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() >= 2) {
                repeated.add(entry);
            }
        }
        repeated.sort(Comparator.comparingInt(Map.Entry<String, Integer>::getValue)
                .reversed()
                .thenComparing(Map.Entry::getKey));
        for (Map.Entry<String, Integer> entry : repeated) {
            System.out.println("  " + entry.getKey() + "：" + entry.getValue() + " 次");
        }
        System.out.println("符合條件的單字共 " + repeated.size() + " 個");

        System.out.println();
        System.out.println("=== 只出現一次的單字 ===");
        List<String> once = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            if (entry.getValue() == 1) {
                once.add(entry.getKey());
            }
        }
        Collections.sort(once);
        System.out.println(once);

        System.out.println();
        System.out.println("=== 大小寫與標點處理驗證 ===");
        System.out.println("Java / java 被視為同一個字：" + wordCounts.get("java") + " 次");
        System.out.println("collections. 與 collections 被視為同一個字："
                + wordCounts.get("collections") + " 次");
        System.out.println("normalize(\"Programming.\") = " + normalize("Programming."));
        System.out.println("normalize(\"matter,\") = " + normalize("matter,"));
    }
}
