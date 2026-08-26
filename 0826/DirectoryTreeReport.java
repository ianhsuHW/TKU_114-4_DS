// 課後作業一：檔案系統統計
// 需求：Node 可表示 file 或 directory。
//       使用 postorder 計算每個 directory 的總容量，
//       並輸出 total node、file count、directory count、height 與最大檔案。

class FsNode {
    final String name;
    final boolean directory;
    final int fileSize;     // directory 本身不佔容量，固定為 0
    FsNode left;
    FsNode right;

    private FsNode(String name, boolean directory, int fileSize) {
        this.name = name;
        this.directory = directory;
        this.fileSize = directory ? 0 : Math.max(0, fileSize);
    }

    static FsNode file(String name, int fileSize) {
        return new FsNode(name, false, fileSize);
    }

    static FsNode directory(String name) {
        return new FsNode(name, true, 0);
    }
}

public class DirectoryTreeReport {

    // postorder：先算完兩個 child，再合併成自己的總容量
    static int totalSize(FsNode node) {
        if (node == null) {
            return 0;
        }
        int leftTotal = totalSize(node.left);
        int rightTotal = totalSize(node.right);
        return leftTotal + rightTotal + node.fileSize;
    }

    static int totalNodes(FsNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + totalNodes(node.left) + totalNodes(node.right);
    }

    static int fileCount(FsNode node) {
        if (node == null) {
            return 0;
        }
        int current = node.directory ? 0 : 1;
        return current + fileCount(node.left) + fileCount(node.right);
    }

    static int directoryCount(FsNode node) {
        if (node == null) {
            return 0;
        }
        int current = node.directory ? 1 : 0;
        return current + directoryCount(node.left) + directoryCount(node.right);
    }

    static int height(FsNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    // 回傳容量最大的 file node，沒有 file 時回傳 null
    static FsNode largestFile(FsNode node) {
        if (node == null) {
            return null;
        }
        FsNode best = node.directory ? null : node;
        FsNode leftBest = largestFile(node.left);
        FsNode rightBest = largestFile(node.right);
        best = larger(best, leftBest);
        best = larger(best, rightBest);
        return best;
    }

    private static FsNode larger(FsNode first, FsNode second) {
        if (first == null) return second;
        if (second == null) return first;
        return second.fileSize > first.fileSize ? second : first;
    }

    // postorder 列出每個 directory 的總容量
    static void printDirectorySizes(FsNode node) {
        if (node == null) {
            return;
        }
        printDirectorySizes(node.left);
        printDirectorySizes(node.right);
        if (node.directory) {
            System.out.println("  dir  " + node.name
                    + " total=" + totalSize(node));
        }
    }

    static void printTree(FsNode node, int depth) {
        if (node == null) {
            return;
        }
        String kind = node.directory
                ? "[dir ] total=" + totalSize(node)
                : "[file] size=" + node.fileSize;
        System.out.println("  " + "  ".repeat(depth) + node.name + " " + kind);
        printTree(node.left, depth + 1);
        printTree(node.right, depth + 1);
    }

    //                    root(dir)
    //                  /           \
    //           docs(dir)          media(dir)
    //           /       \                \
    //   report.pdf   drafts(dir)      video.mp4
    //                   /      \
    //             a.txt        b.txt
    private static FsNode buildTree() {
        FsNode root = FsNode.directory("root");
        FsNode docs = FsNode.directory("docs");
        FsNode media = FsNode.directory("media");
        FsNode drafts = FsNode.directory("drafts");

        root.left = docs;
        root.right = media;
        docs.left = FsNode.file("report.pdf", 40);
        docs.right = drafts;
        drafts.left = FsNode.file("a.txt", 15);
        drafts.right = FsNode.file("b.txt", 8);
        media.right = FsNode.file("video.mp4", 120);
        return root;
    }

    private static void report(String title, FsNode root) {
        System.out.println("[" + title + "]");
        System.out.println("structure (preorder):");
        printTree(root, 0);
        System.out.println("directory totals (postorder):");
        printDirectorySizes(root);
        System.out.println("totalNodes=" + totalNodes(root));
        System.out.println("fileCount=" + fileCount(root));
        System.out.println("directoryCount=" + directoryCount(root));
        System.out.println("height=" + height(root));
        System.out.println("totalSize=" + totalSize(root));
        FsNode largest = largestFile(root);
        System.out.println("largestFile="
                + (largest == null
                        ? "none"
                        : largest.name + " size=" + largest.fileSize));
        System.out.println();
    }

    public static void main(String[] args) {
        report("sample file system", buildTree());
        report("empty tree", null);
        report("single file", FsNode.file("only.txt", 7));
        report("empty directory", FsNode.directory("empty"));
    }
}
