import java.util.*;
public class WildcardNumberTools {
    static double sum(List<? extends Number> values){ double total = 0.0; for(Number n : values) total += n.doubleValue(); return total; }
    public static void main(String[] args) { List<Integer> ints = Arrays.asList(10,20,30); List<Double> doubles = Arrays.asList(1.5, 2.5); System.out.println(sum(ints)); System.out.println(sum(doubles)); }
}
