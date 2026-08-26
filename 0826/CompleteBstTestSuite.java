// 課後作業三：完整 BST Test Suite
// 需求：不使用 JUnit，以自訂 check(description, condition) 輸出 PASS/FAIL。
//       至少包含 20 個 assertion，覆蓋 empty、duplicate、root、leaf、
//       one child、two children、missing、range 與 invariant。

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class TestBstNode {
    int value;
    TestBstNode left;
    TestBstNode right;

    TestBstNode(int value) {
        this.value = value;
    }
}

class TestBst {
    private TestBstNode root;

    boolean add(int value) {
        if (root == null) {
            root = new TestBstNode(value);
            return true;
        }
        TestBstNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new TestBstNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new TestBstNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        TestBstNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    String deleteCase(int value) {
        TestBstNode node = findNode(value);
        if (node == null) return "MISSING";
        if (node.left == null && node.right == null) return "LEAF";
        if (node.left == null || node.right == null) return "ONE_CHILD";
        return "TWO_CHILDREN";
    }

    private TestBstNode findNode(int value) {
        TestBstNode current = root;
        while (current != null) {
            if (value == current.value) return current;
            current = value < current.value ? current.left : current.right;
        }
        return null;
    }

    boolean remove(int value) {
        if (findNode(value) == null) return false;
        root = remove(root, value);
        return true;
    }

    private TestBstNode remove(TestBstNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            TestBstNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private TestBstNode minimumNode(TestBstNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    Integer minimum() {
        if (root == null) return null;
        TestBstNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        TestBstNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(TestBstNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(TestBstNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(TestBstNode node, List<Integer> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.value);
        inorder(node.right, result);
    }

    List<Integer> range(int low, int high) {
        List<Integer> result = new ArrayList<>();
        if (low <= high) range(root, low, high, result);
        return result;
    }

    private void range(TestBstNode node, int low, int high,
                       List<Integer> result) {
        if (node == null) return;
        if (low < node.value) range(node.left, low, high, result);
        if (low <= node.value && node.value <= high) result.add(node.value);
        if (node.value < high) range(node.right, low, high, result);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(TestBstNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }
}

public class CompleteBstTestSuite {

    private static int passed = 0;
    private static int failed = 0;

    static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + description);
        } else {
            failed++;
            System.out.println("FAIL  " + description);
        }
    }

    private static TestBst build(int... values) {
        TestBst tree = new TestBst();
        for (int value : values) {
            tree.add(value);
        }
        return tree;
    }

    public static void main(String[] args) {
        // --- empty ---
        TestBst empty = build();
        check("01 empty: size is 0", empty.size() == 0);
        check("02 empty: height is -1", empty.height() == -1);
        check("03 empty: contains is false", !empty.contains(10));
        check("04 empty: remove is false", !empty.remove(10));
        check("05 empty: minimum is null", empty.minimum() == null);
        check("06 empty: maximum is null", empty.maximum() == null);
        check("07 empty: inorder is empty", empty.inorder().isEmpty());
        check("08 empty: is a valid BST", empty.isValid());

        // --- root / single node ---
        TestBst single = build(42);
        check("09 root: add returns true", single.size() == 1);
        check("10 root: height of single node is 0", single.height() == 0);
        check("11 root: minimum equals maximum",
                single.minimum().equals(single.maximum()));
        check("12 root: deleteCase is LEAF",
                single.deleteCase(42).equals("LEAF"));
        check("13 root: remove root empties tree",
                single.remove(42) && single.size() == 0);

        // --- duplicate ---
        TestBst duplicate = build(50, 30, 70);
        check("14 duplicate: add existing key returns false",
                !duplicate.add(30));
        check("15 duplicate: size unchanged after duplicate add",
                duplicate.size() == 3);
        check("16 duplicate: inorder has no repeat",
                duplicate.inorder().equals(Arrays.asList(30, 50, 70)));

        // --- standard tree ---
        TestBst tree = build(50, 30, 70, 20, 40, 60, 80);
        check("17 standard: inorder is ascending",
                tree.inorder().equals(
                        Arrays.asList(20, 30, 40, 50, 60, 70, 80)));
        check("18 standard: size is 7", tree.size() == 7);
        check("19 standard: height is 2", tree.height() == 2);
        check("20 standard: minimum is 20", tree.minimum() == 20);
        check("21 standard: maximum is 80", tree.maximum() == 80);
        check("22 standard: contains existing key", tree.contains(60));

        // --- missing ---
        check("23 missing: contains is false", !tree.contains(65));
        check("24 missing: deleteCase is MISSING",
                tree.deleteCase(65).equals("MISSING"));
        check("25 missing: remove returns false", !tree.remove(65));
        check("26 missing: size unchanged", tree.size() == 7);

        // --- leaf delete ---
        check("27 leaf: deleteCase is LEAF",
                tree.deleteCase(20).equals("LEAF"));
        check("28 leaf: remove succeeds", tree.remove(20));
        check("29 leaf: inorder loses only that key",
                tree.inorder().equals(Arrays.asList(30, 40, 50, 60, 70, 80)));
        check("30 leaf: still valid after remove", tree.isValid());

        // --- one child delete ---
        check("31 one child: deleteCase is ONE_CHILD",
                tree.deleteCase(30).equals("ONE_CHILD"));
        check("32 one child: remove succeeds", tree.remove(30));
        check("33 one child: child subtree kept",
                tree.inorder().equals(Arrays.asList(40, 50, 60, 70, 80)));
        check("34 one child: still valid", tree.isValid());

        // --- two children delete (root) ---
        check("35 two children: deleteCase is TWO_CHILDREN",
                tree.deleteCase(50).equals("TWO_CHILDREN"));
        check("36 two children: remove root succeeds", tree.remove(50));
        check("37 two children: successor replaces root",
                tree.inorder().equals(Arrays.asList(40, 60, 70, 80)));
        check("38 two children: no duplicate successor",
                tree.size() == 4 && tree.isValid());

        // --- range ---
        TestBst rangeTree = build(50, 30, 70, 20, 40, 60, 80);
        check("39 range: inclusive boundaries",
                rangeTree.range(30, 60).equals(
                        Arrays.asList(30, 40, 50, 60)));
        check("40 range: full range equals inorder",
                rangeTree.range(20, 80).equals(rangeTree.inorder()));
        check("41 range: no data inside valid range",
                rangeTree.range(41, 49).isEmpty());
        check("42 range: low greater than high is empty",
                rangeTree.range(60, 30).isEmpty());
        check("43 range: single key range",
                rangeTree.range(40, 40).equals(Arrays.asList(40)));

        // --- invariant ---
        TestBst skewed = build(10, 20, 30, 40, 50);
        check("44 invariant: skewed tree still valid", skewed.isValid());
        check("45 invariant: skewed height equals size-1",
                skewed.height() == skewed.size() - 1);
        TestBst drain = build(50, 30, 70, 20, 40, 60, 80);
        boolean validThroughout = true;
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            drain.remove(value);
            validThroughout = validThroughout && drain.isValid();
        }
        check("46 invariant: valid after every delete", validThroughout);
        check("47 invariant: drained tree is empty", drain.size() == 0);

        System.out.println();
        System.out.println("assertions=" + (passed + failed)
                + "  passed=" + passed + "  failed=" + failed);
    }
}
