import java.util.*;
public class WordIndexSystem {
    public static void main(String[] args) { Map<String, Integer> index = new HashMap<>(); for (String word : Arrays.asList("Java", "Tree", "Java")) index.put(word, index.getOrDefault(word, 0) + 1); System.out.println(index); }
}
