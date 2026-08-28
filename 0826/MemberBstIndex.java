// 課堂實作題六：Member BST Index
// 需求：Node 保存 Member object，以 memberId 作 key，
//       完成 add、find、updateEmail、remove 與 inorder report。
//       Id 不可重複，email 不得為 blank。

import java.util.ArrayList;
import java.util.List;

class Member {
    final int memberId;
    final String name;
    String email;

    Member(int memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString() {
        return memberId + " " + name + " <" + email + ">";
    }
}

class MemberNode {
    Member data;
    MemberNode left;
    MemberNode right;

    MemberNode(Member data) {
        this.data = data;
    }
}

class MemberIndex {

    private MemberNode root;

    static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    boolean add(Member member) {
        if (member == null || isBlank(member.email)) return false;
        if (root == null) {
            root = new MemberNode(member);
            return true;
        }
        MemberNode current = root;
        while (true) {
            if (member.memberId == current.data.memberId) {
                return false;                       // id 不可重複
            }
            if (member.memberId < current.data.memberId) {
                if (current.left == null) {
                    current.left = new MemberNode(member);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new MemberNode(member);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Member find(int memberId) {
        MemberNode current = root;
        while (current != null) {
            if (memberId == current.data.memberId) return current.data;
            current = memberId < current.data.memberId
                    ? current.left
                    : current.right;
        }
        return null;
    }

    boolean updateEmail(int memberId, String email) {
        if (isBlank(email)) return false;           // blank email 不接受
        Member member = find(memberId);
        if (member == null) return false;
        member.email = email;
        return true;
    }

    boolean remove(int memberId) {
        if (find(memberId) == null) return false;
        root = remove(root, memberId);
        return true;
    }

    private MemberNode remove(MemberNode node, int memberId) {
        if (node == null) return null;
        if (memberId < node.data.memberId) {
            node.left = remove(node.left, memberId);
        } else if (memberId > node.data.memberId) {
            node.right = remove(node.right, memberId);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            MemberNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.memberId);
        }
        return node;
    }

    private MemberNode minimumNode(MemberNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<Member> inorder() {
        List<Member> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(MemberNode node, List<Member> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    void report(String title) {
        System.out.println("[" + title + "]");
        List<Member> members = inorder();
        if (members.isEmpty()) {
            System.out.println("  (no member)");
        }
        for (Member member : members) {
            System.out.println("  " + member);
        }
        System.out.println("  count=" + members.size());
        System.out.println();
    }
}

public class MemberBstIndex {
    public static void main(String[] args) {
        MemberIndex index = new MemberIndex();

        System.out.println("add 3005="
                + index.add(new Member(3005, "Mina", "mina@example.com")));
        System.out.println("add 1002="
                + index.add(new Member(1002, "Leo", "leo@example.com")));
        System.out.println("add 4100="
                + index.add(new Member(4100, "Nora", "nora@example.com")));
        System.out.println("add 2008="
                + index.add(new Member(2008, "Ivy", "ivy@example.com")));
        System.out.println("add 4500="
                + index.add(new Member(4500, "Ken", "ken@example.com")));
        System.out.println("add 1500="
                + index.add(new Member(1500, "Rex", "rex@example.com")));

        System.out.println("duplicate 1002="
                + index.add(new Member(1002, "Copy", "copy@example.com")));
        System.out.println("blank email="
                + index.add(new Member(5000, "Blank", "   ")));
        System.out.println("null email="
                + index.add(new Member(5001, "Null", null)));
        System.out.println("null member=" + index.add(null));

        index.report("after add");

        System.out.println("find(2008)=" + index.find(2008));
        System.out.println("find(9999)=" + index.find(9999));

        System.out.println("updateEmail(2008)="
                + index.updateEmail(2008, "ivy.new@example.com"));
        System.out.println("updateEmail blank="
                + index.updateEmail(2008, "  "));
        System.out.println("updateEmail missing="
                + index.updateEmail(9999, "x@example.com"));

        System.out.println("remove leaf 1500=" + index.remove(1500));
        System.out.println("remove one-child 4100=" + index.remove(4100));
        System.out.println("remove two-children 3005=" + index.remove(3005));
        System.out.println("remove missing 9999=" + index.remove(9999));

        index.report("after update and remove");
    }
}
