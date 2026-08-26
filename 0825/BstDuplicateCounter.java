// 課堂實作題二：Duplicate Policy
// 需求：Node 增加 count，相同 key 不建立新 node 而是增加 count。
//       Inorder 輸出 key(count)。

class CounterNode {
    int key;
    int count;
    CounterNode left;
    CounterNode right;

    CounterNode(int key) {
        this.key = key;
        this.count = 1;
    }
}

class CounterBst {
    private CounterNode root;
    private int distinctKeys;
    private int totalInserts;

    // 回傳 true 代表建立新 node，false 代表只增加既有 node 的 count
    boolean add(int key) {
        totalInserts++;
        if (root == null) {
            root = new CounterNode(key);
            distinctKeys++;
            return true;
        }
        CounterNode current = root;
        while (true) {
            if (key == current.key) {
                current.count++;
                return false;
            }
            if (key < current.key) {
                if (current.left == null) {
                    current.left = new CounterNode(key);
                    distinctKeys++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new CounterNode(key);
                    distinctKeys++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    int countOf(int key) {
        CounterNode current = root;
        while (current != null) {
            if (key == current.key) return current.count;
            current = key < current.key ? current.left : current.right;
        }
        return 0;
    }

    int distinctKeys() {
        return distinctKeys;
    }

    int totalInserts() {
        return totalInserts;
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(CounterNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.key + "(" + node.count + ") ");
        inorder(node.right);
    }
}

public class BstDuplicateCounter {
    public static void main(String[] args) {
        CounterBst tree = new CounterBst();
        int[] data = {50, 30, 70, 30, 20, 50, 50, 40, 70, 20};

        for (int key : data) {
            boolean created = tree.add(key);
            System.out.println("add " + key + " -> newNode=" + created);
        }

        System.out.print("inorder=");
        tree.inorder();
        System.out.println("distinctKeys=" + tree.distinctKeys());
        System.out.println("totalInserts=" + tree.totalInserts());
        System.out.println("countOf(50)=" + tree.countOf(50));
        System.out.println("countOf(40)=" + tree.countOf(40));
        System.out.println("countOf(99)=" + tree.countOf(99));
    }
}
