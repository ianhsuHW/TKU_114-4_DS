import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WildcardNumberTools {

    // PECS 的 Producer：只從 list 讀出 Number，使用 ? extends Number
    static double average(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Number value : values) {
            total += value.doubleValue();
        }
        return total / values.size();
    }

    static double maximum(List<? extends Number> values) {
        if (values == null || values.isEmpty()) {
            return Double.NaN;
        }
        double max = values.get(0).doubleValue();
        for (Number value : values) {
            if (value.doubleValue() > max) {
                max = value.doubleValue();
            }
        }
        return max;
    }

    // PECS 的 Consumer：只把 Integer 寫入 list，使用 ? super Integer
    static void addRange(List<? super Integer> target, int start, int end) {
        if (target == null || start > end) {
            return;
        }
        for (int i = start; i <= end; i++) {
            target.add(i);
        }
    }

    public static void main(String[] args) {
        List<Integer> intValues = new ArrayList<>(Arrays.asList(10, 20, 30, 45));
        List<Double> doubleValues = new ArrayList<>(Arrays.asList(1.5, 2.25, 8.0));

        System.out.println("=== average 同時接收 List<Integer> 與 List<Double> ===");
        System.out.println("List<Integer> " + intValues + " 平均：" + average(intValues));
        System.out.println("List<Double> " + doubleValues + " 平均：" + average(doubleValues));

        System.out.println();
        System.out.println("=== maximum ===");
        System.out.println("List<Integer> 最大值：" + maximum(intValues));
        System.out.println("List<Double> 最大值：" + maximum(doubleValues));

        System.out.println();
        System.out.println("=== 空 list 與 null ===");
        List<Integer> empty = new ArrayList<>();
        System.out.println("空 list average（預期 0.0）：" + average(empty));
        System.out.println("空 list maximum（預期 NaN）：" + maximum(empty));
        System.out.println("maximum 結果是 NaN：" + Double.isNaN(maximum(empty)));
        System.out.println("null average：" + average(null));
        System.out.println("null maximum：" + maximum(null));

        System.out.println();
        System.out.println("=== addRange 寫入 List<Integer> ===");
        List<Integer> target = new ArrayList<>();
        addRange(target, 1, 5);
        System.out.println("addRange(target, 1, 5)：" + target);

        System.out.println();
        System.out.println("=== addRange 寫入 List<Number> 與 List<Object> ===");
        List<Number> numbers = new ArrayList<>();
        addRange(numbers, 10, 13);
        System.out.println("List<Number>：" + numbers);

        List<Object> objects = new ArrayList<>();
        addRange(objects, 100, 102);
        System.out.println("List<Object>：" + objects);

        System.out.println();
        System.out.println("=== start > end 不加入任何資料 ===");
        List<Integer> untouched = new ArrayList<>(Arrays.asList(7, 8));
        addRange(untouched, 10, 5);
        System.out.println("addRange(list, 10, 5) 後：" + untouched);
        addRange(untouched, 5, 5);
        System.out.println("addRange(list, 5, 5) 後（單一元素）：" + untouched);
        addRange(null, 1, 3);
        System.out.println("addRange(null, 1, 3) 不發生例外");

        System.out.println();
        System.out.println("=== 綜合 ===");
        System.out.println("target 平均：" + average(target));
        System.out.println("target 最大值：" + maximum(target));

        System.out.println();
        System.out.println("PECS：Producer Extends、Consumer Super。");
        System.out.println("average 與 maximum 只讀取資料，所以用 ? extends Number；");
        System.out.println("addRange 只寫入 Integer，所以用 ? super Integer；");
        System.out.println("全程沒有使用 raw type（例如未加型別參數的 List）。");
    }
}
