class ArrayStack<T> {
    private Object[] data;
    private int size;

    ArrayStack(int capacity) {
        data = new Object[Math.max(1, capacity)];
    }

    boolean push(T value) {
        if (value == null || isFull()) {
            return false;
        }
        data[size] = value;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    T pop() {
        if (isEmpty()) {
            return null;
        }
        size--;
        T value = (T) data[size];
        data[size] = null;
        return value;
    }

    @SuppressWarnings("unchecked")
    T peek() {
        return isEmpty() ? null : (T) data[size - 1];
    }

    int size() {
        return size;
    }

    boolean isEmpty() {
        return size == 0;
    }

    boolean isFull() {
        return size == data.length;
    }
}

public class GenericArrayStackDemo {
    public static void main(String[] args) {
        System.out.println("=== ArrayStack<String> ===");
        ArrayStack<String> words = new ArrayStack<>(3);
        System.out.println("isEmpty：" + words.isEmpty());
        System.out.println("空 stack pop：" + words.pop());
        System.out.println("push A：" + words.push("A"));
        System.out.println("push B：" + words.push("B"));
        System.out.println("push C：" + words.push("C"));
        System.out.println("isFull：" + words.isFull());
        System.out.println("push D：" + words.push("D"));
        System.out.println("peek：" + words.peek());
        System.out.println("pop：" + words.pop());
        System.out.println("size：" + words.size());

        System.out.println();
        System.out.println("=== ArrayStack<Integer> ===");
        ArrayStack<Integer> numbers = new ArrayStack<>(2);
        System.out.println("push 10：" + numbers.push(10));
        System.out.println("push 20：" + numbers.push(20));
        System.out.println("push 30：" + numbers.push(30));
        System.out.println("peek：" + numbers.peek());

        int total = 0;
        while (!numbers.isEmpty()) {
            total += numbers.pop();
        }
        System.out.println("全部 pop 後總和：" + total);
        System.out.println("size：" + numbers.size());
        System.out.println("isEmpty：" + numbers.isEmpty());
    }
}
