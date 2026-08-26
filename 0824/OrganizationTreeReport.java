// 課後作業五：組織架構報表
// 需求：在組織架構樹上新增 findParent、findDepth、pathFromRoot 與 printByLevel。
//       找不到單位時回傳空結果，不得發生例外。

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

class ReportOrgNode {
    String name;
    ReportOrgNode left;
    ReportOrgNode right;

    ReportOrgNode(String name) {
        this.name = name;
    }
}

class OrganizationReport {
    private final ReportOrgNode root;

    OrganizationReport(ReportOrgNode root) {
        this.root = root;
    }

    // 找不到或本身是 root 時回傳 null
    String findParent(String target) {
        ReportOrgNode parent = findParent(root, target);
        return parent == null ? null : parent.name;
    }

    private ReportOrgNode findParent(ReportOrgNode node, String target) {
        if (node == null || target == null) {
            return null;
        }
        if (node.left != null && node.left.name.equals(target)) {
            return node;
        }
        if (node.right != null && node.right.name.equals(target)) {
            return node;
        }
        ReportOrgNode fromLeft = findParent(node.left, target);
        return fromLeft != null ? fromLeft : findParent(node.right, target);
    }

    // 找不到回傳 -1
    int findDepth(String target) {
        return findDepth(root, target);
    }

    private int findDepth(ReportOrgNode node, String target) {
        if (node == null || target == null) {
            return -1;
        }
        if (node.name.equals(target)) {
            return 0;
        }
        int leftDepth = findDepth(node.left, target);
        if (leftDepth >= 0) {
            return leftDepth + 1;
        }
        int rightDepth = findDepth(node.right, target);
        if (rightDepth >= 0) {
            return rightDepth + 1;
        }
        return -1;
    }

    // 找不到時回傳 empty list
    List<String> pathFromRoot(String target) {
        List<String> path = new ArrayList<>();
        if (!collectPath(root, target, path)) {
            path.clear();
        }
        return path;
    }

    private boolean collectPath(ReportOrgNode node, String target,
                                List<String> path) {
        if (node == null || target == null) {
            return false;
        }
        path.add(node.name);
        if (node.name.equals(target)) {
            return true;
        }
        if (collectPath(node.left, target, path)
                || collectPath(node.right, target, path)) {
            return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    void printByLevel() {
        if (root == null) {
            System.out.println("empty organization");
            return;
        }
        Queue<ReportOrgNode> queue = new ArrayDeque<>();
        queue.offer(root);
        int level = 0;
        while (!queue.isEmpty()) {
            int countInLevel = queue.size();
            StringBuilder line = new StringBuilder();
            for (int index = 0; index < countInLevel; index++) {
                ReportOrgNode node = queue.poll();
                line.append(node.name).append(" ");
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            System.out.println("level " + level + ": "
                    + line.toString().trim());
            level++;
        }
    }
}

public class OrganizationTreeReport {

    private static ReportOrgNode buildOrganization() {
        ReportOrgNode root = new ReportOrgNode("HeadOffice");
        root.left = new ReportOrgNode("Sales");
        root.right = new ReportOrgNode("Technology");
        root.left.left = new ReportOrgNode("Domestic");
        root.left.right = new ReportOrgNode("Export");
        root.right.left = new ReportOrgNode("Platform");
        root.right.right = new ReportOrgNode("Support");
        root.right.right.left = new ReportOrgNode("Helpdesk");
        return root;
    }

    public static void main(String[] args) {
        OrganizationReport report =
                new OrganizationReport(buildOrganization());

        report.printByLevel();

        System.out.println("parent(Export)=" + report.findParent("Export"));
        System.out.println("parent(HeadOffice)="
                + report.findParent("HeadOffice"));
        System.out.println("parent(HR)=" + report.findParent("HR"));

        System.out.println("depth(HeadOffice)="
                + report.findDepth("HeadOffice"));
        System.out.println("depth(Helpdesk)=" + report.findDepth("Helpdesk"));
        System.out.println("depth(HR)=" + report.findDepth("HR"));

        System.out.println("path(Helpdesk)=" + report.pathFromRoot("Helpdesk"));
        System.out.println("path(Domestic)=" + report.pathFromRoot("Domestic"));
        System.out.println("path(HR)=" + report.pathFromRoot("HR"));

        System.out.println("[empty organization]");
        OrganizationReport empty = new OrganizationReport(null);
        empty.printByLevel();
        System.out.println("parent=" + empty.findParent("Sales"));
        System.out.println("depth=" + empty.findDepth("Sales"));
        System.out.println("path=" + empty.pathFromRoot("Sales"));
    }
}
