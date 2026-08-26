# 8/24 教材：遞迴與 Binary Tree 走訪

## 單元名稱

Recursion、Binary Tree 與 Tree Traversal

## 課程定位

本單元將 method call、object reference、Stack 與 Queue 應用到樹狀資料。內容先建立 recursion 的 base case、recursive case 與 call stack 觀念，再建立 Binary Tree，完成 preorder、inorder、postorder 與 level-order 四種走訪。

Binary Tree 是後續 Binary Search Tree、Heap 與 Graph traversal 的重要基礎。重點不是背誦走訪結果，而是能使用小型資料追蹤每次 method call、node reference 與 Queue 狀態。

## 學習目標

1. 區分 base case、recursive case 與 progress。
2. 追蹤 call stack 的呼叫與返回順序。
3. 使用 recursion 處理數值與陣列。
4. 使用 node reference 建立 Binary Tree。
5. 說明 root、parent、child、leaf、subtree、depth 與 height。
6. 實作 preorder、inorder 與 postorder traversal。
7. 使用 Queue 實作 level-order traversal。
8. 處理 empty、single-node、skewed 與 irregular tree。
9. 整合 traversal、search、height 與 leaf count。

## 先備知識

- Method parameter、return value 與 method call。
- Array、class、object、constructor 與 object reference。
- Stack 的 LIFO 與 Queue 的 FIFO。
- `null` 表示 reference 沒有指向物件。

當日程式請放在：

```text
TKU_114-4_DS/0824/
```

## 問題情境

組織架構、檔案目錄、網站選單、商品分類與決策流程通常具有上下層關係。每個節點可以再連接子節點，無法只使用單一 index 表示完整關係。

Recursion 適合處理「整體問題可分成結構相同的小問題」。Binary Tree 的 left subtree 與 right subtree 仍然是 Binary Tree，因此可使用相同 method 重複處理。

## 核心概念

### 概念 1：Base case、recursive case 與 progress

#### 概念說明

Recursive method 會直接或間接呼叫自己。完整 recursion 必須包含 base case、recursive case 與 progress。Base case 可直接得到結果；recursive case 將問題縮小；progress 保證每次呼叫更接近 base case。

只有 base case 但沒有 progress，仍可能無限呼叫。Java 通常會在 call stack 用完時產生 `StackOverflowError`。

#### 實際應用

- 數值分解與數位處理。
- 樹狀目錄、選單與組織架構走訪。
- Merge sort 與其他 divide-and-conquer algorithm。

#### 資料變化

```text
countDown(3)
3 -> countDown(2)
2 -> countDown(1)
1 -> countDown(0)
0 -> base case, return
```

#### 設計判斷

問題能明確縮小且深度可控制時，recursion 能直接對應問題定義。大量線性重複通常適合 loop，避免建立大量 stack frame。

#### 範例程式

檔名：`RecursionCountdownDemo.java`

```java
public class RecursionCountdownDemo {
    static void countDown(int number) {
        if (number <= 0) {
            System.out.println("GO");
            return;
        }
        System.out.println(number);
        countDown(number - 1);
    }

    public static void main(String[] args) {
        countDown(3);
        countDown(0);
    }
}
```

執行結果：

```text
3
2
1
GO
GO
```

#### 執行重點

`number <= 0` 同時處理 0 與負數，避免負數繼續減 1。

---

### 概念 2：Call stack 與返回順序

#### 概念說明

每次 method call 都會建立 stack frame，保存 parameter、local variable 與返回位置。Recursive call 開始後，當前 frame 暫停，新 frame 放到 call stack 頂端。

遇到 base case 後，frame 依 LIFO 順序移除。程式回到上一層 recursive call 之後的位置，因此呼叫前與呼叫後的輸出順序相反。

#### 實際應用

- 理解 preorder 與 postorder 的差異。
- 追蹤 recursive search 的結果回傳。
- 診斷 `StackOverflowError`。

#### 資料變化

```text
trace(2): enter 2
  trace(1): enter 1
    trace(0): enter 0, base, leave 0
  leave 1
leave 2
```

#### 設計判斷

閱讀 recursion 時先用 0、1、2 等小型輸入，逐層寫出 parameter 與暫停位置，不要一次展開大量層級。

#### 範例程式

檔名：`CallStackTraceDemo.java`

```java
public class CallStackTraceDemo {
    static void trace(int level) {
        System.out.println("enter " + level);
        if (level == 0) {
            System.out.println("base");
        } else {
            trace(level - 1);
        }
        System.out.println("leave " + level);
    }

    public static void main(String[] args) {
        trace(3);
    }
}
```

執行結果：

```text
enter 3
enter 2
enter 1
enter 0
base
leave 0
leave 1
leave 2
leave 3
```

#### 執行重點

`enter` 由 3 到 0，`leave` 由 0 到 3，呈現 call stack 的 LIFO。

---

### 概念 3：使用 recursion 回傳計算結果

#### 概念說明

Recursive method 可以回傳結果。`sumTo(n)` 可定義為 `n + sumTo(n - 1)`，base case `sumTo(0)` 回傳 0。每一層 frame 等待下一層結果，再完成自己的運算。

Base case 與 recursive case 必須回傳相同型態。若某個分支沒有回傳值，程式將無法編譯或產生錯誤結果。

#### 實際應用

- 計算 tree node count、height 與 leaf count。
- 合併左右 subtree 的計算結果。
- 將搜尋到的 object 或 boolean 往上回傳。

#### 資料變化

```text
sumTo(4)
= 4 + sumTo(3)
= 4 + 3 + sumTo(2)
= 4 + 3 + 2 + sumTo(1)
= 4 + 3 + 2 + 1 + sumTo(0)
= 10
```

#### 設計判斷

先定義「單一層負責什麼」。輸入可能很大且 recursion 為線性深度時，loop 通常更適合。

#### 範例程式

檔名：`RecursiveResultDemo.java`

```java
public class RecursiveResultDemo {
    static int sumTo(int number) {
        if (number <= 0) {
            return 0;
        }
        return number + sumTo(number - 1);
    }

    static long factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("negative number");
        }
        if (number <= 1) {
            return 1;
        }
        return number * factorial(number - 1);
    }

    public static void main(String[] args) {
        System.out.println("sum=" + sumTo(5));
        System.out.println("5!=" + factorial(5));
        System.out.println("0!=" + factorial(0));
    }
}
```

執行結果：

```text
sum=15
5!=120
0!=1
```

#### 執行重點

`0!` 定義為 1。負數 factorial 在本程式中沒有合法定義，因此明確拒絕。

---

### 概念 4：使用 recursion 處理 array

#### 概念說明

處理 array 時，recursive method 通常接收 index。Base case 是 index 到達 length；recursive case 處理目前元素，再呼叫 `index + 1`。

不需要在每層複製剩餘 array。使用同一陣列與 index 即可表示尚未處理的範圍。

#### 實際應用

- 對 array 區間進行 divide and conquer。
- 累加、計數與尋找最大值。
- 練習用 parameter 表示目前位置。

#### 資料變化

```text
sum([4,7,2], 0)
= 4 + sum(..., 1)
= 4 + 7 + sum(..., 2)
= 4 + 7 + 2 + sum(..., 3)
= 13
```

#### 設計判斷

單純 array traversal 使用 loop 通常較清楚。此處使用 recursion 是為 tree traversal 準備，觀察 state 如何經由 parameter 前進。

#### 範例程式

檔名：`RecursiveArrayDemo.java`

```java
import java.util.Arrays;

public class RecursiveArrayDemo {
    static int sum(int[] values, int index) {
        if (values == null || index >= values.length) {
            return 0;
        }
        return values[index] + sum(values, index + 1);
    }

    static int countEven(int[] values, int index) {
        if (values == null || index >= values.length) {
            return 0;
        }
        int current = values[index] % 2 == 0 ? 1 : 0;
        return current + countEven(values, index + 1);
    }

    static void printReverse(int[] values, int index) {
        if (values == null || index >= values.length) {
            return;
        }
        printReverse(values, index + 1);
        System.out.print(values[index] + " ");
    }

    public static void main(String[] args) {
        int[] values = {4, 7, 2, 9};
        System.out.println(Arrays.toString(values));
        System.out.println("sum=" + sum(values, 0));
        System.out.println("even=" + countEven(values, 0));
        printReverse(values, 0);
        System.out.println();
    }
}
```

執行結果：

```text
[4, 7, 2, 9]
sum=22
even=2
9 2 7 4 
```

#### 執行重點

`printReverse` 在 recursive call 返回後才輸出，因此產生反向順序。

---

### 概念 5：Recursion 與 loop 的選擇

#### 概念說明

Loop 使用變數明確追蹤狀態；recursion 將每層狀態保存於 call stack。對線性資料，loop 通常較節省 stack space；對 tree 這種自我相似結構，recursion 可直接寫成「處理 left subtree，再處理 right subtree」。

#### 實際應用

- Loop：大量線性資料、計數與簡單走訪。
- Recursion：tree traversal、divide and conquer、backtracking。
- Explicit stack：需要自行控制深度與暫停狀態。

#### 資料變化

| 方式 | 狀態儲存 | 終止條件 |
|---|---|---|
| Loop | local variable | loop condition |
| Recursion | call stack frame | base case |

#### 設計判斷

選擇最能清楚表達問題且不會造成過深 stack 的方式。不能只因為 recursion 程式較短就忽略輸入深度。

#### 範例程式

檔名：`RecursionLoopComparison.java`

```java
public class RecursionLoopComparison {
    static int loopSum(int number) {
        int total = 0;
        for (int value = 1; value <= number; value++) {
            total += value;
        }
        return total;
    }

    static int recursiveSum(int number) {
        return number <= 0 ? 0 : number + recursiveSum(number - 1);
    }

    public static void main(String[] args) {
        for (int number : new int[]{0, 1, 5, 10}) {
            int loop = loopSum(number);
            int recursion = recursiveSum(number);
            System.out.println(number + " -> " + loop + ", " + recursion
                    + ", same=" + (loop == recursion));
        }
    }
}
```

執行結果：

```text
0 -> 0, 0, same=true
1 -> 1, 1, same=true
5 -> 15, 15, same=true
10 -> 55, 55, same=true
```

#### 執行重點

功能結果相同，不代表執行機制與資源使用相同。

---

### 概念 6：Binary Tree node 與 reference

#### 概念說明

Tree 由 node 與 edge 組成。Binary Tree 每個 node 最多有 left child 與 right child。`TreeNode` 保存 value、left reference 與 right reference；沒有 child 時 reference 為 `null`。

`root` 是整棵樹的進入點。Empty tree 的 root 為 `null`。只建立多個 node object 不會自動形成 tree，必須明確連接 reference。

#### 實際應用

- Expression tree、decision tree 與分類結構。
- Binary Search Tree 的資料節點。
- Heap 與 tree algorithm 的概念基礎。

#### 資料變化

```text
root = new Node("A")
root -> [A | left=null | right=null]

root.left = new Node("B")
root -> [A] -> left -> [B]
```

#### 設計判斷

Binary Tree 是結構限制，不自動代表排序。若每個 node 可能有任意數量 child，應使用 children collection 的一般 tree model。

#### 範例程式

檔名：`BinaryTreeNodeDemo.java`

```java
class TextTreeNode {
    String value;
    TextTreeNode left;
    TextTreeNode right;

    TextTreeNode(String value) {
        this.value = value;
    }
}

public class BinaryTreeNodeDemo {
    public static void main(String[] args) {
        TextTreeNode root = new TextTreeNode("A");
        root.left = new TextTreeNode("B");
        root.right = new TextTreeNode("C");
        root.left.left = new TextTreeNode("D");

        System.out.println("root=" + root.value);
        System.out.println("left=" + root.left.value);
        System.out.println("right=" + root.right.value);
        System.out.println("left-left=" + root.left.left.value);
        System.out.println("left-right=" + root.left.right);
    }
}
```

執行結果：

```text
root=A
left=B
right=C
left-left=D
left-right=null
```

#### 執行重點

所有 node 都必須能從 root 沿 reference path 到達，才屬於同一棵 tree。

---

### 概念 7：Tree terminology、depth 與 height

#### 概念說明

Root 沒有 parent；leaf 的 left 與 right 都是 `null`；subtree 是以某個 node 為 root 的完整子樹。Depth 是 root 到目標 node 的 edge 數；height 是 node 到最深 leaf 的最長 edge 數。

本教材定義 empty tree height = -1、leaf height = 0。不同教材可能採用 node 數定義 height，實作前必須先確認。

#### 實際應用

- Height 用於觀察 tree 深度與可能的操作路徑。
- Leaf count 統計沒有下層選項的結果。
- Depth 用於層級縮排與路徑報表。

#### 資料變化

```text
        A depth=0
       / \
 B depth=1 C depth=1
 /
D depth=2

D 是 leaf，A height=2
```

#### 設計判斷

Height method 前要先固定 empty tree 與 leaf 的定義，否則容易產生 off-by-one。整份程式必須使用一致定義。

#### 範例程式

檔名：`TreeTerminologyDemo.java`

```java
class NumberTreeNode {
    int value;
    NumberTreeNode left;
    NumberTreeNode right;

    NumberTreeNode(int value) {
        this.value = value;
    }
}

public class TreeTerminologyDemo {
    static int size(NumberTreeNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    static int leaves(NumberTreeNode node) {
        if (node == null) {
            return 0;
        }
        if (node.left == null && node.right == null) {
            return 1;
        }
        return leaves(node.left) + leaves(node.right);
    }

    static int height(NumberTreeNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    public static void main(String[] args) {
        NumberTreeNode root = new NumberTreeNode(10);
        root.left = new NumberTreeNode(5);
        root.right = new NumberTreeNode(20);
        root.left.left = new NumberTreeNode(3);

        System.out.println("size=" + size(root));
        System.out.println("leaves=" + leaves(root));
        System.out.println("height=" + height(root));
        System.out.println("empty height=" + height(null));
    }
}
```

執行結果：

```text
size=4
leaves=2
height=2
empty height=-1
```

#### 執行重點

Size 合併左右結果再加目前 node；height 只選左右較大值。

---

### 概念 8：Preorder traversal，Root-Left-Right

#### 概念說明

Preorder 進入 node 時先處理目前 node，再走訪 left subtree，最後走訪 right subtree。Base case 是 `node == null`。輸出 statement 位於兩個 recursive call 前。

#### 實際應用

- 先顯示 parent 再顯示 children。
- 複製 tree 時先建立 parent。
- Prefix expression 的節點順序。

#### 資料變化

```text
        A
       / \
      B   C
     / \
    D   E

preorder: A B D E C
```

#### 設計判斷

Parent 必須在 child 前處理時使用 preorder。若要先完成 child 再處理 parent，應使用 postorder。

#### 範例程式

檔名：`PreorderTraversalDemo.java`

```java
class PreorderNode {
    String value;
    PreorderNode left;
    PreorderNode right;

    PreorderNode(String value) {
        this.value = value;
    }
}

public class PreorderTraversalDemo {
    static void preorder(PreorderNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.value + " ");
        preorder(node.left);
        preorder(node.right);
    }

    public static void main(String[] args) {
        PreorderNode root = new PreorderNode("A");
        root.left = new PreorderNode("B");
        root.right = new PreorderNode("C");
        root.left.left = new PreorderNode("D");
        root.left.right = new PreorderNode("E");

        preorder(root);
        System.out.println();
        preorder(null);
        System.out.println("empty done");
    }
}
```

執行結果：

```text
A B D E C 
empty done
```

#### 執行重點

每個 subtree 都先輸出自己的 root，再處理兩個 child。

---

### 概念 9：Inorder traversal，Left-Root-Right

#### 概念說明

Inorder 先走訪 left subtree，再處理目前 node，最後走訪 right subtree。對一般 Binary Tree，inorder 不保證排序；只有滿足 BST property 時才會得到升冪結果。

#### 實際應用

- BST 以升冪順序輸出 key。
- Expression tree 產生 infix expression。
- 觀察左右 subtree 與 root 的相對順序。

#### 資料變化

```text
        A
       / \
      B   C
     / \
    D   E

inorder: D B E A C
```

#### 設計判斷

輸出位置在 left 與 right recursive call 之間就是 inorder。不能看到 inorder 就假設輸出一定排序。

#### 範例程式

檔名：`InorderTraversalDemo.java`

```java
class InorderNode {
    int value;
    InorderNode left;
    InorderNode right;

    InorderNode(int value) {
        this.value = value;
    }
}

public class InorderTraversalDemo {
    static void inorder(InorderNode node) {
        if (node == null) {
            return;
        }
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    public static void main(String[] args) {
        InorderNode root = new InorderNode(40);
        root.left = new InorderNode(10);
        root.right = new InorderNode(30);
        root.left.left = new InorderNode(20);

        inorder(root);
        System.out.println();
        System.out.println("ordinary binary tree");
    }
}
```

執行結果：

```text
20 10 40 30 
ordinary binary tree
```

#### 執行重點

輸出沒有排序，因為這棵樹尚未套用 BST property。

---

### 概念 10：Postorder traversal，Left-Right-Root

#### 概念說明

Postorder 先完成 left subtree，再完成 right subtree，最後處理目前 node。當 parent 必須等待兩個 child 結果時，postorder 是自然選擇。

#### 實際應用

- 刪除目錄前先刪除 children。
- 計算 subtree size 與 height。
- Postfix expression 與 expression tree evaluation。

#### 資料變化

```text
        A
       / \
      B   C
     / \
    D   E

postorder: D E B C A
```

#### 設計判斷

需要 child 結果才能處理 parent 時使用 postorder。若 parent 要先建立或先輸出，則使用 preorder。

#### 範例程式

檔名：`PostorderTraversalDemo.java`

```java
class PostorderNode {
    String value;
    PostorderNode left;
    PostorderNode right;

    PostorderNode(String value) {
        this.value = value;
    }
}

public class PostorderTraversalDemo {
    static void postorder(PostorderNode node) {
        if (node == null) {
            return;
        }
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.value + " ");
    }

    public static void main(String[] args) {
        PostorderNode root = new PostorderNode("A");
        root.left = new PostorderNode("B");
        root.right = new PostorderNode("C");
        root.left.left = new PostorderNode("D");
        root.left.right = new PostorderNode("E");
        postorder(root);
        System.out.println();
    }
}
```

執行結果：

```text
D E B C A 
```

#### 執行重點

輸出在兩個 recursive call 之後，因此 root A 最後才出現。

---

### 概念 11：Level-order traversal 與 Queue

#### 概念說明

Level-order 依 depth 從小到大走訪，同層由左到右。它使用 Queue 保存「已發現但尚未處理」的 node。每次 poll 一個 node，再將非 `null` child offer 到隊尾。

#### 實際應用

- 依層級輸出組織架構。
- 尋找距離 root 最近的符合條件 node。
- Graph BFS 的直接基礎。

#### 資料變化

```text
Queue=[A]
poll A, offer B,C -> [B,C]
poll B, offer D,E -> [C,D,E]
poll C -> [D,E]
poll D -> [E]
poll E -> []
```

#### 設計判斷

需要先完成較淺層級時使用 level-order。自然對應 subtree recursion 時，DFS traversal 通常更簡潔。

#### 範例程式

檔名：`LevelOrderTraversalDemo.java`

```java
import java.util.ArrayDeque;
import java.util.Queue;

class LevelNode {
    String value;
    LevelNode left;
    LevelNode right;

    LevelNode(String value) {
        this.value = value;
    }
}

public class LevelOrderTraversalDemo {
    static void levelOrder(LevelNode root) {
        if (root == null) {
            return;
        }
        Queue<LevelNode> queue = new ArrayDeque<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            LevelNode current = queue.poll();
            System.out.print(current.value + " ");
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
    }

    public static void main(String[] args) {
        LevelNode root = new LevelNode("A");
        root.left = new LevelNode("B");
        root.right = new LevelNode("C");
        root.left.left = new LevelNode("D");
        root.left.right = new LevelNode("E");
        levelOrder(root);
        System.out.println();
    }
}
```

執行結果：

```text
A B C D E 
```

#### 執行重點

`ArrayDeque` 不允許 `null`，只將實際存在的 child offer 進 Queue。

---

### 概念 12：綜合應用，組織架構樹

#### 概念說明

簡化組織架構以 Binary Tree 表示上層與兩個下層單位。系統整合 preorder、level-order、contains、size 與 height。

#### 實際應用

- 組織單位、分類選單與決策分支。
- Preorder 先顯示上層單位。
- Level-order 製作各層級報表。

#### 資料變化

```text
             HeadOffice
             /        \
       Sales           Technology
       /   \            /      \
 Domestic Export   Platform    Support
```

#### 設計判斷

真實組織的 child 數量不一定只有兩個，正式系統可能應使用 `List<OrgNode> children`。資料結構必須符合 domain，不能為了使用 Binary Tree 強迫資料二分。

#### 範例程式

檔名：`OrganizationTreeSystem.java`

```java
import java.util.ArrayDeque;
import java.util.Queue;

class OrgNode {
    String name;
    OrgNode left;
    OrgNode right;

    OrgNode(String name) {
        this.name = name;
    }
}

class OrganizationTree {
    private final OrgNode root;

    OrganizationTree(OrgNode root) {
        this.root = root;
    }

    void preorder() {
        preorder(root);
        System.out.println();
    }

    private void preorder(OrgNode node) {
        if (node == null) {
            return;
        }
        System.out.print(node.name + " ");
        preorder(node.left);
        preorder(node.right);
    }

    void levelOrder() {
        if (root == null) {
            System.out.println("empty");
            return;
        }
        Queue<OrgNode> queue = new ArrayDeque<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            OrgNode node = queue.poll();
            System.out.print(node.name + " ");
            if (node.left != null) queue.offer(node.left);
            if (node.right != null) queue.offer(node.right);
        }
        System.out.println();
    }

    boolean contains(String target) {
        return contains(root, target);
    }

    private boolean contains(OrgNode node, String target) {
        if (node == null || target == null) return false;
        return node.name.equals(target)
                || contains(node.left, target)
                || contains(node.right, target);
    }

    int size() {
        return size(root);
    }

    private int size(OrgNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(OrgNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }
}

public class OrganizationTreeSystem {
    public static void main(String[] args) {
        OrgNode root = new OrgNode("HeadOffice");
        root.left = new OrgNode("Sales");
        root.right = new OrgNode("Technology");
        root.left.left = new OrgNode("Domestic");
        root.left.right = new OrgNode("Export");
        root.right.left = new OrgNode("Platform");
        root.right.right = new OrgNode("Support");

        OrganizationTree tree = new OrganizationTree(root);
        tree.preorder();
        tree.levelOrder();
        System.out.println("Support=" + tree.contains("Support"));
        System.out.println("HR=" + tree.contains("HR"));
        System.out.println("size=" + tree.size());
        System.out.println("height=" + tree.height());
    }
}
```

執行結果：

```text
HeadOffice Sales Domestic Export Technology Platform Support 
HeadOffice Sales Technology Domestic Export Platform Support 
Support=true
HR=false
size=7
height=2
```

#### 執行重點

這是一般 Binary Tree，`contains` 無法利用大小順序，必要時要搜尋兩個 subtree。

## 程式執行追蹤

### 追蹤一：Preorder call stack

| 步驟 | Node | 動作 | 已輸出 |
|---|---|---|---|
| 1 | A | 輸出並進入 left | A |
| 2 | B | 輸出並進入 left | A B |
| 3 | D | 兩個 child 為 null | A B D |
| 4 | E | 返回 B 後進入 right | A B D E |
| 5 | C | 返回 A 後進入 right | A B D E C |

### 追蹤二：Level-order Queue

| Poll | Offer | Queue | 輸出 |
|---|---|---|---|
| A | B, C | `[B, C]` | A |
| B | D, E | `[C, D, E]` | A B |
| C | 無 | `[D, E]` | A B C |
| D | 無 | `[E]` | A B C D |
| E | 無 | `[]` | A B C D E |

## 除錯練習

### 除錯練習一：沒有接近 base case

```java
static void broken(int number) {
    if (number == 0) return;
    broken(number + 1);
}
```

正數會持續增加，永遠無法到達 0。倒數應使用 `number - 1`，base case 也應處理 `number <= 0`。

### 除錯練習二：Traversal 缺少 null case

```java
static void preorder(TreeNode node) {
    System.out.println(node.value);
    preorder(node.left);
    preorder(node.right);
}
```

走到 leaf 的 child 時 node 為 `null`，會發生 `NullPointerException`。Method 開頭必須先處理 `node == null`。

## 課堂實作題

### 課堂實作題一：遞迴數位統計

指定檔名：`RecursiveDigitReport.java`

完成 `digitSum`、`digitCount` 與 `countDigit`。負數先轉絕對值，`digitCount(0)` 回傳 1。使用 50205、0、-731 測試。核心計算不得使用 loop 或轉成 String。

### 課堂實作題二：遞迴陣列統計

指定檔名：`RecursiveArrayStatistics.java`

完成 `maximum`、`minimum` 與 `countAbove`。Public wrapper 對 `null` 或 empty array 拋出 `IllegalArgumentException`，helper 不得複製 array。

### 課堂實作題三：Binary Tree 結構報表

指定檔名：`BinaryTreeStructureReport.java`

建立至少 7 個 node，輸出 root、所有 leaf、size、leaf count 與 height，另外測試 empty tree 與 single-node tree。

### 課堂實作題四：三種 DFS traversal

指定檔名：`ThreeTraversalPractice.java`

對 `M(F(B,null),T(R,Z))` 完成 preorder、inorder、postorder。三個 method 都要處理 `null`，不得寫死結果字串。

### 課堂實作題五：逐層分行輸出

指定檔名：`LevelOrderByLine.java`

使用 Queue 將每層輸出在不同行，並輸出每層 node count。處理 empty tree，不得用 DFS 假裝 level-order。

### 課堂實作題六：樹狀選單搜尋

指定檔名：`MenuTreeSearch.java`

完成 `contains`、`findDepth`、`countLeaves` 與 preorder display。找不到時 depth 回傳 -1。

## 課後作業

### 課後作業一：遞迴字串工具

指定檔名：`RecursiveTextTools.java`

完成 `reverse`、`isPalindrome` 與 `countCharacter`。Palindrome 忽略英文大小寫與空白，測試 empty、single character、`Level` 與一般字串。

### 課後作業二：Binary Tree 統計系統

指定檔名：`BinaryTreeStatistics.java`

完成 size、sum、maximum、leaf count、height 與 contains。`maximum` 要明確處理 empty tree，不可一律用 0 代表空樹最大值。

### 課後作業三：Traversal 結果集合

指定檔名：`TraversalResultCollector.java`

讓四種 traversal 回傳 `List<String>`，不直接輸出。測試 empty、single-node、left-skewed 與 complete tree。

### 課後作業四：目錄大小累加

指定檔名：`FolderSizeTree.java`

`FolderNode` 保存 name、ownSize、left、right。使用 postorder 計算 subtree size，輸出總大小、最大 subtree 與 leaf folder。

### 課後作業五：組織架構報表

指定檔名：`OrganizationTreeReport.java`

新增 `findParent`、`findDepth`、`pathFromRoot` 與 `printByLevel`。找不到單位時回傳空結果，不得發生例外。

### 課後作業六：Traversal 測試報告

指定檔名：`TraversalTestReport.java`

建立 empty、single-node、only-left、only-right、complete 與 irregular tree，輸出四種 traversal 的預期與實際結果及是否相同。

## 常見錯誤與診斷

| 問題 | 常見原因 | 修正方式 |
|---|---|---|
| `StackOverflowError` | 無 base case 或沒有 progress | 以小型輸入追蹤每層 parameter |
| `NullPointerException` | 未處理 `node == null` | Traversal 開頭加入 null case |
| Traversal 順序錯誤 | Root 處理位置錯誤 | 檢查輸出在兩個 recursive call 的前、中、後 |
| Height 差 1 | Empty 與 leaf 定義不一致 | 固定 empty=-1、leaf=0 |
| Level-order 重複 | Child 被重複 offer | 每次 poll 只加入目前 node 的 child |
| Inorder 沒有排序 | 誤以為所有 Binary Tree 都是 BST | 先檢查 BST property |
| Node 無法到達 | Object 未從 root 連接 | 追蹤 root 到 node 的 reference path |
| Queue 拒絕 null | 未檢查 child | 只 offer 非 null child |

## 形成性評量

1. Base case、recursive case 與 progress 各自負責什麼？
2. `trace(3)` 的 leave 順序為何與 enter 相反？
3. Empty tree 的 root 應該是什麼？
4. Leaf node 如何判斷？
5. 三種 DFS traversal 的 root 處理位置有何不同？
6. 一般 Binary Tree 的 inorder 為何不一定排序？
7. Level-order 為何使用 Queue？
8. Height 為何選擇左右 subtree 較大值？
9. Postorder 適合刪除目錄的原因是什麼？
10. Skewed tree 如何影響 recursive call depth？

## 評分規準

| 評分項目 | 完整達成 | 部分達成 | 尚未達成 |
|---|---|---|---|
| Recursion | Base case、progress、return 正確 | 一般輸入正確，邊界不足 | 無法終止或回傳錯誤 |
| Tree structure | Root 與 child 連接完整 | 部分 node 未連接 | 只有獨立 object |
| DFS traversal | 三種順序與 null case 正確 | 兩種正確 | 順序與 base case 皆錯 |
| Level-order | Queue 操作與結果正確 | Empty case 不足 | 未使用 FIFO 或結果重複 |
| Tree statistics | Size、leaf、height 定義一致 | 一般案例正確 | Empty/single 出現 off-by-one |
| Testing | 四種以上結構與邊界均測試 | 只測一般 tree | 沒有可核對結果 |
| 程式完整性 | 檔名正確且可編譯 | 少量錯誤 | 缺少 class 或 method |
| GitHub 繳交 | `0824` 連結與檔案完整 | 命名或紀錄缺漏 | 未 push 或連結錯誤 |

## 參考教材

- [Algorithms, 4th Edition：Binary Search Trees](https://algs4.cs.princeton.edu/32bst/)
- [Java SE 17 Queue API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Queue.html)
- [Java SE 17 ArrayDeque API](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayDeque.html)
- [Dev.java：Learn Java](https://dev.java/learn/)
