import java.util.ArrayList; import java.util.List;
public class WildcardPecsDemo {
    static double sum(List<? extends Number> values) { double total = 0.0; for (Number v : values) total += v.doubleValue(); return total; }
    static void addDefaults(List<? super Integer> destination) { destination.add(60); destination.add(70); }
    static <T> void copy(List<? extends T> source, List<? super T> destination) { for (T v : source) destination.add(v); }
    public static void main(String[] args) {
        List<Integer> scores = new ArrayList<>(List.of(80, 90)); List<Number> numbers = new ArrayList<>();
        addDefaults(scores); copy(scores, numbers); System.out.println("scores=" + scores); System.out.println("numbers=" + numbers); System.out.println("sum=" + sum(numbers));
    }
}
