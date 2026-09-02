# 9/1 教材：HashMap、HashSet 與 Graph 表示法

## 單元名稱

以 Key 建立索引，並將關係資料表示成 Graph

## 課程定位

8/31 已使用 bucket 與 separate chaining 處理 collision。本單元先完成 Hash Table 的查詢、更新、刪除與 key 設計，再進入 Graph。Graph 不只保存物件，也保存物件之間的 edge；因此必須依資料密度與操作需求選擇 adjacency matrix 或 adjacency list。

## 學習目標

完成本單元後，應能：

1. 完成 chained Hash Table 的 `put`、`get`、`remove` 與 `size`。
2. 說明 load factor 與 rehash 的目的。
3. 正確設計自訂 key 的 `equals()` 與 `hashCode()`。
4. 使用 `HashMap` 完成分組、統計與直接查找。
5. 使用 `HashSet` 完成去除重複與 membership test。
6. 區分 directed、undirected、weighted 與 unweighted Graph。
7. 實作 adjacency matrix 與 adjacency list。
8. 比較兩種 Graph 表示法的空間與操作成本。

## 先備知識

- 8/31 的 hash function、bucket、collision 與 chaining。
- `List`、`Map`、`Set`、generic type 與 object equality。
- 建立資料夾 `0901`，每個範例存成獨立 `.java` 檔案。

## 問題情境

學籍系統需要依學號找到資料，也要表示選課、好友、道路或系統服務之間的關係。第一種需求適合 key-value index；第二種需求不能只靠單一 key，必須使用 vertex 與 edge 建立 Graph。

## 核心概念

### 概念 1：完整 Chained Hash Table

#### 概念說明

Hash Table 的 `put` 先定位 bucket，再判斷 chain 中是否已存在相同 key。存在時更新 value，不存在才新增 entry 並增加 size。`get` 與 `remove` 也必須同時比較 bucket 與真正的 key，不能把相同 bucket 當成相同 key。

#### 實際應用

適合商品編號、學號、帳號、cache key 與設定名稱等直接查找需求。

#### 資料變化

`put(12,A)`、`put(7,B)`、`put(12,A2)` 的最終 size 是 2，因為第三次操作更新既有 key。

#### 設計判斷

查不到 key 時要先決定 API contract：回傳 `null`、`Optional` 或丟出 exception。不同 method 不應混用不一致規則。

#### 範例程式

<!-- DEMO_START: SimpleHashTable.java | Chained Hash Table 完整操作 -->
檔名：`SimpleHashTable.java`

```java
import java.util.ArrayList;
import java.util.List;

public class SimpleHashTable<K, V> {
    private record Entry<K, V>(K key, V value) {}

    private final List<List<Entry<K, V>>> buckets;
    private int size;

    public SimpleHashTable(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        buckets = new ArrayList<>();
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    private int index(K key) {
        if (key == null) throw new IllegalArgumentException("key");
        return Math.floorMod(key.hashCode(), buckets.size());
    }

    public void put(K key, V value) {
        List<Entry<K, V>> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                chain.set(i, new Entry<>(key, value));
                return;
            }
        }
        chain.add(new Entry<>(key, value));
        size++;
    }

    public V get(K key) {
        for (Entry<K, V> entry : buckets.get(index(key))) {
            if (entry.key().equals(key)) return entry.value();
        }
        return null;
    }

    public boolean remove(K key) {
        List<Entry<K, V>> chain = buckets.get(index(key));
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).key().equals(key)) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    public void printBuckets() {
        for (int i = 0; i < buckets.size(); i++) {
            System.out.println(i + " -> " + buckets.get(i));
        }
    }

    public static void main(String[] args) {
        SimpleHashTable<Integer, String> table = new SimpleHashTable<>(5);
        table.put(12, "A");
        table.put(7, "B");
        table.put(22, "C");
        table.put(12, "A2");
        table.printBuckets();
        System.out.println("size=" + table.size());
        System.out.println("get12=" + table.get(12));
        System.out.println("remove7=" + table.remove(7));
        System.out.printf("load=%.2f%n", table.loadFactor());
    }
}
```

```bash
javac SimpleHashTable.java
java SimpleHashTable
```

預期主要輸出：

```text
size=3
get12=A2
remove7=true
load=0.40
```
<!-- DEMO_END -->

#### 執行重點

更新既有 key 不增加 size；成功 remove 才減少 size。

### 概念 2：Load Factor、Rehash 與 Key Equality

#### 概念說明

Load factor 是 `size / bucketCount`。資料增加後，平均 chain 會變長，查詢成本也會增加。Rehash 不是直接複製舊 bucket，而是建立較大 bucket array，重新計算每個 entry 的 index。自訂 key 必須遵守：兩個物件若 `equals()` 為 true，`hashCode()` 必須相同。

#### 實際應用

複合 key 常見於課程代碼、訂單年度加流水號、學號加學期等索引。

#### 資料變化

Bucket count 從 5 改成 11 時，舊 index 不能沿用；每個 key 都要重新 `% 11`。

#### 設計判斷

作為 key 的欄位應在放入 Map 後保持不變。若 key 的 hashCode 改變，Map 可能無法在原 bucket 找回物件。

#### 範例程式

<!-- DEMO_START: StudentKeyMapDemo.java | equals 與 hashCode 的一致性 -->
檔名：`StudentKeyMapDemo.java`

```java
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StudentKeyMapDemo {
    record StudentKey(String department, String studentId) {
        StudentKey {
            department = normalize(department, "department");
            studentId = normalize(studentId, "studentId");
        }

        private static String normalize(String value, String field) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(field);
            }
            return value.trim().toUpperCase();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (!(other instanceof StudentKey key)) return false;
            return department.equals(key.department)
                    && studentId.equals(key.studentId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(department, studentId);
        }
    }

    public static void main(String[] args) {
        Map<StudentKey, String> names = new HashMap<>();
        names.put(new StudentKey(" im ", "412001"), "Amy");
        names.put(new StudentKey("CS", "412001"), "Ben");

        System.out.println(names.get(new StudentKey("IM", "412001")));
        System.out.println(names.containsKey(new StudentKey(" cs ", "412001")));
        System.out.println("size=" + names.size());
    }
}
```

```bash
javac StudentKeyMapDemo.java
java StudentKeyMapDemo
```

預期輸出：

```text
Amy
true
size=2
```
<!-- DEMO_END -->

#### 執行重點

Record 原本會自動產生 equality；範例覆寫的目的，是明確呈現 normalization 與一致的 equality 規則。

### 概念 3：HashMap 統計與 Grouping

#### 概念說明

`HashMap<K,V>` 適合由 key 累積 value。常用操作包括 `getOrDefault`、`putIfAbsent`、`computeIfAbsent` 與 `merge`。選擇 method 時，要能說明不存在 key 與已存在 key 的處理方式。

#### 實際應用

字詞次數、商品銷量、錯誤代碼統計、課程分組與使用者行為彙整都可使用 HashMap。

#### 資料變化

讀到 `java` 時，第一次建立 `java=1`，後續透過 `merge` 累加成 2、3。

#### 設計判斷

HashMap 不保證 key 的顯示順序。如果輸出需要排序，應把 entry 轉成 List 後明確排序。

#### 範例程式

<!-- DEMO_START: WordFrequencyMap.java | HashMap frequency table -->
檔名：`WordFrequencyMap.java`

```java
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyMap {
    static Map<String, Integer> countWords(List<String> words) {
        Map<String, Integer> counts = new HashMap<>();
        if (words == null) return counts;
        for (String word : words) {
            if (word == null || word.isBlank()) continue;
            counts.merge(word.trim().toLowerCase(), 1, Integer::sum);
        }
        return counts;
    }

    static List<String> sortedReport(Map<String, Integer> counts) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(counts.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey()));
        List<String> report = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : entries) {
            report.add(entry.getKey() + "=" + entry.getValue());
        }
        return report;
    }

    public static void main(String[] args) {
        List<String> words = List.of("Java", "heap", "JAVA", "graph", "heap", "java");
        Map<String, Integer> counts = countWords(words);
        System.out.println(counts);
        System.out.println(sortedReport(counts));
    }
}
```

```bash
javac WordFrequencyMap.java
java WordFrequencyMap
```

預期排序報告：

```text
[java=3, heap=2, graph=1]
```
<!-- DEMO_END -->

#### 執行重點

統計與輸出排序是兩個不同階段，不要依賴 HashMap 的 iterator 順序。

### 概念 4：HashSet 與 Membership Test

#### 概念說明

HashSet 只保存不重複元素，可視為只關心 key 的 HashMap。`add()` 回傳 false 表示元素已存在；`contains()` 適合快速判斷是否看過資料。

#### 實際應用

重複報名、唯一標籤、已處理 request、權限集合，以及後續 BFS／DFS 的 visited 都會使用 Set。

#### 資料變化

加入 `A01, B02, A01` 時，第二次 A01 不會增加 size，可直接列入 duplicate report。

#### 設計判斷

需要保留插入順序時改用 `LinkedHashSet`；需要排序時考慮 `TreeSet`。不要假設 HashSet 顯示順序固定。

#### 範例程式

<!-- DEMO_START: RegistrationSetDemo.java | HashSet 去除重複與 membership test -->
檔名：`RegistrationSetDemo.java`

```java
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RegistrationSetDemo {
    static List<String> duplicates(List<String> ids) {
        Set<String> accepted = new HashSet<>();
        Set<String> reported = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        if (ids == null) return duplicates;

        for (String id : ids) {
            if (id == null || id.isBlank()) continue;
            String normalized = id.trim().toUpperCase();
            if (!accepted.add(normalized) && reported.add(normalized)) {
                duplicates.add(normalized);
            }
        }
        return duplicates;
    }

    public static void main(String[] args) {
        List<String> ids = List.of("A01", "B02", " a01 ", "C03", "B02", "B02");
        System.out.println("duplicates=" + duplicates(ids));
    }
}
```

```bash
javac RegistrationSetDemo.java
java RegistrationSetDemo
```

預期輸出：

```text
duplicates=[A01, B02]
```
<!-- DEMO_END -->

#### 執行重點

使用第二個 Set 避免同一重複值在報告中出現多次。

### 概念 5：Graph Vocabulary 與 Adjacency Matrix

#### 概念說明

Graph 由 vertex 與 edge 組成。Undirected edge 表示雙向關係；directed edge 有 from 與 to。Adjacency matrix 使用 `matrix[from][to]` 表示 edge，檢查指定兩點是否相鄰為 O(1)，但需要 O(V²) 空間。

#### 實際應用

小型且 dense 的網路、固定節點的權限關係、遊戲地圖格與演算法教學常使用 matrix。

#### 資料變化

Undirected edge A-B 必須同時設定 `[A][B]` 與 `[B][A]`。移除時也要同時清除。

#### 設計判斷

Vertex 數量很大但 edge 很少時，matrix 會浪費大量空間；此時 adjacency list 更合適。

#### 範例程式

<!-- DEMO_START: AdjacencyMatrixGraph.java | Undirected Graph 的 adjacency matrix -->
檔名：`AdjacencyMatrixGraph.java`

```java
import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraph {
    private final List<String> vertices;
    private final boolean[][] edges;

    public AdjacencyMatrixGraph(List<String> vertices) {
        if (vertices == null || vertices.isEmpty()) {
            throw new IllegalArgumentException("vertices");
        }
        this.vertices = List.copyOf(vertices);
        this.edges = new boolean[vertices.size()][vertices.size()];
    }

    private int indexOf(String vertex) {
        int index = vertices.indexOf(vertex);
        if (index < 0) throw new IllegalArgumentException("unknown vertex: " + vertex);
        return index;
    }

    public void addEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = true;
        edges[b][a] = true;
    }

    public void removeEdge(String first, String second) {
        int a = indexOf(first);
        int b = indexOf(second);
        edges[a][b] = false;
        edges[b][a] = false;
    }

    public boolean hasEdge(String first, String second) {
        return edges[indexOf(first)][indexOf(second)];
    }

    public int degree(String vertex) {
        int row = indexOf(vertex);
        int degree = 0;
        for (boolean connected : edges[row]) if (connected) degree++;
        return degree;
    }

    public List<String> neighbors(String vertex) {
        int row = indexOf(vertex);
        List<String> result = new ArrayList<>();
        for (int column = 0; column < vertices.size(); column++) {
            if (edges[row][column]) result.add(vertices.get(column));
        }
        return result;
    }

    public static void main(String[] args) {
        AdjacencyMatrixGraph graph = new AdjacencyMatrixGraph(List.of("A", "B", "C", "D"));
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");
        System.out.println("A neighbors=" + graph.neighbors("A"));
        System.out.println("C degree=" + graph.degree("C"));
        System.out.println("B-C=" + graph.hasEdge("B", "C"));
        graph.removeEdge("A", "B");
        System.out.println("A-B=" + graph.hasEdge("A", "B"));
    }
}
```

```bash
javac AdjacencyMatrixGraph.java
java AdjacencyMatrixGraph
```

預期主要輸出：

```text
A neighbors=[B, C]
C degree=2
B-C=false
A-B=false
```
<!-- DEMO_END -->

#### 執行重點

Undirected Graph 的 matrix 應沿對角線對稱。

### 概念 6：Adjacency List 與 Sparse Graph

#### 概念說明

Adjacency list 為每個 vertex 保存 neighbor collection。空間約為 O(V+E)，列出某個 vertex 的 neighbors 與其 degree 成正比。使用 Set 可避免重複 edge。

#### 實際應用

社群網路、道路、網站連結、課程關係與服務依賴通常是 sparse Graph，適合 adjacency list。

#### 資料變化

加入 undirected edge A-B 時，A 的 Set 加入 B，B 的 Set 加入 A。

#### 設計判斷

若必須頻繁檢查任意兩點是否相鄰，matrix 更直接；若主要操作是列出 neighbors 或走訪 Graph，list 通常更節省。

#### 範例程式

<!-- DEMO_START: AdjacencyListGraph.java | 使用 Map 與 Set 建立 adjacency list -->
檔名：`AdjacencyListGraph.java`

```java
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AdjacencyListGraph {
    private final Map<String, Set<String>> adjacency = new LinkedHashMap<>();

    public boolean addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) return false;
        return adjacency.putIfAbsent(vertex.trim(), new LinkedHashSet<>()) == null;
    }

    public boolean addEdge(String first, String second) {
        if (!adjacency.containsKey(first) || !adjacency.containsKey(second)) return false;
        if (first.equals(second)) return false;
        boolean changed = adjacency.get(first).add(second);
        adjacency.get(second).add(first);
        return changed;
    }

    public boolean removeEdge(String first, String second) {
        if (!adjacency.containsKey(first) || !adjacency.containsKey(second)) return false;
        boolean changed = adjacency.get(first).remove(second);
        adjacency.get(second).remove(first);
        return changed;
    }

    public List<String> neighbors(String vertex) {
        Set<String> neighbors = adjacency.get(vertex);
        return neighbors == null ? List.of() : new ArrayList<>(neighbors);
    }

    public int edgeCount() {
        int degreeSum = 0;
        for (Set<String> neighbors : adjacency.values()) degreeSum += neighbors.size();
        return degreeSum / 2;
    }

    public static void main(String[] args) {
        AdjacencyListGraph graph = new AdjacencyListGraph();
        for (String vertex : List.of("A", "B", "C", "D")) graph.addVertex(vertex);
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("C", "D");
        graph.addEdge("A", "B");
        System.out.println("A=" + graph.neighbors("A"));
        System.out.println("edges=" + graph.edgeCount());
        System.out.println("missing=" + graph.neighbors("X"));
    }
}
```

```bash
javac AdjacencyListGraph.java
java AdjacencyListGraph
```

預期輸出：

```text
A=[B, C]
edges=3
missing=[]
```
<!-- DEMO_END -->

#### 執行重點

Undirected edge 只算一次，因此 degree sum 最後除以 2。

### 概念 7：Directed、Weighted Graph 與 In/Out Degree

#### 概念說明

Directed Graph 加入 A→B 時，只修改 A 的 outgoing edge。Weighted Graph 的 neighbor 不能只保存 vertex，還要保存 weight。Out-degree 是從 vertex 出發的 edge 數；in-degree 是指向 vertex 的 edge 數。

#### 實際應用

單行道、網站連結、追蹤關係、課程先修、服務呼叫與運輸成本都是 directed 或 weighted 關係。

#### 資料變化

加入 A→B(5) 與 C→B(2) 後，B 的 in-degree 是 2，out-degree 仍可能是 0。

#### 設計判斷

Weight 的意義必須一致，可能是距離、成本、時間或容量。不能在同一演算法中混用不同單位。

#### 範例程式

<!-- DEMO_START: WeightedDirectedGraph.java | Directed weighted adjacency list -->
檔名：`WeightedDirectedGraph.java`

```java
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WeightedDirectedGraph {
    public record Edge(String to, int weight) {
        public Edge {
            if (to == null || to.isBlank()) throw new IllegalArgumentException("to");
            if (weight < 0) throw new IllegalArgumentException("weight");
        }
    }

    private final Map<String, List<Edge>> outgoing = new LinkedHashMap<>();

    public void addVertex(String vertex) {
        if (vertex == null || vertex.isBlank()) throw new IllegalArgumentException("vertex");
        outgoing.putIfAbsent(vertex, new ArrayList<>());
    }

    public boolean addEdge(String from, String to, int weight) {
        if (!outgoing.containsKey(from) || !outgoing.containsKey(to)) return false;
        List<Edge> edges = outgoing.get(from);
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).to().equals(to)) {
                edges.set(i, new Edge(to, weight));
                return false;
            }
        }
        edges.add(new Edge(to, weight));
        return true;
    }

    public int outDegree(String vertex) {
        return outgoing.getOrDefault(vertex, List.of()).size();
    }

    public int inDegree(String vertex) {
        if (!outgoing.containsKey(vertex)) return 0;
        int count = 0;
        for (List<Edge> edges : outgoing.values()) {
            for (Edge edge : edges) if (edge.to().equals(vertex)) count++;
        }
        return count;
    }

    public List<Edge> outgoingFrom(String vertex) {
        return List.copyOf(outgoing.getOrDefault(vertex, List.of()));
    }

    public static void main(String[] args) {
        WeightedDirectedGraph graph = new WeightedDirectedGraph();
        for (String vertex : List.of("A", "B", "C")) graph.addVertex(vertex);
        graph.addEdge("A", "B", 5);
        graph.addEdge("C", "B", 2);
        graph.addEdge("A", "C", 4);
        System.out.println("A outgoing=" + graph.outgoingFrom("A"));
        System.out.println("B in=" + graph.inDegree("B"));
        System.out.println("B out=" + graph.outDegree("B"));
    }
}
```

```bash
javac WeightedDirectedGraph.java
java WeightedDirectedGraph
```

預期主要輸出：

```text
A outgoing=[Edge[to=B, weight=5], Edge[to=C, weight=4]]
B in=2
B out=0
```
<!-- DEMO_END -->

#### 執行重點

Directed edge 不可自動新增反向 edge；更新既有 edge weight 時不增加 edge count。

## 表示法比較

| 操作 | Adjacency Matrix | Adjacency List |
|---|---|---|
| 空間 | O(V²) | O(V+E) |
| 檢查指定 edge | O(1) | O(degree) |
| 列出 neighbors | O(V) | O(degree) |
| 適合 | Dense、vertex 固定 | Sparse、走訪為主 |

## 除錯練習

1. 相同 key 更新後 size 增加：新增前未先搜尋 chain。
2. 自訂 key 查不到：`equals()` 與 `hashCode()` 使用不同欄位。
3. Undirected edge 只有單向：只更新一方 adjacency。
4. 重複 add edge 造成 edge count 增加：neighbor 使用 List 且未檢查重複。
5. Directed Graph 的 in-degree 錯誤：把 outgoing size 當成 in-degree。

## 課堂實作題

### 課堂實作題一：可擴充 Hash Table

指定檔名：`ResizableStringMap.java`。使用 separate chaining；load factor 大於 0.75 時將 bucket 數擴充為原本兩倍加一，重新配置全部 entry。

### 課堂實作題二：課程成績統計

指定檔名：`CourseGradeMap.java`。使用 `Map<String,List<Integer>>` 管理課號與成績，提供新增、平均、最高分與依課號排序報告。

### 課堂實作題三：共同興趣

指定檔名：`InterestSetComparison.java`。使用 Set 計算 union、intersection、first-only 與 second-only，不修改輸入 Set。

### 課堂實作題四：校園 Matrix Graph

指定檔名：`CampusMatrixGraph.java`。支援新增與移除 undirected edge、查詢 degree、neighbors 與 edge count，重複 edge 不可重複計數。

### 課堂實作題五：社群 Adjacency List

指定檔名：`SocialNetworkGraph.java`。支援使用者、好友關係、共同好友、解除好友及查詢孤立使用者。

### 課堂實作題六：課程相依 Graph

指定檔名：`CourseDependencyGraph.java`。使用 directed adjacency list，輸出每門課的 prerequisites 與後續課程，並計算 in/out degree。

## 課後作業

### 課後作業一：圖書索引

指定檔名：`BookIsbnHashTable.java`。自行實作 Hash Table，支援新增、更新、搜尋、刪除、size、load factor 與 bucket report。

### 課後作業二：登入紀錄分析

指定檔名：`LoginActivityReport.java`。使用 HashMap 統計每個帳號次數，HashSet 找出不同 IP 數量，輸出異常重複登入報告。

### 課後作業三：選課重複檢查

指定檔名：`EnrollmentConflictSet.java`。以複合 key 表示學號與課號，找出重複紀錄、每人課程集合與每門課修課人數。

### 課後作業四：捷運 Matrix

指定檔名：`MetroMatrixGraph.java`。建立固定站點的 undirected matrix，輸出鄰站、degree、edge count 與 matrix report。

### 課後作業五：網站連結 Graph

指定檔名：`WebsiteLinkGraph.java`。建立 directed adjacency list，輸出 outgoing links、incoming count、無 incoming 頁面與無 outgoing 頁面。

### 課後作業六：物流成本網路

指定檔名：`LogisticsWeightedGraph.java`。支援 weighted directed edge 的新增、更新、移除與查詢，拒絕負權重及不存在 vertex。

## 常見錯誤與診斷

| 症狀 | 原因 | 修正 |
|---|---|---|
| 更新 key 後 size 增加 | 把 update 當 insert | chain 先比對 key |
| Rehash 後查不到資料 | 直接複製舊 bucket | 依新 bucket count 重算 index |
| HashSet 報告順序不固定 | 依賴 iterator 順序 | 顯示前轉 List 排序 |
| Matrix undirected 不對稱 | 只改一個方向 | 同步 `[a][b]`、`[b][a]` |
| Edge count 是兩倍 | degree sum 未除以 2 | undirected edge 只計一次 |

## 形成性評量

1. 相同 key 更新時，size 應如何改變？
2. Rehash 為何不能複製原 bucket index？
3. `equals()` 為 true 時，`hashCode()` 必須符合什麼規則？
4. HashMap、LinkedHashMap、TreeMap 的主要差異是什麼？
5. Dense Graph 與 sparse Graph 分別適合哪種表示法？
6. Undirected 與 directed edge 的新增操作有何不同？
7. Weighted neighbor 為何不能只存 vertex 名稱？

## 評分規準

| 面向 | 完整 | 部分完成 | 未完成 |
|---|---|---|---|
| Hash 操作 | 新增、更新、刪除、size 正確 | 少數邊界錯誤 | collision 資料遺失 |
| Key equality | equality 與 hash 一致 | normalization 不完整 | 相等 key 無法查找 |
| Graph 表示 | edge、degree、neighbors 正確 | 單一操作錯誤 | directed/undirected 混用 |
| 邊界處理 | duplicate、missing、empty 完整 | 缺少部分檢查 | 主要輸入造成例外 |
| 程式完整性 | 指定檔名並可獨立執行 | 少量錯誤 | 缺少主要 method |

## 參考教材

- Java Platform API：`HashMap`、`HashSet`、`Map`、`Set`。
- Open Data Structures：Hash Tables、Graphs。
- VisuAlgo：Graph Data Structures。

