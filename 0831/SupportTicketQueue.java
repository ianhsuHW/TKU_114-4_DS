// 課堂實作題三：工作排程
// 需求：建立 Ticket(id, severity, createdOrder)。
//       severity 數字越大越優先；severity 相同時 createdOrder 越小越早。
//       依序取出全部 ticket 並輸出 id|severity|createdOrder。

import java.util.Comparator;
import java.util.PriorityQueue;

public class SupportTicketQueue {
    record Ticket(String id, int severity, int createdOrder) {
        Ticket {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
            if (createdOrder < 0) throw new IllegalArgumentException("createdOrder");
        }
    }

    // severity 由大到小；severity 相同時 createdOrder 由小到大，
    // 最後再用 id 當 tie-breaker，取出順序才完全可預測
    static final Comparator<Ticket> ORDER = Comparator
            .comparingInt(Ticket::severity).reversed()
            .thenComparingInt(Ticket::createdOrder)
            .thenComparing(Ticket::id);

    public static void main(String[] args) {
        PriorityQueue<Ticket> queue = new PriorityQueue<>(ORDER);
        queue.offer(new Ticket("T-101", 2, 1));
        queue.offer(new Ticket("T-102", 5, 2));
        queue.offer(new Ticket("T-103", 3, 3));
        queue.offer(new Ticket("T-104", 5, 4));
        queue.offer(new Ticket("T-105", 1, 5));
        queue.offer(new Ticket("T-106", 3, 0));

        System.out.println("size=" + queue.size());
        System.out.println("head=" + queue.peek().id());
        System.out.println();

        System.out.println("id|severity|createdOrder");
        while (!queue.isEmpty()) {
            Ticket ticket = queue.poll();
            System.out.println(ticket.id() + "|" + ticket.severity()
                    + "|" + ticket.createdOrder());
        }

        System.out.println();
        System.out.println("empty poll=" + queue.poll());
        System.out.println("empty peek=" + queue.peek());
    }
}
