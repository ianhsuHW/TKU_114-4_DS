// 期末綜合練習二：服務請求系統
// 需求：HashMap 依 id 查詢 request，PriorityQueue 取下一筆，
//       取消時兩份結構必須一致。

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class ServiceRequestSystem {

    public record Request(String id, String title, int severity, int arrivalOrder) {
        public Request {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
            if (severity < 1 || severity > 5) throw new IllegalArgumentException("severity");
        }

        @Override
        public String toString() {
            return id + "[s" + severity + ",#" + arrivalOrder + "]" + title;
        }
    }

    // severity 越小越急；同 severity 依到達順序，最後用 id 當 tie-breaker
    private static final Comparator<Request> ORDER = Comparator
            .comparingInt(Request::severity)
            .thenComparingInt(Request::arrivalOrder)
            .thenComparing(Request::id);

    // 兩份索引各司其職：Map 負責 O(1) 依 id 查詢，Heap 負責 O(log n) 取下一筆。
    // 只要有一個操作忘了同步，系統就會出現查得到卻叫不到（或反過來）的資料。
    private final Map<String, Request> byId = new HashMap<>();
    private final PriorityQueue<Request> pending = new PriorityQueue<>(ORDER);
    private int arrivalCounter = 0;

    public Request submit(String id, String title, int severity) {
        if (id == null || id.isBlank() || byId.containsKey(id)) return null;
        Request request = new Request(id, title, severity, ++arrivalCounter);
        byId.put(id, request);
        pending.offer(request);
        return request;
    }

    public Request findById(String id) {
        return id == null ? null : byId.get(id);
    }

    public Request peekNext() {
        return pending.peek();
    }

    // 取出時兩份結構一起移除
    public Request handleNext() {
        Request next = pending.poll();
        if (next == null) return null;
        byId.remove(next.id());
        return next;
    }

    // 取消：Map 與 Heap 都要拿掉同一筆，否則會叫到已取消的 request
    public boolean cancel(String id) {
        Request request = findById(id);
        if (request == null) return false;
        byId.remove(id);
        pending.remove(request);
        return true;
    }

    public int pendingCount() {
        return pending.size();
    }

    public int indexedCount() {
        return byId.size();
    }

    // 一致性檢查：兩邊筆數相同，且 Heap 裡每一筆都還在 Map 中
    public boolean isConsistent() {
        if (byId.size() != pending.size()) return false;
        for (Request request : pending) {
            if (!request.equals(byId.get(request.id()))) return false;
        }
        return true;
    }

    public List<Request> pendingInOrder() {
        PriorityQueue<Request> copy = new PriorityQueue<>(ORDER);
        copy.addAll(pending);
        List<Request> result = new ArrayList<>();
        while (!copy.isEmpty()) result.add(copy.poll());
        return result;
    }

    private void printState(String label) {
        System.out.println("  " + label);
        System.out.println("    map=" + indexedCount() + " heap=" + pendingCount()
                + " consistent=" + isConsistent());
        System.out.println("    order=" + pendingInOrder());
    }

    public static void main(String[] args) {
        ServiceRequestSystem system = new ServiceRequestSystem();

        System.out.println("[submit]");
        system.submit("R-001", "印表機故障", 3);
        system.submit("R-002", "無法登入", 1);
        system.submit("R-003", "帳號權限", 2);
        system.submit("R-004", "系統當機", 1);
        system.submit("R-005", "換螢幕", 4);
        System.out.println("  duplicate id=" + system.submit("R-001", "重複", 2));
        System.out.println("  blank id=" + system.submit("  ", "空白", 2));
        system.printState("after submit");

        System.out.println();
        System.out.println("[lookup by id: HashMap]");
        System.out.println("  R-003 -> " + system.findById("R-003"));
        System.out.println("  R-999 -> " + system.findById("R-999"));
        System.out.println("  null  -> " + system.findById(null));

        System.out.println();
        System.out.println("[next by priority: Heap]");
        System.out.println("  peekNext=" + system.peekNext());

        System.out.println();
        System.out.println("[cancel keeps both structures in sync]");
        System.out.println("  cancel(R-004)=" + system.cancel("R-004"));
        System.out.println("  cancel(R-004) again=" + system.cancel("R-004"));
        System.out.println("  cancel(R-999)=" + system.cancel("R-999"));
        system.printState("after cancel");
        System.out.println("    findById(R-004)=" + system.findById("R-004")
                + " (Map 已移除)");
        System.out.println("    peekNext=" + system.peekNext()
                + " (Heap 也不會再叫到 R-004)");

        System.out.println();
        System.out.println("[handle all]");
        Request handled;
        while ((handled = system.handleNext()) != null) {
            System.out.println("  handle " + handled
                    + " remaining=" + system.pendingCount()
                    + " consistent=" + system.isConsistent());
        }

        System.out.println();
        System.out.println("[empty system]");
        System.out.println("  peekNext=" + system.peekNext());
        System.out.println("  handleNext=" + system.handleNext());
        System.out.println("  map=" + system.indexedCount()
                + " heap=" + system.pendingCount()
                + " consistent=" + system.isConsistent());
    }
}
