import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CollectionChoiceReport {

    static void printChoice(int no, String need, String type, String impl, String reason) {
        System.out.println("需求 " + no + "：" + need);
        System.out.println("  選擇 interface：" + type);
        System.out.println("  選擇 implementation：" + impl);
        System.out.println("  理由：" + reason);
    }

    static void searchHistory() {
        printChoice(1, "保留搜尋紀錄且允許重複", "List<String>", "ArrayList<>",
                "List 允許重複並保留插入順序，ArrayList 以 index 走訪最快。");

        List<String> history = new ArrayList<>();
        history.add("java list");
        history.add("java stack");
        history.add("java list");
        history.add("java queue");

        System.out.println("  全部紀錄：" + history);
        System.out.println("  筆數（含重複）：" + history.size());
        System.out.println("  最近一筆：" + history.get(history.size() - 1));
        System.out.println();
    }

    static void memberIds() {
        printChoice(2, "保存不重複會員編號", "Set<String>", "HashSet<>",
                "Set 自動排除重複，HashSet 的 contains 平均 O(1)。");

        Set<String> members = new HashSet<>();
        System.out.println("  加入 M001：" + members.add("M001"));
        System.out.println("  加入 M002：" + members.add("M002"));
        System.out.println("  重複加入 M001：" + members.add("M001"));
        System.out.println("  目前會員數：" + members.size());
        System.out.println("  是否包含 M002：" + members.contains("M002"));
        System.out.println();
    }

    static void scoreLookup() {
        printChoice(3, "以學號查詢成績", "Map<String, Integer>", "HashMap<>",
                "Map 表達 key 對 value 的對應，HashMap 依 key 查詢平均 O(1)。");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("S001", 90);
        scores.put("S002", 78);
        scores.put("S003", 85);

        System.out.println("  查詢 S002：" + scores.get("S002"));
        System.out.println("  查詢 S999：" + scores.getOrDefault("S999", -1));
        System.out.println("  平均：" + average(scores));
        System.out.println();
    }

    static int average(Map<String, Integer> scores) {
        if (scores.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (int v : scores.values()) {
            total += v;
        }
        return total / scores.size();
    }

    static void printJobs() {
        printChoice(4, "依到達順序處理列印工作", "Queue<String>", "ArrayDeque<>",
                "Queue 表達 FIFO，ArrayDeque 的頭尾操作是 O(1) 且不允許 null。");

        Queue<String> jobs = new ArrayDeque<>();
        jobs.offer("report.pdf");
        jobs.offer("slides.pptx");
        jobs.offer("notes.txt");

        System.out.println("  等待列印：" + jobs);
        System.out.println("  下一個：" + jobs.peek());
        while (!jobs.isEmpty()) {
            System.out.println("  列印中：" + jobs.poll());
        }
        System.out.println("  是否清空：" + jobs.isEmpty());
        System.out.println();
    }

    static void undoActions() {
        printChoice(5, "復原最近操作", "Deque<String>", "ArrayDeque<>",
                "Deque 當作 stack 使用符合 LIFO，官方建議以 Deque 取代舊的 Stack class。");

        Deque<String> undo = new ArrayDeque<>();
        undo.push("新增欄位");
        undo.push("修改標題");
        undo.push("刪除段落");

        System.out.println("  操作歷程：" + undo);
        System.out.println("  復原：" + undo.pop());
        System.out.println("  復原：" + undo.pop());
        System.out.println("  剩餘：" + undo);
        System.out.println("  再復原：" + undo.pop());
        System.out.println("  空 stack 復原：" + undo.poll());
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("=== 集合選擇報告 ===");
        System.out.println();
        searchHistory();
        memberIds();
        scoreLookup();
        printJobs();
        undoActions();

        System.out.println("=== 總結 ===");
        System.out.println("允許重複且看順序用 List，排除重複用 Set，key-value 對應用 Map。");
        System.out.println("先到先服務用 Queue，最近優先復原用 Deque 當 stack。");
        System.out.println("宣告時使用 interface 型別，之後替換 implementation 不必修改其他程式碼。");
    }
}
