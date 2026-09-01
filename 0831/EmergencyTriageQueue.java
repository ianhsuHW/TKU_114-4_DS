// 課後作業一：急診候診佇列
// 需求：依危急程度、到院順序與病歷號建立穩定的 Priority Queue，
//       支援報到、查看下一位、叫號與查詢目前人數。
//       輸出每次叫號結果及空佇列處理。

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class EmergencyTriageQueue {

    // level 1 最危急，數字越小越優先
    record Patient(String chartNo, int level, int arrivalOrder) {
        Patient {
            if (chartNo == null || chartNo.isBlank()) {
                throw new IllegalArgumentException("chartNo");
            }
            if (level < 1 || level > 5) throw new IllegalArgumentException("level");
        }

        @Override
        public String toString() {
            return chartNo + "(level=" + level + ",arrival=" + arrivalOrder + ")";
        }
    }

    // 主排序是危急程度，同級用到院順序，最後用病歷號，
    // 三層都指定後同級病患的叫號順序才是穩定的
    private static final Comparator<Patient> ORDER = Comparator
            .comparingInt(Patient::level)
            .thenComparingInt(Patient::arrivalOrder)
            .thenComparing(Patient::chartNo);

    private final PriorityQueue<Patient> waiting = new PriorityQueue<>(ORDER);
    private int arrivalCounter = 0;

    // 報到：到院順序由佇列自己編號，呼叫端不必維護
    public Patient checkIn(String chartNo, int level) {
        Patient patient = new Patient(chartNo, level, ++arrivalCounter);
        waiting.offer(patient);
        System.out.println("check-in  " + patient + "  waiting=" + waitingCount());
        return patient;
    }

    public Patient peekNext() {
        return waiting.peek();
    }

    public Patient callNext() {
        Patient next = waiting.poll();
        if (next == null) throw new NoSuchElementException("no patient waiting");
        return next;
    }

    public int waitingCount() {
        return waiting.size();
    }

    public boolean isEmpty() {
        return waiting.isEmpty();
    }

    public static void main(String[] args) {
        EmergencyTriageQueue queue = new EmergencyTriageQueue();

        System.out.println("[empty queue]");
        System.out.println("waiting=" + queue.waitingCount()
                + " peekNext=" + queue.peekNext());
        try {
            queue.callNext();
        } catch (NoSuchElementException e) {
            System.out.println("callNext() -> NoSuchElementException: " + e.getMessage());
        }

        System.out.println();
        System.out.println("[check-in]");
        queue.checkIn("A-001", 3);
        queue.checkIn("A-002", 1);
        queue.checkIn("A-003", 2);
        queue.checkIn("A-004", 1);   // 與 A-002 同級，到院較晚
        queue.checkIn("A-005", 4);
        queue.checkIn("A-006", 2);

        System.out.println();
        System.out.println("[call]");
        System.out.println("next=" + queue.peekNext());
        while (!queue.isEmpty()) {
            Patient patient = queue.callNext();
            System.out.println("call " + patient + "  remaining=" + queue.waitingCount());
        }

        System.out.println();
        System.out.println("[drained]");
        System.out.println("waiting=" + queue.waitingCount()
                + " peekNext=" + queue.peekNext());
        try {
            queue.callNext();
        } catch (NoSuchElementException e) {
            System.out.println("callNext() -> NoSuchElementException: " + e.getMessage());
        }
    }
}
