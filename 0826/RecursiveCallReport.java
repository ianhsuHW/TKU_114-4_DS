// 課堂實作題一：Recursive Call Report
// 需求：實作 recursive sum(int[] data, int index)，
//       每次輸出 index、current value、recursive result 與 return value。
//       必須測試一般陣列、單一元素與 empty array。

import java.util.Arrays;

public class RecursiveCallReport {

    static int sum(int[] data, int index) {
        String indent = "  ".repeat(index);

        if (data == null || index >= data.length) {
            System.out.println(indent + "index=" + index
                    + " -> base case, return 0");
            return 0;
        }

        int current = data[index];
        System.out.println(indent + "index=" + index
                + " current=" + current + " -> call sum(index+1)");

        int recursiveResult = sum(data, index + 1);
        int returnValue = current + recursiveResult;

        System.out.println(indent + "index=" + index
                + " current=" + current
                + " recursiveResult=" + recursiveResult
                + " return=" + returnValue);
        return returnValue;
    }

    private static void report(String title, int[] data) {
        System.out.println("[" + title + "] data="
                + (data == null ? "null" : Arrays.toString(data)));
        int total = sum(data, 0);
        System.out.println("total=" + total);
        System.out.println();
    }

    public static void main(String[] args) {
        report("normal array", new int[]{4, 7, 2, 9});
        report("single element", new int[]{42});
        report("empty array", new int[0]);
        report("null array", null);
    }
}
