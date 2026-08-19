class ArrayStack {
    private String[] data;
    private int top;

    ArrayStack(int capacity) {
        data = new String[capacity];
        top = -1;
    }

    boolean push(String value) {
        if (top + 1 >= data.length) {
            return false;
        }
        data[++top] = value;
        return true;
    }

    String pop() {
        if (top < 0) {
            return null;
        }
        String value = data[top];
        data[top] = null;
        top--;
        return value;
    }

    String peek() {
        if (top < 0) {
            return null;
        }
        return data[top];
    }

    boolean isEmpty() {
        return top < 0;
    }
}

public class ArrayStackDemo {
    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(4);

        System.out.println("push A：" + stack.push("A"));
        System.out.println("push B：" + stack.push("B"));
        System.out.println("top：" + stack.peek());
        System.out.println("pop：" + stack.pop());
        System.out.println("top：" + stack.peek());
    }
}
