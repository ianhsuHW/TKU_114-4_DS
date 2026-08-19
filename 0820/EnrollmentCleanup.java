import java.util.*;
public class EnrollmentCleanup {
    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>(Arrays.asList(12, 35, 8, 50));
        Iterator<Integer> it = scores.iterator(); while (it.hasNext()) { int s = it.next(); if (s < 20) it.remove(); }
        System.out.println(scores);
    }
}
