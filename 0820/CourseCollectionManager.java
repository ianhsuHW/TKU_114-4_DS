import java.util.*;
public class CourseCollectionManager {
    public static void main(String[] args) {
        List<String> order = new ArrayList<>(Arrays.asList("Java", "DS", "Java"));
        Set<String> unique = new HashSet<>(order);
        Map<String, Integer> count = new HashMap<>();
        for (String item : order) count.put(item, count.getOrDefault(item, 0) + 1);
        System.out.println(order); System.out.println(unique); System.out.println(count);
    }
}
