// 課堂實作題五：逐層分行輸出
// 需求：使用 Queue 將每層輸出在不同行，並輸出每層 node count。
//       必須處理 empty tree，不得用 DFS 假裝 level-order。

import java.util.ArrayDeque;
import java.util.Queue;

class LineNode {
    String value;
    LineNode left;
    LineNode right;

    LineNode(String value) {
        this.value = value;
    }
}

public class LevelOrderByLine {

    static void printByLevel(LineNode root) {
        if (root == null) {
            System.out.println("empty tree");
            return;
        }

        Queue<LineNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;

        while (!queue.isEmpty()) {
            int countInLevel = queue.size();   // 這一層目前全部的 node
            StringBuilder line = new StringBuilder();

            for (int index = 0; index < countInLevel; index++) {
                LineNode current = queue.poll();
                line.append(current.value).append(" ");
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }

            System.out.println("level " + level + " (count=" + countInLevel
                    + "): " + line.toString().trim());
            level++;
        }
    }

    //            A
    //         /     \
    //        B       C
    //       / \       \
    //      D   E       F
    //     /
    //    G
    private static LineNode buildTree() {
        LineNode root = new LineNode("A");
        root.left = new LineNode("B");
        root.right = new LineNode("C");
        root.left.left = new LineNode("D");
        root.left.right = new LineNode("E");
        root.right.right = new LineNode("F");
        root.left.left.left = new LineNode("G");
        return root;
    }

    public static void main(String[] args) {
        System.out.println("[normal tree]");
        printByLevel(buildTree());

        System.out.println("[single node]");
        printByLevel(new LineNode("only"));

        System.out.println("[empty tree]");
        printByLevel(null);
    }
}
