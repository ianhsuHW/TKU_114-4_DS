import java.util.Arrays;

class CircularQueue<T> {
    private Object[] data;
    private int front;
    private int rear;
    private int size;

    CircularQueue(int capacity) {
        data = new Object[Math.max(1, capacity)];
    }

    boolean enqueue(T value) {
        if (value == null || isFull()) {
            return false;
        }
        data[rear] = value;
        rear = (rear + 1) % data.length;
        size++;
        return true;
    }

    @SuppressWarnings("unchecked")
    T dequeue() {
        if (isEmpty()) {
            return null;
        }
        T value = (T) data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size--;
        return value;
    }

    @SuppressWarnings("unchecked")
    T peek() {
        return isEmpty() ? null : (T) data[front];
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

    void printState(String action) {
        System.out.printf("%-12s array=%-24s front=%d rear=%d size=%d%n",
                action, Arrays.toString(data), front, rear, size);
    }
}

public class CircularQueuePractice {
    public static void main(String[] args) {
        CircularQueue<String> queue = new CircularQueue<>(4);
        queue.printState("初始");

        enqueue(queue, "A");
        enqueue(queue, "B");
        enqueue(queue, "C");

        dequeue(queue);
        dequeue(queue);

        enqueue(queue, "D");
        enqueue(queue, "E");
        enqueue(queue, "F");

        dequeue(queue);

        enqueue(queue, "G");

        System.out.println();
        System.out.println("=== 依 FIFO 取出所有元素 ===");
        while (!queue.isEmpty()) {
            String value = queue.dequeue();
            queue.printState("dequeue " + value);
        }
        System.out.println("最終 isEmpty：" + queue.isEmpty());
    }

    static void enqueue(CircularQueue<String> queue, String value) {
        boolean ok = queue.enqueue(value);
        queue.printState("enqueue " + value + (ok ? "" : "(滿)"));
    }

    static void dequeue(CircularQueue<String> queue) {
        String value = queue.dequeue();
        queue.printState("dequeue " + (value == null ? "(空)" : value));
    }
}
