import java.util.ArrayDeque;
import java.util.Deque;

class Customer {
    private String id;
    private String name;

    Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
class CounterQueue {
    private Deque<Customer> waiting = new ArrayDeque<>();

    boolean takeNumber(Customer customer) {
        if (customer == null) {
            System.out.println("加入失敗：顧客資料為 null");
            return false;
        }
        waiting.offerLast(customer);
        System.out.println("加入等候：" + customer + "，目前等候數 " + waiting.size());
        return true;
    }

    Customer peekNext() {
        Customer next = waiting.peekFirst();
        if (next == null) {
            System.out.println("目前沒有等候的顧客");
        } else {
            System.out.println("下一位：" + next);
        }
        return next;
    }

    Customer serveNext() {
        Customer served = waiting.pollFirst();
        if (served == null) {
            System.out.println("服務失敗：等候隊列已空");
        } else {
            System.out.println("服務中：" + served + "，剩餘等候數 " + waiting.size());
        }
        return served;
    }

    int waitingCount() {
        return waiting.size();
    }

    boolean isEmpty() {
        return waiting.isEmpty();
    }

    void printWaiting() {
        System.out.println("等候名單：" + waiting);
    }
}

public class CounterWaitingQueue {
    public static void main(String[] args) {
        CounterQueue counter = new CounterQueue();

        System.out.println("=== 空隊列處理 ===");
        counter.peekNext();
        counter.serveNext();
        System.out.println("是否為空：" + counter.isEmpty());

        System.out.println();
        System.out.println("=== 顧客報到 ===");
        counter.takeNumber(new Customer("A001", "Amy"));
        counter.takeNumber(new Customer("A002", "Ben"));
        counter.takeNumber(new Customer("A003", "Cindy"));
        counter.takeNumber(null);
        counter.printWaiting();

        System.out.println();
        System.out.println("=== 依 FIFO 叫號 ===");
        counter.peekNext();
        counter.serveNext();
        counter.serveNext();
        counter.printWaiting();
        System.out.println("等候數：" + counter.waitingCount());

        System.out.println();
        counter.serveNext();
        counter.serveNext();
        System.out.println("是否為空：" + counter.isEmpty());
    }
}
