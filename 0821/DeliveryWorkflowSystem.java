import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

class Delivery {
    private String id;
    private String destination;
    private boolean completed;

    Delivery(String id, String destination) {
        this.id = id;
        this.destination = destination;
    }

    String getId() {
        return id;
    }

    boolean isCompleted() {
        return completed;
    }

    void complete() {
        completed = true;
    }

    void reopen() {
        completed = false;
    }

    @Override
    public String toString() {
        return id + " -> " + destination + (completed ? "（已完成）" : "（等待中）");
    }
}

class DeliveryWorkflow {
    private Map<String, Delivery> byId = new LinkedHashMap<>();
    private Deque<Delivery> waiting = new ArrayDeque<>();
    private Deque<Delivery> completedHistory = new ArrayDeque<>();

    boolean add(String id, String destination) {
        if (byId.containsKey(id)) {
            System.out.println("新增失敗，配送編號重複：" + id);
            return false;
        }
        Delivery delivery = new Delivery(id, destination);
        byId.put(id, delivery);
        waiting.offerLast(delivery);
        System.out.println("新增配送：" + delivery);
        return true;
    }

    Delivery process() {
        Delivery next = waiting.pollFirst();
        if (next == null) {
            System.out.println("處理失敗：沒有等待中的配送");
            return null;
        }
        next.complete();
        completedHistory.push(next);
        System.out.println("完成配送：" + next);
        return next;
    }

    Delivery undo() {
        Delivery last = completedHistory.pollFirst();
        if (last == null) {
            System.out.println("undo 失敗：沒有已完成的配送");
            return null;
        }
        last.reopen();
        waiting.offerFirst(last);
        System.out.println("復原配送：" + last);
        return last;
    }

    Delivery find(String id) {
        Delivery found = byId.get(id);
        System.out.println("查詢 " + id + "：" + (found == null ? "(查無資料)" : found));
        return found;
    }

    void printStatistics() {
        System.out.println("=== 統計 ===");
        System.out.println("總筆數：" + byId.size());
        System.out.println("等待中：" + waiting.size() + " " + waiting);
        System.out.println("已完成：" + completedHistory.size() + " " + completedHistory);
        System.out.println("最近完成：" + (completedHistory.isEmpty() ? "(無)" : completedHistory.peekFirst()));
    }
}

public class DeliveryWorkflowSystem {
    public static void main(String[] args) {
        DeliveryWorkflow workflow = new DeliveryWorkflow();

        System.out.println("=== 空工作流程 ===");
        workflow.process();
        workflow.undo();
        workflow.find("D001");

        System.out.println();
        System.out.println("=== 新增 ===");
        workflow.add("D001", "台北");
        workflow.add("D002", "新竹");
        workflow.add("D003", "台中");
        workflow.add("D002", "高雄");

        System.out.println();
        System.out.println("=== 處理與 undo ===");
        workflow.process();
        workflow.process();
        workflow.undo();
        workflow.process();

        System.out.println();
        System.out.println("=== 查詢 ===");
        workflow.find("D001");
        workflow.find("D999");

        System.out.println();
        workflow.printStatistics();
    }
}
