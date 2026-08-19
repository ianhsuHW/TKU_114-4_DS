import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollectionPracticeHomework {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();
        names.add("Amy");
        names.add("Ben");
        names.add("Amy");
        names.add("Cara");

        System.out.println("List：" + names);

        Set<String> uniqueNames = new HashSet<>(names);
        System.out.println("Set：" + uniqueNames);

        for (String name : uniqueNames) {
            System.out.println("學生：" + name);
        }
    }
}
