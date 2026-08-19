public class GenericArrayTools {
    static <T> void printArray(T[] data) { for (T v : data) System.out.print(v + " "); System.out.println(); }
    public static void main(String[] args) { String[] names = {"Amy", "Ben"}; Integer[] scores = {80, 90}; printArray(names); printArray(scores); }
}
