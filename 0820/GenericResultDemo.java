class Result<T> {
    private final boolean success;
    private final String message;
    private final T data;

    private Result(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    static <T> Result<T> ok(String message, T data) {
        return new Result<>(true, message, data);
    }

    static <T> Result<T> fail(String message) {
        return new Result<>(false, message, null);
    }

    boolean isSuccess() {
        return success;
    }

    String getMessage() {
        return message;
    }

    T getData() {
        return data;
    }

    T getDataOrDefault(T fallback) {
        return data == null ? fallback : data;
    }

    @Override
    public String toString() {
        return "success=" + success + " message=" + message + " data=" + data;
    }
}

public class GenericResultDemo {

    static Result<String> findStudentName(String studentId) {
        if ("S101".equals(studentId)) {
            return Result.ok("查詢成功", "Amy");
        }
        return Result.fail("查無此學號：" + studentId);
    }

    static Result<Integer> findScore(String studentId) {
        if ("S101".equals(studentId)) {
            return Result.ok("查詢成功", 88);
        }
        return Result.fail("查無成績：" + studentId);
    }

    public static void main(String[] args) {
        System.out.println("=== Result<String> 成功 ===");
        Result<String> nameOk = findStudentName("S101");
        System.out.println(nameOk);
        // 取出資料不需要 cast，compiler 已經知道是 String
        String name = nameOk.getData();
        System.out.println("姓名長度：" + name.length());

        System.out.println();
        System.out.println("=== Result<String> 失敗 ===");
        Result<String> nameFail = findStudentName("S999");
        System.out.println(nameFail);
        System.out.println("data 是否為 null：" + (nameFail.getData() == null));
        System.out.println("安全取值：" + nameFail.getDataOrDefault("(未知)"));
        if (nameFail.isSuccess()) {
            System.out.println("長度：" + nameFail.getData().length());
        } else {
            System.out.println("失敗時不可直接呼叫 getData().length()，會 NullPointerException");
        }

        System.out.println();
        System.out.println("=== Result<Integer> 成功 ===");
        Result<Integer> scoreOk = findScore("S101");
        System.out.println(scoreOk);
        // 直接當 int 使用，不需要 cast
        int score = scoreOk.getData();
        System.out.println("加 5 分後：" + (score + 5));

        System.out.println();
        System.out.println("=== Result<Integer> 失敗 ===");
        Result<Integer> scoreFail = findScore("S999");
        System.out.println(scoreFail);
        System.out.println("安全取值：" + scoreFail.getDataOrDefault(0));

        System.out.println();
        System.out.println("=== 型態安全 ===");
        System.out.println("以下兩行如果解除註解，會在編譯階段就被擋下來，不會等到執行才出錯：");
        System.out.println("  // String wrong = scoreOk.getData();");
        System.out.println("  // Result<String> mixed = findScore(\"S101\");");
        // String wrong = scoreOk.getData();
        // Result<String> mixed = findScore("S101");
    }
}
