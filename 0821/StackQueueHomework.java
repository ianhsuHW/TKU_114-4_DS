import java.util.ArrayDeque;
import java.util.Deque;

public class StackQueueHomework {
    public static void main(String[] args) {
        Deque<String> stack = new ArrayDeque<>();
        Deque<String> queue = new ArrayDeque<>();

        stack.offerLast("A");
        stack.offerLast("B");
        stack.offerLast("C");

        System.out.println("Stack LIFO：");
        System.out.println("pop=" + stack.pollLast());
        System.out.println("next=" + stack.peekLast());

        queue.offerLast("A");
        queue.offerLast("B");
        queue.offerLast("C");

        System.out.println("Queue FIFO：");
        System.out.println("poll=" + queue.pollFirst());
        System.out.println("next=" + queue.peekFirst());
    }
}
