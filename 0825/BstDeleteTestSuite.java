// 課後作業四：完整 Delete 測試
// 需求：測試 empty、missing、single root、root with one child、
//       root with two children 與連續刪除到 empty。

class SuiteNode {
    int value;
    SuiteNode left;
    SuiteNode right;

    SuiteNode(int value) {
        this.value = value;
    }
}

class SuiteBst {
    private SuiteNode root;

    boolean add(int value) {
        if (root == null) {
            root = new SuiteNode(value);
            return true;
        }
        SuiteNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new SuiteNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new SuiteNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        SuiteNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }

    private SuiteNode remove(SuiteNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            SuiteNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }

    private SuiteNode minimumNode(SuiteNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    int size() {
        return size(root);
    }

    private int size(SuiteNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    private boolean isValid(SuiteNode node, long low, long high) {
        if (node == null) return true;
        if (node.value <= low || node.value >= high) return false;
        return isValid(node.left, low, node.value)
                && isValid(node.right, node.value, high);
    }

    String inorder() {
        StringBuilder text = new StringBuilder();
        inorder(root, text);
        return text.toString().trim();
    }

    private void inorder(SuiteNode node, StringBuilder text) {
        if (node == null) return;
        inorder(node.left, text);
        text.append(node.value).append(" ");
        inorder(node.right, text);
    }
}

public class BstDeleteTestSuite {

    private static int passed = 0;
    private static int failed = 0;

    private static void check(String description, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS  " + description);
        } else {
            failed++;
            System.out.println("FAIL  " + description);
        }
    }

    private static SuiteBst build(int... values) {
        SuiteBst tree = new SuiteBst();
        for (int value : values) {
            tree.add(value);
        }
        return tree;
    }

    public static void main(String[] args) {
        // 1. empty tree
        SuiteBst empty = build();
        check("empty: remove returns false", !empty.remove(10));
        check("empty: size stays 0", empty.size() == 0);
        check("empty: still valid", empty.isValid());

        // 2. missing target
        SuiteBst normal = build(50, 30, 70, 20, 40, 60, 80);
        check("missing: remove(65) false", !normal.remove(65));
        check("missing: size unchanged", normal.size() == 7);
        check("missing: inorder unchanged",
                normal.inorder().equals("20 30 40 50 60 70 80"));

        // 3. single root
        SuiteBst singleRoot = build(42);
        check("single root: remove true", singleRoot.remove(42));
        check("single root: size 0", singleRoot.size() == 0);
        check("single root: inorder empty", singleRoot.inorder().isEmpty());
        check("single root: valid", singleRoot.isValid());

        // 4. root with one child (right)
        SuiteBst rootRightChild = build(50, 70);
        check("root one child(right): remove root", rootRightChild.remove(50));
        check("root one child(right): child becomes root",
                rootRightChild.inorder().equals("70"));
        check("root one child(right): valid", rootRightChild.isValid());

        // 4b. root with one child (left)
        SuiteBst rootLeftChild = build(50, 30, 20);
        check("root one child(left): remove root", rootLeftChild.remove(50));
        check("root one child(left): subtree kept",
                rootLeftChild.inorder().equals("20 30"));
        check("root one child(left): valid", rootLeftChild.isValid());

        // 5. root with two children
        SuiteBst rootTwoChildren = build(50, 30, 70, 20, 40, 60, 80);
        check("root two children: remove root", rootTwoChildren.remove(50));
        check("root two children: successor 60 replaces root",
                rootTwoChildren.inorder().equals("20 30 40 60 70 80"));
        check("root two children: size 6", rootTwoChildren.size() == 6);
        check("root two children: no duplicate successor",
                rootTwoChildren.isValid());

        // 6. 連續刪除到 empty
        SuiteBst drain = build(50, 30, 70, 20, 40, 60, 80);
        boolean allRemoved = true;
        boolean stayedValid = true;
        for (int value : new int[]{20, 40, 30, 60, 80, 70, 50}) {
            allRemoved = allRemoved && drain.remove(value);
            stayedValid = stayedValid && drain.isValid();
        }
        check("drain: every remove returned true", allRemoved);
        check("drain: valid after every step", stayedValid);
        check("drain: size 0", drain.size() == 0);
        check("drain: inorder empty", drain.inorder().isEmpty());
        check("drain: remove again false", !drain.remove(50));

        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
    }
}
