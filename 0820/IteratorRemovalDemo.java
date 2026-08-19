import java.util.ArrayList; import java.util.Iterator; import java.util.List;
public class IteratorRemovalDemo {
    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>();
        scores.add(12); scores.add(35); scores.add(8); scores.add(50);
        Iterator<Integer> it = scores.iterator();
        while (it.hasNext()) { int score = it.next(); if (score < 20) it.remove(); }
        System.out.println(scores);
    }
}
