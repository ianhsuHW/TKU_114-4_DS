public class BoundedGenericDemo {
    static <T extends Number> double sum(T[] data) {
        double total = 0;
        for (T value : data) total += value.doubleValue();
        return total;
    }
    static <T extends Comparable<T>> T max(T first, T second) {
        return first.compareTo(second) >= 0 ? first : second;
    }
    public static void main(String[] args) {
        Integer[] ints = {10, 20, 30};
        Double[] doubles = {1.5, 2.5, 3.0};
        System.out.println(sum(ints));
        System.out.println(sum(doubles));
        System.out.println(max("Java", "Graph"));
    }
}
