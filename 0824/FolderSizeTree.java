// 課後作業四：目錄大小累加
// 需求：FolderNode 保存 name、ownSize、left、right。
//       使用 postorder 計算 subtree size，
//       輸出總大小、最大 subtree 與 leaf folder。

class FolderNode {
    String name;
    int ownSize;
    FolderNode left;
    FolderNode right;

    FolderNode(String name, int ownSize) {
        this.name = name;
        this.ownSize = Math.max(0, ownSize);
    }
}

public class FolderSizeTree {

    // postorder：先算完兩個 child，再加上自己的 ownSize
    static int totalSize(FolderNode node) {
        if (node == null) {
            return 0;
        }
        int leftTotal = totalSize(node.left);
        int rightTotal = totalSize(node.right);
        return leftTotal + rightTotal + node.ownSize;
    }

    // 同樣以 postorder 找出 subtree 總量最大的 node
    static FolderNode largestSubtree(FolderNode node) {
        if (node == null) {
            return null;
        }
        FolderNode best = node;
        int bestTotal = totalSize(node);

        FolderNode leftBest = largestSubtree(node.left);
        if (leftBest != null && totalSize(leftBest) > bestTotal) {
            best = leftBest;
            bestTotal = totalSize(leftBest);
        }

        FolderNode rightBest = largestSubtree(node.right);
        if (rightBest != null && totalSize(rightBest) > bestTotal) {
            best = rightBest;
        }
        return best;
    }

    static void printLeafFolders(FolderNode node) {
        if (node == null) {
            return;
        }
        if (node.left == null && node.right == null) {
            System.out.print(node.name + "(" + node.ownSize + ") ");
            return;
        }
        printLeafFolders(node.left);
        printLeafFolders(node.right);
    }

    static void printPostorder(FolderNode node) {
        if (node == null) {
            return;
        }
        printPostorder(node.left);
        printPostorder(node.right);
        System.out.println("  " + node.name + " own=" + node.ownSize
                + " subtree=" + totalSize(node));
    }

    //                root(10)
    //               /        \
    //         docs(20)       media(5)
    //          /     \            \
    //   report(40) draft(15)     video(120)
    private static FolderNode buildTree() {
        FolderNode root = new FolderNode("root", 10);
        root.left = new FolderNode("docs", 20);
        root.right = new FolderNode("media", 5);
        root.left.left = new FolderNode("report", 40);
        root.left.right = new FolderNode("draft", 15);
        root.right.right = new FolderNode("video", 120);
        return root;
    }

    public static void main(String[] args) {
        FolderNode root = buildTree();

        System.out.println("[postorder subtree size]");
        printPostorder(root);

        System.out.println("totalSize=" + totalSize(root));

        // root 的 subtree 必然是全部，因此比較 root 以外的 subtree 才有意義
        FolderNode leftBest = largestSubtree(root.left);
        FolderNode rightBest = largestSubtree(root.right);
        FolderNode largest = totalSize(leftBest) >= totalSize(rightBest)
                ? leftBest
                : rightBest;
        System.out.println("largestSubtree=" + largest.name
                + " total=" + totalSize(largest));

        System.out.print("leafFolders=");
        printLeafFolders(root);
        System.out.println();

        System.out.println("[empty tree]");
        System.out.println("totalSize=" + totalSize(null));
        System.out.println("largestSubtree=" + largestSubtree(null));
    }
}
