import java.util.Arrays;
import java.util.Objects;

public class GenericArrayTools {

    static <T> int countMatches(T[] data, T target) {
        if (data == null) {
            return 0;
        }
        int count = 0;
        for (T value : data) {
            if (Objects.equals(value, target)) {
                count++;
            }
        }
        return count;
    }

    static <T> T last(T[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        return data[data.length - 1];
    }

    static <T> void swap(T[] data, int first, int second) {
        if (data == null) {
            System.out.println("  swap 失敗：陣列為 null");
            return;
        }
        if (!isValidIndex(data, first) || !isValidIndex(data, second)) {
            System.out.println("  swap 失敗：index 超出範圍 first=" + first
                    + " second=" + second + " length=" + data.length);
            return;
        }
        T temp = data[first];
        data[first] = data[second];
        data[second] = temp;
    }

    private static <T> boolean isValidIndex(T[] data, int index) {
        return index >= 0 && index < data.length;
    }

    public static void main(String[] args) {
        String[] names = { "Amy", "Ben", "Amy", "Cindy", "Amy" };
        Integer[] scores = { 80, 95, 80, 70 };

        System.out.println("=== countMatches ===");
        System.out.println("names 中 Amy 的數量：" + countMatches(names, "Amy"));
        System.out.println("names 中 Zoe 的數量：" + countMatches(names, "Zoe"));
        System.out.println("scores 中 80 的數量：" + countMatches(scores, 80));
        System.out.println("null 陣列：" + countMatches(null, "Amy"));
        System.out.println("空陣列：" + countMatches(new String[0], "Amy"));

        System.out.println();
        System.out.println("=== 含 null 元素 ===");
        String[] withNull = { "Amy", null, "Ben", null };
        System.out.println("null 元素的數量：" + countMatches(withNull, null));
        System.out.println("Amy 的數量：" + countMatches(withNull, "Amy"));

        System.out.println();
        System.out.println("=== last ===");
        System.out.println("names 最後一個：" + last(names));
        System.out.println("scores 最後一個：" + last(scores));
        System.out.println("null 陣列：" + last(null));
        System.out.println("空陣列：" + last(new Integer[0]));

        System.out.println();
        System.out.println("=== swap ===");
        System.out.println("交換前：" + Arrays.toString(names));
        swap(names, 0, 3);
        System.out.println("swap(0, 3)：" + Arrays.toString(names));
        swap(names, 1, 1);
        System.out.println("swap(1, 1)：" + Arrays.toString(names));

        System.out.println();
        System.out.println("=== swap 的不合法輸入 ===");
        swap(names, -1, 2);
        swap(names, 0, 99);
        swap(null, 0, 1);
        swap(new String[0], 0, 0);
        System.out.println("陣列內容未被破壞：" + Arrays.toString(names));

        System.out.println();
        System.out.println("=== 型態安全 ===");
        System.out.println("countMatches(scores, 80) 可以編譯，因為兩邊都是 Integer。");
        System.out.println("countMatches(scores, \"80\") 會在編譯階段被擋下來。");
        // System.out.println(countMatches(scores, "80"));
    }
}
