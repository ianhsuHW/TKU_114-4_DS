// 課堂實作題二：遞迴陣列統計
// 需求：完成 maximum、minimum 與 countAbove。
//       Public wrapper 對 null 或 empty array 拋出 IllegalArgumentException，
//       helper 只用 index 前進，不得複製 array。

public class RecursiveArrayStatistics {

    static int maximum(int[] values) {
        requireData(values);
        return maximum(values, 0);
    }

    private static int maximum(int[] values, int index) {
        if (index == values.length - 1) {
            return values[index];
        }
        return Math.max(values[index], maximum(values, index + 1));
    }

    static int minimum(int[] values) {
        requireData(values);
        return minimum(values, 0);
    }

    private static int minimum(int[] values, int index) {
        if (index == values.length - 1) {
            return values[index];
        }
        return Math.min(values[index], minimum(values, index + 1));
    }

    static int countAbove(int[] values, int threshold) {
        requireData(values);
        return countAbove(values, threshold, 0);
    }

    private static int countAbove(int[] values, int threshold, int index) {
        if (index >= values.length) {
            return 0;
        }
        int current = values[index] > threshold ? 1 : 0;
        return current + countAbove(values, threshold, index + 1);
    }

    private static void requireData(int[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("values must not be null or empty");
        }
    }

    public static void main(String[] args) {
        int[] values = {18, 4, 27, 9, 27, -6};
        System.out.println("maximum=" + maximum(values));
        System.out.println("minimum=" + minimum(values));
        System.out.println("countAbove(9)=" + countAbove(values, 9));

        int[] single = {42};
        System.out.println("single maximum=" + maximum(single));
        System.out.println("single minimum=" + minimum(single));
        System.out.println("single countAbove(42)=" + countAbove(single, 42));

        try {
            maximum(null);
        } catch (IllegalArgumentException error) {
            System.out.println("null -> " + error.getMessage());
        }

        try {
            minimum(new int[0]);
        } catch (IllegalArgumentException error) {
            System.out.println("empty -> " + error.getMessage());
        }
    }
}
