// 除錯練習（8/24）
// 練習一：沒有接近 base case
// 練習二：Traversal 缺少 null case
//
// md 沒有指定檔名，本檔把兩個練習的「錯誤版」與「修正版」並列執行，
// 讓錯誤症狀可以實際重現，而不是只用文字描述。

class DebugTreeNode {
    String value;
    DebugTreeNode left;
    DebugTreeNode right;

    DebugTreeNode(String value) {
        this.value = value;
    }
}

public class RecursionTraversalDebugPractice {

    // ---------- 除錯練習一：沒有接近 base case ----------

    // 錯誤：base case 是 number == 0，遞迴卻用 number + 1，
    //       正數會越來越大，永遠到不了 0，最後耗盡 call stack。
    static void brokenCountDown(int number) {
        if (number == 0) return;
        brokenCountDown(number + 1);
    }

    // 修正：往 base case 前進要用 number - 1，
    //       base case 同時處理 0 與負數，避免負數繼續往下遞迴。
    static void fixedCountDown(int number) {
        if (number <= 0) {
            System.out.println("GO");
            return;
        }
        System.out.println(number);
        fixedCountDown(number - 1);
    }

    private static void runExerciseOne() {
        System.out.println("[Debug 1] no progress toward base case");

        System.out.println("  broken: brokenCountDown(3)");
        try {
            brokenCountDown(3);
            System.out.println("    (unexpectedly returned)");
        } catch (StackOverflowError error) {
            System.out.println("    StackOverflowError -> "
                    + "number keeps growing and never equals 0");
        }

        System.out.println("  fixed: fixedCountDown(3)");
        fixedCountDown(3);
        System.out.println("  fixed: fixedCountDown(0)");
        fixedCountDown(0);
        System.out.println("  fixed: fixedCountDown(-2)");
        fixedCountDown(-2);
        System.out.println();
    }

    // ---------- 除錯練習二：Traversal 缺少 null case ----------

    // 錯誤：method 開頭沒有處理 node == null，
    //       走到 leaf 的 child 時就會存取 null.value。
    static void brokenPreorder(DebugTreeNode node) {
        System.out.print(node.value + " ");
        brokenPreorder(node.left);
        brokenPreorder(node.right);
    }

    // 修正：base case 就是 node == null，直接 return。
    static void fixedPreorder(DebugTreeNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        fixedPreorder(node.left);
        fixedPreorder(node.right);
    }

    //        A
    //       / \
    //      B   C
    //     /
    //    D
    private static DebugTreeNode buildTree() {
        DebugTreeNode root = new DebugTreeNode("A");
        root.left = new DebugTreeNode("B");
        root.right = new DebugTreeNode("C");
        root.left.left = new DebugTreeNode("D");
        return root;
    }

    private static void runExerciseTwo() {
        System.out.println("[Debug 2] traversal missing the null case");

        System.out.print("  broken: ");
        try {
            brokenPreorder(buildTree());
            System.out.println("(unexpectedly finished)");
        } catch (NullPointerException error) {
            System.out.println();
            System.out.println("    NullPointerException -> "
                    + "reads node.value after stepping into a leaf's null child");
        }

        System.out.print("  fixed: ");
        fixedPreorder(buildTree());
        System.out.println();

        System.out.print("  fixed on empty tree: ");
        fixedPreorder(null);
        System.out.println("(no output, returned safely)");
        System.out.println();
    }

    public static void main(String[] args) {
        runExerciseOne();
        runExerciseTwo();
    }
}
