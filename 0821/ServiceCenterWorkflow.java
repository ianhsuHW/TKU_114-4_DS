import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

class ServiceTicket {
    private String id;
    private String topic;
    private String status;

    ServiceTicket(String id, String topic) {
        this.id = id;
        this.topic = topic;
        this.status = "WAITING";
    }

    String getId() {
        return id;
    }

    String getStatus() {
        return status;
    }

    void markCompleted() {
        status = "COMPLETED";
    }

    void markWaiting() {
        status = "WAITING";
    }

    void markCancelled() {
        status = "CANCELLED";
    }

    @Override
    public String toString() {
        return id + " " + topic + " [" + status + "]";
    }
}

class ServiceCenter {
    private Map<String, ServiceTicket> ticketsById = new LinkedHashMap<>();
    private Deque<ServiceTicket> waiting = new ArrayDeque<>();
    private Deque<ServiceTicket> completedHistory = new ArrayDeque<>();
    private Set<String> usedIds = new HashSet<>();

    boolean createTicket(String id, String topic) {
        if (id == null || id.isEmpty()) {
            System.out.println("建立失敗：ticket id 不可為空");
            return false;
        }
        if (!usedIds.add(id)) {
            System.out.println("建立失敗，ticket id 重複：" + id);
            return false;
        }
        ServiceTicket ticket = new ServiceTicket(id, topic);
        ticketsById.put(id, ticket);
        waiting.offerLast(ticket);
        System.out.println("建立 ticket：" + ticket + "，等待數 " + waiting.size());
        return true;
    }

    ServiceTicket processNext() {
        ServiceTicket next = waiting.pollFirst();
        if (next == null) {
            System.out.println("處理失敗：等待 Queue 已空");
            return null;
        }
        next.markCompleted();
        completedHistory.push(next);
        System.out.println("處理完成：" + next + "，等待數 " + waiting.size());
        return next;
    }

    boolean cancelWaiting(String id) {
        Iterator<ServiceTicket> it = waiting.iterator();
        while (it.hasNext()) {
            ServiceTicket ticket = it.next();
            if (ticket.getId().equals(id)) {
                it.remove();
                ticket.markCancelled();
                System.out.println("取消成功：" + ticket + "，等待數 " + waiting.size());
                return true;
            }
        }
        ServiceTicket known = ticketsById.get(id);
        if (known == null) {
            System.out.println("取消失敗，查無 ticket：" + id);
        } else {
            System.out.println("取消失敗，ticket 不在等待中：" + known);
        }
        return false;
    }

    ServiceTicket undoLastCompletion() {
        ServiceTicket last = completedHistory.pollFirst();
        if (last == null) {
            System.out.println("undo 失敗：沒有已完成的 ticket");
            return null;
        }
        last.markWaiting();
        waiting.offerFirst(last);
        System.out.println("undo 完成：" + last + " 放回等待前端，等待數 " + waiting.size());
        return last;
    }

    ServiceTicket findById(String id) {
        ServiceTicket found = ticketsById.get(id);
        System.out.println("查詢 " + id + "：" + (found == null ? "(查無資料)" : found));
        return found;
    }

    void printSummary() {
        System.out.println("=== 服務中心摘要 ===");
        System.out.println("已建立 ticket 數：" + ticketsById.size());
        System.out.println("等待中(" + waiting.size() + ")：" + waiting);
        System.out.println("完成歷程(" + completedHistory.size() + ")：" + completedHistory);
        System.out.println("下一個處理：" + (waiting.isEmpty() ? "(無)" : waiting.peekFirst()));
        System.out.println("全部 ticket 狀態：");
        for (ServiceTicket ticket : ticketsById.values()) {
            System.out.println("  " + ticket);
        }
    }
}

public class ServiceCenterWorkflow {
    public static void main(String[] args) {
        ServiceCenter center = new ServiceCenter();

        System.out.println("=== 空 Queue 測試 ===");
        center.processNext();
        center.undoLastCompletion();
        center.findById("S001");

        System.out.println();
        System.out.println("=== 建立 ticket 與重複 id ===");
        center.createTicket("S001", "帳號無法登入");
        center.createTicket("S002", "退款查詢");
        center.createTicket("S003", "設備維修");
        center.createTicket("S004", "資料更新");
        center.createTicket("S002", "重複編號");

        System.out.println();
        System.out.println("=== 取消 ===");
        center.cancelWaiting("S003");
        center.cancelWaiting("S999");

        System.out.println();
        System.out.println("=== 處理 ===");
        center.processNext();
        center.processNext();
        center.cancelWaiting("S001");

        System.out.println();
        System.out.println("=== 連續兩次 undo ===");
        center.undoLastCompletion();
        center.undoLastCompletion();
        center.undoLastCompletion();

        System.out.println();
        center.findById("S001");
        center.findById("S003");

        System.out.println();
        center.printSummary();
    }
}
