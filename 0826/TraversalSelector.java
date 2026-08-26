// 課堂實作題二：Traversal Selector
// 需求：建立 expression tree，分別以 preorder、inorder、postorder
//       輸出 prefix、infix、postfix。
//       Infix 必須加入括號，讓運算順序明確。

class ExpressionNode {
    String token;
    ExpressionNode left;
    ExpressionNode right;

    ExpressionNode(String token) {
        this.token = token;
    }

    boolean isOperator() {
        return left != null || right != null;
    }
}

public class TraversalSelector {

    // preorder：operator 在 operand 之前
    static String prefix(ExpressionNode node) {
        if (node == null) {
            return "";
        }
        if (!node.isOperator()) {
            return node.token;
        }
        return node.token + " " + prefix(node.left) + " " + prefix(node.right);
    }

    // inorder：operator 在中間，operator node 一律加括號
    static String infix(ExpressionNode node) {
        if (node == null) {
            return "";
        }
        if (!node.isOperator()) {
            return node.token;
        }
        return "(" + infix(node.left) + " " + node.token + " "
                + infix(node.right) + ")";
    }

    // postorder：operator 在 operand 之後
    static String postfix(ExpressionNode node) {
        if (node == null) {
            return "";
        }
        if (!node.isOperator()) {
            return node.token;
        }
        return postfix(node.left) + " " + postfix(node.right) + " "
                + node.token;
    }

    static int evaluate(ExpressionNode node) {
        if (node == null) {
            return 0;
        }
        if (!node.isOperator()) {
            return Integer.parseInt(node.token);
        }
        int leftValue = evaluate(node.left);
        int rightValue = evaluate(node.right);
        switch (node.token) {
            case "+": return leftValue + rightValue;
            case "-": return leftValue - rightValue;
            case "*": return leftValue * rightValue;
            case "/": return leftValue / rightValue;
            default: throw new IllegalStateException("unknown operator "
                    + node.token);
        }
    }

    private static ExpressionNode operator(String token,
                                           ExpressionNode left,
                                           ExpressionNode right) {
        ExpressionNode node = new ExpressionNode(token);
        node.left = left;
        node.right = right;
        return node;
    }

    private static ExpressionNode number(int value) {
        return new ExpressionNode(String.valueOf(value));
    }

    //        *
    //       / \
    //      +   -
    //     / \ / \
    //    3  5 10 4
    private static ExpressionNode firstExpression() {
        return operator("*",
                operator("+", number(3), number(5)),
                operator("-", number(10), number(4)));
    }

    //          +
    //         / \
    //        2   /
    //           / \
    //          *   6
    //         / \
    //        9   4
    private static ExpressionNode secondExpression() {
        return operator("+",
                number(2),
                operator("/",
                        operator("*", number(9), number(4)),
                        number(6)));
    }

    private static void report(String title, ExpressionNode root) {
        System.out.println("[" + title + "]");
        System.out.println("  prefix (preorder)  = " + prefix(root));
        System.out.println("  infix  (inorder)   = " + infix(root));
        System.out.println("  postfix(postorder) = " + postfix(root));
        System.out.println("  value              = " + evaluate(root));
        System.out.println();
    }

    public static void main(String[] args) {
        report("(3 + 5) * (10 - 4)", firstExpression());
        report("2 + (9 * 4) / 6", secondExpression());
        report("single operand", number(7));
    }
}
