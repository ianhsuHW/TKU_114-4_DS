# 8/31 教材：Heap、PriorityQueue 與 Hash Table 基礎

## 單元名稱

以優先順序與雜湊索引管理大量資料

## 課程定位

前面的 Stack、Queue 與 Binary Search Tree 都依照特定規則安排資料。本單元繼續處理兩種常見需求：第一種是反覆取出目前最重要的資料，第二種是依照 key 快速定位資料。Heap 適合支援 Priority Queue；Hash Table 則透過 hash function 將 key 對應到 bucket。

## 學習目標

完成本單元後，應能：

1. 說明 Complete Binary Tree 為何能用陣列儲存。
2. 根據 index 計算 parent、left child 與 right child。
3. 實作 Min Heap 的 insert、remove 與 heapify。
4. 使用 heap invariant 判斷結構是否正確。
5. 使用 Java `PriorityQueue` 與 `Comparator` 建立自訂優先順序。
6. 使用 Heap 解決 Top-K 與工作排程問題。
7. 說明 hash function、bucket 與 collision 的關係。
8. 使用 separate chaining 儲存發生 collision 的資料。

## 先備知識

- Java class、constructor、method 與 encapsulation。
- Array、`ArrayList`、generic type 與 `Comparator`。
- Binary Tree 的 parent、left child、right child。
- 建立資料夾 `0831`，所有程式分別存成指定檔名。

## 問題情境

系統同時收到大量工作時，不能每次都掃描整個 List 才找出最高優先工作；如果需要依學號、訂單編號或帳號查詢資料，也不應每次從第一筆搜尋到最後一筆。Heap 與 Hash Table 分別處理這兩種效能問題。

## 核心概念

### 概念 1：Complete Binary Tree 與陣列索引

#### 概念說明

Heap 的形狀必須是 Complete Binary Tree。除了最後一層外，每一層都填滿；最後一層由左至右填入。這項限制讓節點之間不需要真正的 `left`、`right` reference，只要知道節點在陣列中的 index，就能計算親子位置。

對 index `i`：

- parent：`(i - 1) / 2`
- left child：`2 * i + 1`
- right child：`2 * i + 2`

#### 實際應用

Job scheduler、事件模擬、網路封包排程與 Top-K 問題都需要頻繁比較優先順序。陣列表示能減少物件 reference，並具有良好的記憶體區域性。

#### 資料變化

陣列 `[10, 20, 30, 40, 50, 60]` 可解讀為：10 的 children 是 20、30；20 的 children 是 40、50；30 的 left child 是 60。

#### 設計判斷

只有形狀完整的 Binary Tree 才適合直接使用此索引公式。一般 Binary Tree 可能缺少中間節點，不能任意壓進連續陣列後仍保留原本結構。

#### 範例程式

<!-- DEMO_START: HeapIndexExplorer.java | Complete Binary Tree 的陣列索引 -->
檔名：`HeapIndexExplorer.java`

```java
import java.util.List;

public class HeapIndexExplorer {
    static int parentIndex(int index) {
        return index <= 0 ? -1 : (index - 1) / 2;
    }

    static int leftIndex(int index) {
        return index * 2 + 1;
    }

    static int rightIndex(int index) {
        return index * 2 + 2;
    }

    static String valueAt(List<Integer> heap, int index) {
        return index >= 0 && index < heap.size()
                ? String.valueOf(heap.get(index)) : "none";
    }

    public static void main(String[] args) {
        List<Integer> heap = List.of(10, 20, 30, 40, 50, 60);

        for (int i = 0; i < heap.size(); i++) {
            int parent = parentIndex(i);
            int left = leftIndex(i);
            int right = rightIndex(i);
            System.out.printf(
                    "index=%d value=%d parent=%s left=%s right=%s%n",
                    i, heap.get(i), valueAt(heap, parent),
                    valueAt(heap, left), valueAt(heap, right));
        }
    }
}
```

```bash
javac HeapIndexExplorer.java
java HeapIndexExplorer
```

預期輸出包含：

```text
index=0 value=10 parent=none left=20 right=30
index=1 value=20 parent=10 left=40 right=50
index=2 value=30 parent=10 left=60 right=none
```
<!-- DEMO_END -->

#### 執行重點

觀察陣列 index 與樹狀關係，不要把節點值誤認為 index。

### 概念 2：Min Heap Insert 與 Bubble-Up

#### 概念說明

Min Heap 必須同時維持形狀與順序：新值先加入陣列尾端以維持 Complete Binary Tree，再與 parent 比較。只要 child 小於 parent 就交換，直到到達 root 或 parent 已不大於 child。這個過程稱為 bubble-up、sift-up 或 percolate-up。

#### 實際應用

當系統持續加入工作，並且需要快速得知最小截止時間、最低成本或最早事件時，可以使用 Min Heap。

#### 資料變化

將 15 加入 `[10, 30, 20, 50, 40]`：先變成 `[10,30,20,50,40,15]`，15 與 parent 20 交換，得到 `[10,30,15,50,40,20]`。

#### 設計判斷

Insert 不需要重新排序整個陣列。Heap 只要求 parent-child 關係，不要求 inorder 或完整排序。

#### 範例程式

<!-- DEMO_START: MinHeapInsertDemo.java | Min Heap insert 與 bubble-up -->
檔名：`MinHeapInsertDemo.java`

```java
import java.util.ArrayList;
import java.util.List;

public class MinHeapInsertDemo {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        System.out.println("append " + value + " -> " + data);

        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            System.out.println("swap  " + parent + "," + index + " -> " + data);
            index = parent;
        }
    }

    public Integer peek() {
        return data.isEmpty() ? null : data.get(0);
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public static void main(String[] args) {
        MinHeapInsertDemo heap = new MinHeapInsertDemo();
        for (int value : new int[]{30, 10, 20, 50, 40, 15}) {
            heap.add(value);
        }
        System.out.println("heap=" + heap.snapshot());
        System.out.println("min=" + heap.peek());
    }
}
```

```bash
javac MinHeapInsertDemo.java
java MinHeapInsertDemo
```

預期主要結果：

```text
heap=[10, 30, 15, 50, 40, 20]
min=10
```
<!-- DEMO_END -->

#### 執行重點

每次交換後，index 必須更新成 parent，否則只會完成一層修正。

### 概念 3：Remove Root 與 Bubble-Down

#### 概念說明

Min Heap 移除的是 root。直接刪除 index 0 會造成陣列中間出現空洞，因此先保存 root，把最後一個值移到 index 0，再移除陣列尾端。新的 root 可能破壞 invariant，必須反覆與較小的 child 交換。

#### 實際應用

事件模擬每次取出最早事件，工作排程每次取出最高優先工作，都會反覆執行 remove root。

#### 資料變化

`[10,30,15,50,40,20]` 移除 10：20 移到 root 成為 `[20,30,15,50,40]`，再與較小 child 15 交換，得到 `[15,30,20,50,40]`。

#### 設計判斷

有兩個 child 時必須選較小 child。只與 left child 比較可能在 right child 更小時留下錯誤 Heap。

#### 範例程式

<!-- DEMO_START: MinHeapRemoveDemo.java | Min Heap remove 與 bubble-down -->
檔名：`MinHeapRemoveDemo.java`

```java
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class MinHeapRemoveDemo {
    private final List<Integer> data = new ArrayList<>();

    public void add(int value) {
        data.add(value);
        int index = data.size() - 1;
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (data.get(parent) <= data.get(index)) break;
            swap(parent, index);
            index = parent;
        }
    }

    public int removeMin() {
        if (data.isEmpty()) throw new NoSuchElementException("heap is empty");
        int result = data.get(0);
        int last = data.remove(data.size() - 1);
        if (!data.isEmpty()) {
            data.set(0, last);
            bubbleDown(0);
        }
        return result;
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            if (left >= data.size()) return;

            int smaller = left;
            if (right < data.size() && data.get(right) < data.get(left)) {
                smaller = right;
            }
            if (data.get(index) <= data.get(smaller)) return;
            swap(index, smaller);
            index = smaller;
        }
    }

    private void swap(int first, int second) {
        int temp = data.get(first);
        data.set(first, data.get(second));
        data.set(second, temp);
    }

    public List<Integer> snapshot() {
        return List.copyOf(data);
    }

    public static void main(String[] args) {
        MinHeapRemoveDemo heap = new MinHeapRemoveDemo();
        for (int value : new int[]{30, 10, 20, 50, 40, 15}) heap.add(value);

        while (!heap.data.isEmpty()) {
            System.out.println("remove=" + heap.removeMin()
                    + " remaining=" + heap.snapshot());
        }
    }
}
```

```bash
javac MinHeapRemoveDemo.java
java MinHeapRemoveDemo
```

預期移除順序：

```text
10, 15, 20, 30, 40, 50
```
<!-- DEMO_END -->

#### 執行重點

測試 zero、one 與 two-child 情況；單一元素移除後不能再設定 index 0。

### 概念 4：Bottom-Up Heapify 與 Invariant Audit

#### 概念說明

已存在一批未排序資料時，可以逐筆 insert，也可以直接複製陣列後，由最後一個 non-leaf node 往 root 執行 bubble-down。最後一個 non-leaf index 是 `size / 2 - 1`。Bottom-up heapify 的整體時間複雜度為 O(n)。

#### 實際應用

批次載入工作、從檔案建立事件佇列或將查詢結果轉成 Heap 時，heapify 比逐筆重排更適合。

#### 資料變化

`[45,12,30,8,20,18]` 從 index 2、1、0 依序向下修正，最後得到 `[8,12,18,45,20,30]`。

#### 設計判斷

Invariant audit 應檢查每一個 parent 是否不大於已存在的 child，而不是只檢查 root。

#### 範例程式

<!-- DEMO_START: HeapifyAudit.java | Bottom-up heapify 與 invariant 驗證 -->
檔名：`HeapifyAudit.java`

```java
import java.util.ArrayList;
import java.util.List;

public class HeapifyAudit {
    static List<Integer> heapify(List<Integer> source) {
        List<Integer> heap = new ArrayList<>(source);
        for (int i = heap.size() / 2 - 1; i >= 0; i--) {
            bubbleDown(heap, i);
            System.out.println("after index " + i + " -> " + heap);
        }
        return heap;
    }

    static boolean isMinHeap(List<Integer> heap) {
        for (int parent = 0; parent < heap.size(); parent++) {
            int left = parent * 2 + 1;
            int right = parent * 2 + 2;
            if (left < heap.size() && heap.get(parent) > heap.get(left)) return false;
            if (right < heap.size() && heap.get(parent) > heap.get(right)) return false;
        }
        return true;
    }

    private static void bubbleDown(List<Integer> heap, int index) {
        while (true) {
            int left = index * 2 + 1;
            int right = index * 2 + 2;
            int smallest = index;
            if (left < heap.size() && heap.get(left) < heap.get(smallest)) smallest = left;
            if (right < heap.size() && heap.get(right) < heap.get(smallest)) smallest = right;
            if (smallest == index) return;
            int temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);
            index = smallest;
        }
    }

    public static void main(String[] args) {
        List<Integer> source = List.of(45, 12, 30, 8, 20, 18);
        List<Integer> heap = heapify(source);
        System.out.println("result=" + heap);
        System.out.println("valid=" + isMinHeap(heap));
        System.out.println("invalid=" + isMinHeap(List.of(10, 5, 20)));
    }
}
```

```bash
javac HeapifyAudit.java
java HeapifyAudit
```

預期主要輸出：

```text
result=[8, 12, 18, 45, 20, 30]
valid=true
invalid=false
```
<!-- DEMO_END -->

#### 執行重點

Heap 的陣列內容不一定唯一；不同但符合 invariant 的陣列都可能是正確答案。

### 概念 5：Java PriorityQueue 與資料取出順序

#### 概念說明

Java `PriorityQueue` 預設是 Min Priority Queue。`peek()` 查看但不移除 head；`poll()` 取出並移除 head；`offer()` 加入元素。直接列印 PriorityQueue 只能看到內部 Heap 陣列，不能當成完整排序結果。

#### 實際應用

適合儲存下一個要處理的 deadline、最小成本節點、最低價格或事件時間。

#### 資料變化

加入 40、10、30、20 後，`peek()` 是 10。依序 `poll()` 才會得到 10、20、30、40。

#### 設計判斷

如果需求是保留完整排序並支援任意位置查找，PriorityQueue 不是替代 `TreeSet` 或排序 List 的工具。

#### 範例程式

<!-- DEMO_START: PriorityNumberQueue.java | PriorityQueue 的 offer、peek 與 poll -->
檔名：`PriorityNumberQueue.java`

```java
import java.util.PriorityQueue;

public class PriorityNumberQueue {
    public static void main(String[] args) {
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        for (int value : new int[]{40, 10, 30, 20}) {
            queue.offer(value);
            System.out.println("offer=" + value + " head=" + queue.peek());
        }

        System.out.print("poll order=");
        while (!queue.isEmpty()) {
            System.out.print(queue.poll());
            if (!queue.isEmpty()) System.out.print(",");
        }
        System.out.println();
        System.out.println("empty poll=" + queue.poll());
    }
}
```

```bash
javac PriorityNumberQueue.java
java PriorityNumberQueue
```

預期主要輸出：

```text
poll order=10,20,30,40
empty poll=null
```
<!-- DEMO_END -->

#### 執行重點

空 Queue 的 `poll()` 回傳 `null`，`remove()` 則會丟出 exception。

### 概念 6：Comparator 與多欄位優先順序

#### 概念說明

物件沒有自然順序時，建立 PriorityQueue 必須提供 `Comparator`。多欄位規則要明確定義 tie-breaker，例如先比較 priority，再比較建立順序，確保相同 priority 時仍有可預測結果。

#### 實際應用

客服工單、急診分級、CPU 工作、訂單出貨與排隊候補，都會同時比較嚴重度與等待順序。

#### 資料變化

Priority 越小越優先；priority 相同時 sequence 越小越早處理。

#### 設計判斷

Comparator 必須符合一致性。比較結果若會隨外部狀態改變，已放入 Queue 的元素可能無法維持正確位置。

#### 範例程式

<!-- DEMO_START: TaskSchedulerQueue.java | Comparator 多欄位工作排程 -->
檔名：`TaskSchedulerQueue.java`

```java
import java.util.Comparator;
import java.util.PriorityQueue;

public class TaskSchedulerQueue {
    record Task(String id, int priority, long sequence) {
        Task {
            if (id == null || id.isBlank()) throw new IllegalArgumentException("id");
        }
    }

    public static void main(String[] args) {
        Comparator<Task> order = Comparator
                .comparingInt(Task::priority)
                .thenComparingLong(Task::sequence)
                .thenComparing(Task::id);

        PriorityQueue<Task> tasks = new PriorityQueue<>(order);
        tasks.offer(new Task("normal-1", 3, 1));
        tasks.offer(new Task("urgent-2", 1, 4));
        tasks.offer(new Task("urgent-1", 1, 2));
        tasks.offer(new Task("medium-1", 2, 3));

        while (!tasks.isEmpty()) {
            Task task = tasks.poll();
            System.out.println(task.id() + "|" + task.priority()
                    + "|" + task.sequence());
        }
    }
}
```

```bash
javac TaskSchedulerQueue.java
java TaskSchedulerQueue
```

預期輸出：

```text
urgent-1|1|2
urgent-2|1|4
medium-1|2|3
normal-1|3|1
```
<!-- DEMO_END -->

#### 執行重點

讀 Comparator 時要逐層判斷：前一欄相同時才比較下一欄。

### 概念 7：Top-K 與固定大小 Heap

#### 概念說明

若只需要最大的 K 筆，不必排序全部資料。維持大小最多為 K 的 Min Heap：每次加入數值，超過 K 就移除目前最小值。最後 Heap 中保留最大的 K 筆。

#### 實際應用

排行榜、熱門商品、最高交易、推薦系統候選與監控異常值，都可能只需要 Top-K。

#### 資料變化

K=3，依序讀取 70、90、60、85、100。Heap 最後保留 85、90、100。

#### 設計判斷

資料量很小或必須完整排序時，直接 sort 更簡單。資料流很大且 K 遠小於 n 時，固定大小 Heap 才能顯著減少空間與排序成本。

#### 範例程式

<!-- DEMO_START: TopKScoreTracker.java | 固定大小 Min Heap 保存 Top-K -->
檔名：`TopKScoreTracker.java`

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TopKScoreTracker {
    static List<Integer> topK(List<Integer> scores, int k) {
        if (scores == null || k <= 0) return List.of();
        PriorityQueue<Integer> top = new PriorityQueue<>();

        for (Integer score : scores) {
            if (score == null) continue;
            top.offer(score);
            if (top.size() > k) top.poll();
            System.out.println("score=" + score + " heap=" + top);
        }

        List<Integer> result = new ArrayList<>(top);
        result.sort(Comparator.reverseOrder());
        return result;
    }

    public static void main(String[] args) {
        List<Integer> scores = List.of(70, 90, 60, 85, 100, 75);
        System.out.println("top3=" + topK(scores, 3));
        System.out.println("top0=" + topK(scores, 0));
        System.out.println("null=" + topK(null, 3));
    }
}
```

```bash
javac TopKScoreTracker.java
java TopKScoreTracker
```

預期主要輸出：

```text
top3=[100, 90, 85]
top0=[]
null=[]
```
<!-- DEMO_END -->

#### 執行重點

Heap 保存候選集合，最後另外排序只是為了顯示，不是 Top-K 篩選的必要步驟。

### 概念 8：Hash Function、Collision 與 Separate Chaining

#### 概念說明

Hash Table 使用 hash function 把 key 轉成 bucket index。不同 key 可能得到相同 index，稱為 collision。Separate chaining 在每個 bucket 放一個 List，讓多個 entry 共存。查詢時先定位 bucket，再在該 bucket 內比較真正的 key。

#### 實際應用

帳號索引、session cache、商品代碼查詢、字詞統計及 duplicate detection 都依賴雜湊結構。

#### 資料變化

Bucket 數量為 5 時，key 12、7、22 都可能落在 index 2。它們必須保留在同一 chain，不能讓後加入資料直接覆蓋前一筆。

#### 設計判斷

Hash value 不是 array index。必須先使用 `Math.floorMod(hash, bucketCount)`，避免負 hash 造成負 index。Collision 是正常情況，不代表 hash function 失敗。

#### 範例程式

<!-- DEMO_START: ChainedHashTableDemo.java | Separate chaining 處理 collision -->
檔名：`ChainedHashTableDemo.java`

```java
import java.util.ArrayList;
import java.util.List;

public class ChainedHashTableDemo {
    private record Entry(int key, String value) {}

    private final List<List<Entry>> buckets;

    public ChainedHashTableDemo(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    private int index(int key) {
        return Math.floorMod(Integer.hashCode(key), buckets.size());
    }

    public void put(int key, String value) {
        List<Entry> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key() == key) {
                chain.set(i, new Entry(key, value));
                return;
            }
        }
        chain.add(new Entry(key, value));
    }

    public String get(int key) {
        for (Entry entry : buckets.get(index(key))) {
            if (entry.key() == key) return entry.value();
        }
        return null;
    }

    public boolean remove(int key) {
        return buckets.get(index(key)).removeIf(entry -> entry.key() == key);
    }

    public void printBuckets() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println(i + " -> " + buckets.get(i));
        }
    }

    public static void main(String[] args) {
        ChainedHashTableDemo table = new ChainedHashTableDemo(5);
        table.put(12, "A");
        table.put(7, "B");
        table.put(22, "C");
        table.put(-3, "D");
        table.put(7, "B2");
        table.printBuckets();
        System.out.println("get7=" + table.get(7));
        System.out.println("remove12=" + table.remove(12));
        System.out.println("missing=" + table.get(99));
    }
}
```

```bash
javac ChainedHashTableDemo.java
java ChainedHashTableDemo
```

預期主要結果：

```text
get7=B2
remove12=true
missing=null
```
<!-- DEMO_END -->

#### 執行重點

`put()` 遇到相同 key 應更新 value；不同 key 即使 collision 也必須同時保留。

## 程式執行追蹤

### 追蹤一：Bubble-Up

| 動作 | Heap |
|---|---|
| 原始 | `[10,30,20,50,40]` |
| append 15 | `[10,30,20,50,40,15]` |
| 15 與 20 交換 | `[10,30,15,50,40,20]` |

### 追蹤二：Hash Collision

| Key | Bucket Count | Bucket Index | 結果 |
|---:|---:|---:|---|
| 12 | 5 | 2 | chain 加入 `12=A` |
| 7 | 5 | 2 | 同一 chain 加入 `7=B` |
| 22 | 5 | 2 | 同一 chain 加入 `22=C` |
| 7 | 5 | 2 | 更新 `7=B2` |

## 除錯練習

### 除錯練習一：Bubble-Down 選錯 Child

如果只與 left child 比較，資料 `[20,30,15]` 不會交換，但 right child 15 已違反 Min Heap。修正方式是先選出較小 child，再決定是否交換。

### 除錯練習二：負數 Hash Index

`hash % bucketCount` 可能是負數。應使用 `Math.floorMod(hash, bucketCount)`，不能直接拿負數存取 List。

### 除錯練習三：直接列印 PriorityQueue

PriorityQueue 的 iterator 與 `toString()` 不保證完整排序。需要驗證取出順序時，應複製 Queue 後反覆 `poll()`。

## 課堂實作題

### 課堂實作題一：Max Heap Insert Trace

指定檔名：`MaxHeapInsertTrace.java`。

建立自行實作 Max Heap，提供 `add(int)`、`peekMax()`、`snapshot()`。加入每筆資料後印出陣列，重複值允許存在。測試 `{25,40,10,50,30,50}`，完成時 root 必須是 50。

### 課堂實作題二：Min Heap 完整操作

指定檔名：`IntegerMinHeap.java`。

實作 `add()`、`peek()`、`removeMin()`、`size()`、`isEmpty()`。空 Heap 的 `peek()` 與 `removeMin()` 必須丟出 `NoSuchElementException`，並驗證移除結果為非遞減順序。

### 課堂實作題三：工作排程

指定檔名：`SupportTicketQueue.java`。

建立 `Ticket(id, severity, createdOrder)`。severity 數字越大越優先；severity 相同時 createdOrder 越小越早。依序取出全部 ticket 並輸出 `id|severity|createdOrder`。

### 課堂實作題四：Top-K 最低價格

指定檔名：`LowestKPriceTracker.java`。

使用固定大小 Max Heap 保留最低 K 個有效價格。忽略 `null` 與負數；K 小於等於 0 時回傳 empty List；結果依價格遞增排列。

### 課堂實作題五：Heap Validator

指定檔名：`HeapPropertyValidator.java`。

提供 `isMinHeap(List<Integer>)` 與 `isMaxHeap(List<Integer>)`。`null` 回傳 false，empty List 與單一元素回傳 true。不能用排序後比較取代 parent-child 檢查。

### 課堂實作題六：Collision Bucket Report

指定檔名：`CollisionBucketReport.java`。

將整數 key 放入固定數量 bucket，輸出每個 bucket 的 key 清單、collision 數量與最長 chain。必須正確處理負 key、重複 key 與空輸入。

## 課後作業

### 課後作業一：急診候診佇列

指定檔名：`EmergencyTriageQueue.java`。

依危急程度、到院順序與病歷號建立穩定的 Priority Queue，支援報到、查看下一位、叫號與查詢目前人數。輸出每次叫號結果及空佇列處理。

### 課後作業二：活動事件模擬器

指定檔名：`EventSimulationQueue.java`。

事件包含時間、類型與 sequence。依時間先後執行；時間相同依 sequence。支援取消指定事件，並輸出完整執行紀錄。

### 課後作業三：可調整容量 Min Heap

指定檔名：`ArrayMinHeap.java`。

使用自行管理的 `int[]`，不可使用 `PriorityQueue`。容量不足時擴充為兩倍，支援 add、remove、peek 與 snapshot，測試至少 20 筆資料。

### 課後作業四：Top-K 熱門商品

指定檔名：`TopSellingProducts.java`。

商品包含 id 與 sales。保留銷量最高 K 筆；銷量相同時 id 字典序較小者優先。輸入含重複商品 id 時先合併銷量。

### 課後作業五：整數 Hash Table

指定檔名：`IntegerStringHashTable.java`。

使用 separate chaining 實作 `put(int,String)`、`get(int)`、`containsKey(int)`、`remove(int)`、`size()` 與 `bucketReport()`。相同 key 必須更新，size 不增加。

### 課後作業六：學號 Collision 分析

指定檔名：`StudentIdHashAnalysis.java`。

輸入一組學號與 bucket count，統計每個 bucket 筆數、總 collision 次數、最大 chain 與平均 chain 長度，並比較兩種 bucket count 的結果。

## 常見錯誤與診斷

| 症狀 | 可能原因 | 診斷方式 |
|---|---|---|
| Bubble-up 只交換一次 | 未更新 index | 使用需要跨兩層交換的資料 |
| Remove 後 root 正確但下層錯誤 | 沒有持續 bubble-down | 每次 remove 後執行 invariant audit |
| 重複值造成無限迴圈 | 使用不一致的 `<`、`<=` | 確認停止與交換條件互補 |
| PriorityQueue 同分順序不固定 | 缺少 tie-breaker | Comparator 加入 sequence |
| Hash Table 遺失 collision 資料 | 每個 bucket 只存一筆 | 使用 List 或 linked chain |
| 負 key 造成 index error | `%` 得到負餘數 | 改用 `Math.floorMod` |

## 形成性評量

1. 陣列 index 7 的 parent、left 與 right index 分別是多少？
2. 為什麼 Heap 不等於完整排序陣列？
3. Bubble-down 有兩個 child 時，Min Heap 應選哪一個？
4. 直接列印 PriorityQueue 能否證明 poll 順序？說明原因。
5. Top-K 最大值為何使用大小為 K 的 Min Heap？
6. Collision 是否表示 Hash Table 不能使用該 key？
7. `put()` 遇到相同 key 與不同 collision key 時應有何差異？

## 評分規準

| 面向 | 完整 | 部分完成 | 未完成 |
|---|---|---|---|
| Heap invariant | insert、remove 後皆正確 | 只有部分資料正確 | 無法維持 parent-child 關係 |
| 邊界條件 | empty、single、duplicate 都處理 | 缺少一至兩種 | 主要操作發生例外 |
| Priority 規則 | 主排序與 tie-breaker 完整 | 只有主排序 | 取出順序錯誤 |
| Hash collision | 相同 bucket 可保留多 key | 可新增但更新或刪除錯誤 | collision 資料被覆蓋 |
| 程式完整性 | 指定檔名且可獨立編譯 | 少量錯誤 | 缺少主要 class 或 method |

## 參考教材

- Java Platform API：`PriorityQueue`、`Comparator`、`List`。
- Open Data Structures：Binary Heap 與 Hash Table 章節。
- VisuAlgo：Heap 與 Hash Table 操作視覺化。

