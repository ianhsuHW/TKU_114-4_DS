import java.util.*;
public class CourseTagReport {
    public static void main(String[] args) {
        Set<String> tags = new HashSet<>(Arrays.asList("Java", "Data", "Java"));
        System.out.println(tags);
        Map<String, Integer> counts = new HashMap<>();
        for (String tag : Arrays.asList("Java", "Java", "Data")) counts.put(tag, counts.getOrDefault(tag, 0) + 1);
        System.out.println(counts);
    }
}
