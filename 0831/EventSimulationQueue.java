// 課後作業二：活動事件模擬器
// 需求：事件包含時間、類型與 sequence。依時間先後執行；時間相同依 sequence。
//       支援取消指定事件，並輸出完整執行紀錄。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class EventSimulationQueue {

    record Event(int time, String type, int sequence) {
        Event {
            if (time < 0) throw new IllegalArgumentException("time");
            if (type == null || type.isBlank()) throw new IllegalArgumentException("type");
        }

        @Override
        public String toString() {
            return "t=" + time + " " + type + " #" + sequence;
        }
    }

    // 時間相同時用 sequence 當 tie-breaker，模擬結果才可重現
    private static final Comparator<Event> ORDER = Comparator
            .comparingInt(Event::time)
            .thenComparingInt(Event::sequence);

    private final PriorityQueue<Event> pending = new PriorityQueue<>(ORDER);
    private final List<String> log = new ArrayList<>();
    private int sequenceCounter = 0;

    public Event schedule(int time, String type) {
        Event event = new Event(time, type, ++sequenceCounter);
        pending.offer(event);
        log.add("schedule " + event);
        return event;
    }

    // 取消要直接把事件從 queue 移除；
    // PriorityQueue.remove(Object) 用 equals 比對，record 已自動提供
    public boolean cancel(int sequence) {
        Event target = null;
        for (Event event : pending) {
            if (event.sequence() == sequence) {
                target = event;
                break;
            }
        }
        if (target == null) {
            log.add("cancel  #" + sequence + " -> not found");
            return false;
        }
        pending.remove(target);
        log.add("cancel  " + target);
        return true;
    }

    public int pendingCount() {
        return pending.size();
    }

    public void run() {
        log.add("--- run ---");
        int clock = 0;
        while (!pending.isEmpty()) {
            Event event = pending.poll();
            clock = event.time();
            log.add("run     " + event + " (clock=" + clock + ")");
        }
        log.add("--- done, clock=" + clock + " ---");
    }

    public void printLog() {
        for (String line : log) System.out.println(line);
    }

    public static void main(String[] args) {
        EventSimulationQueue simulator = new EventSimulationQueue();

        simulator.schedule(30, "gate-open");
        simulator.schedule(10, "setup");
        simulator.schedule(30, "speech");      // 與 gate-open 同時間，sequence 較大
        simulator.schedule(20, "check-in");
        simulator.schedule(50, "cleanup");
        simulator.schedule(40, "lucky-draw");

        System.out.println("pending=" + simulator.pendingCount());

        // 直接列印 PriorityQueue 只會看到內部 heap 陣列，不代表執行順序
        System.out.println("internal heap=" + simulator.pending);
        System.out.println();

        simulator.cancel(6);      // lucky-draw
        simulator.cancel(99);     // 不存在

        simulator.run();
        simulator.printLog();

        System.out.println();
        System.out.println("pending after run=" + simulator.pendingCount());
    }
}
