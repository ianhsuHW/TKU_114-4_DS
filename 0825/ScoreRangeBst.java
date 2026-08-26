// 課後作業三：排名範圍查詢
// 需求：Key 使用 score + studentId 複合順序，支援同分資料，
//       並輸出指定分數範圍。

class ScoreEntry {
    final int score;
    final int studentId;
    final String name;

    ScoreEntry(int score, int studentId, String name) {
        this.score = score;
        this.studentId = studentId;
        this.name = name;
    }

    // 複合順序：先比 score，同分再比 studentId
    int compareTo(ScoreEntry other) {
        if (score != other.score) {
            return Integer.compare(score, other.score);
        }
        return Integer.compare(studentId, other.studentId);
    }

    @Override
    public String toString() {
        return score + "/" + studentId + " " + name;
    }
}

class ScoreNode {
    ScoreEntry data;
    ScoreNode left;
    ScoreNode right;

    ScoreNode(ScoreEntry data) {
        this.data = data;
    }
}

class ScoreBst {
    private ScoreNode root;

    boolean add(ScoreEntry entry) {
        if (entry == null) return false;
        if (root == null) {
            root = new ScoreNode(entry);
            return true;
        }
        ScoreNode current = root;
        while (true) {
            int order = entry.compareTo(current.data);
            if (order == 0) return false;           // 同 score 同 id 才算重複
            if (order < 0) {
                if (current.left == null) {
                    current.left = new ScoreNode(entry);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ScoreNode(entry);
                    return true;
                }
                current = current.right;
            }
        }
    }

    ScoreEntry find(int score, int studentId) {
        ScoreEntry key = new ScoreEntry(score, studentId, "");
        ScoreNode current = root;
        while (current != null) {
            int order = key.compareTo(current.data);
            if (order == 0) return current.data;
            current = order < 0 ? current.left : current.right;
        }
        return null;
    }

    // 分數範圍查詢，含端點；同分資料全部列出
    void printScoreRange(int lowScore, int highScore) {
        System.out.println("scoreRange[" + lowScore + "," + highScore + "]");
        if (lowScore > highScore) {
            System.out.println("  invalid range, empty result");
            return;
        }
        printScoreRange(root, lowScore, highScore);
    }

    private void printScoreRange(ScoreNode node, int low, int high) {
        if (node == null) return;
        // 同分可能還在左子樹，因此 >= 才剪枝
        if (node.data.score >= low) {
            printScoreRange(node.left, low, high);
        }
        if (low <= node.data.score && node.data.score <= high) {
            System.out.println("  " + node.data);
        }
        if (node.data.score <= high) {
            printScoreRange(node.right, low, high);
        }
    }

    void inorder() {
        System.out.println("inorder (score asc, same score by id)");
        inorder(root);
    }

    private void inorder(ScoreNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.println("  " + node.data);
        inorder(node.right);
    }
}

public class ScoreRangeBst {
    public static void main(String[] args) {
        ScoreBst tree = new ScoreBst();
        tree.add(new ScoreEntry(78, 305, "Mina"));
        tree.add(new ScoreEntry(91, 101, "Leo"));
        tree.add(new ScoreEntry(78, 118, "Ivy"));      // 同分不同 id
        tree.add(new ScoreEntry(65, 402, "Ken"));
        tree.add(new ScoreEntry(91, 220, "Nora"));     // 同分不同 id
        tree.add(new ScoreEntry(84, 512, "Ray"));
        tree.add(new ScoreEntry(78, 201, "Sam"));      // 同分不同 id
        System.out.println("duplicate key="
                + tree.add(new ScoreEntry(78, 118, "Copy")));

        tree.inorder();
        System.out.println();

        tree.printScoreRange(78, 91);
        System.out.println();
        tree.printScoreRange(78, 78);   // 只取同一個分數
        System.out.println();
        tree.printScoreRange(60, 70);
        System.out.println();
        tree.printScoreRange(95, 99);   // 沒有資料
        System.out.println();
        tree.printScoreRange(91, 78);   // low > high

        System.out.println();
        System.out.println("find(78,201)=" + tree.find(78, 201));
        System.out.println("find(78,999)=" + tree.find(78, 999));
    }
}
