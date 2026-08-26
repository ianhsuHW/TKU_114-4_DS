# 8/25 教材：Binary Search Tree 搜尋、插入與刪除

## 單元名稱

Binary Search Tree：Search、Insert、Delete 與 Validation

## 課程定位

本單元在 Binary Tree traversal 基礎上加入 ordering invariant，使搜尋不必走訪整棵樹。內容完整拆解 search、insert、duplicate policy、minimum/maximum、tree shape，以及 leaf、single child、two children 三種 delete case。

## 學習目標

1. 說明 BST property 並判斷 tree 是否有效。
2. 實作 recursive 與 iterative search。
3. 實作 insert 並處理 duplicate key。
4. 取得 minimum、maximum、size 與 height。
5. 分別完成三種 delete case。
6. 使用 inorder successor 維持 BST property。
7. 測試 empty、single-node、skewed、missing 與 duplicate。
8. 使用 BST 管理完整 domain object。

## 先備知識

- Binary Tree node、root、leaf、subtree、height。
- Preorder、inorder、postorder 與 recursion。
- Object reference、class、constructor 與 comparison。

當日程式放在 `TKU_114-4_DS/0825/`。

## 問題情境

一般 Binary Tree 沒有 key 方向，搜尋可能必須檢查所有 node。BST 將較小 key 放在 left subtree、較大 key 放在 right subtree，每次比較可排除另一側。這項優勢必須以持續維護 ordering invariant 為前提。

## 核心概念

### 概念 1：BST property 與有方向的搜尋

#### 概念說明

Binary Search Tree 對每個 node 定義 left subtree 的 key 較小、right subtree 的 key 較大。本教材採用不接受 duplicate key 的規則。這個 property 必須對每一個 subtree 都成立，不只比較 parent 和直接 child。

#### 實際應用

- 依編號搜尋商品或會員
- 維持可排序且可動態新增的資料
- 為 TreeMap 與 TreeSet 建立概念基礎

#### 資料變化

```text
插入 50,30,70,20,40 後，inorder 為 20 30 40 50 70。
```

#### 設計判斷

BST 適合需要動態 search/insert 且資料可比較的情境。若只需依 key 精確查找，HashMap 可能更直接。

#### 範例程式

檔名：`BstPropertyDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class BstPropertyDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        tree.inorder();
        System.out.println("30=" + tree.contains(30));
    }
}
```

執行結果：

```text
20 30 40 50 60 70 80 
30=true
```

#### 執行重點

Inorder 升冪是整棵樹持續符合 BST property 的結果。

---

### 概念 2：Recursive search 與路徑縮小

#### 概念說明

搜尋時比較 target 和 current value。相等即成功；較小只搜尋 left；較大只搜尋 right。每次呼叫排除另一個 subtree。

#### 實際應用

- 動態索引查詢
- 搜尋排序資料
- 建立 recursive decision path

#### 資料變化

```text
搜尋 60：50 -> 70 -> 60；搜尋 65：50 -> 70 -> 60 -> null。
```

#### 設計判斷

Tree 平衡時路徑較短；skewed tree 最差可能接近 sequential search。

#### 範例程式

檔名：`RecursiveBstSearchDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class RecursiveBstSearchDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("60=" + tree.contains(60));
        System.out.println("65=" + tree.contains(65));
    }
}
```

執行結果：

```text
60=true
65=false
```

#### 執行重點

找不到時必須在 current 變成 null 時停止。

---

### 概念 3：Iterative search 與 current reference

#### 概念說明

Search 也可用 while loop。current 從 root 開始，每次比較後改指向 left 或 right，直到找到或變成 null。

#### 實際應用

- 避免過深 recursive call
- 逐步輸出搜尋路徑
- 需要容易中止的互動查詢

#### 資料變化

```text
current=50，target=20 -> 30 -> 20。
```

#### 設計判斷

Iterative 與 recursive search 的比較次數相同；差別是 state 放在 local variable 或 call stack。

#### 範例程式

檔名：`IterativeBstSearchDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class IterativeBstSearchDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("20=" + tree.contains(20));
        System.out.println("90=" + tree.contains(90));
    }
}
```

執行結果：

```text
20=true
90=false
```

#### 執行重點

每輪必須更新 current，否則 while loop 無法前進。

---

### 概念 4：Insert 與新 leaf 位置

#### 概念說明

Insert 使用 search path 尋找第一個 null child，再把新 node 接到該位置。新加入 node 一定先成為 leaf。

#### 實際應用

- 動態建立索引
- 依 key 維持搜尋方向
- 逐筆載入資料

#### 資料變化

```text
插入 65：50 -> right 70 -> left 60 -> right null，接在 60.right。
```

#### 設計判斷

Insert 只可修改找到的 null link，不可破壞其他 subtree。

#### 範例程式

檔名：`BstInsertDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class BstInsertDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("add65=" + tree.add(65));
        tree.inorder();
    }
}
```

執行結果：

```text
add65=true
20 30 40 50 60 65 70 80 
```

#### 執行重點

插入後用 inorder 檢查排序是否仍成立。

---

### 概念 5：Duplicate key policy

#### 概念說明

BST 必須明確定義 equal key 的處理。本教材拒絕 duplicate，add 回傳 false 且 tree 不改變。也可設計 count 或 value replacement，但不能沒有規則。

#### 實際應用

- 防止重複學號
- 相同 key 更新 value
- 統計重複次數

#### 資料變化

```text
已有 40，再 add(40)：回傳 false，size 不變。
```

#### 設計判斷

若 domain 允許相同排序欄位，應使用複合 key 或在 node 中保存 collection。

#### 範例程式

檔名：`BstDuplicatePolicyDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class BstDuplicatePolicyDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("before=" + tree.size());
        System.out.println("duplicate=" + tree.add(40));
        System.out.println("after=" + tree.size());
    }
}
```

執行結果：

```text
before=7
duplicate=false
after=7
```

#### 執行重點

不能一律把相等值放到右側卻不說明，否則 search/delete 規則可能不一致。

---

### 概念 6：Minimum、maximum 與方向

#### 概念說明

BST minimum 持續往 left；maximum 持續往 right。Empty tree 沒有最小或最大值，本範例回傳 null。

#### 實際應用

- 取得最早或最低 key
- 範圍查詢端點
- Delete successor 的基礎

#### 資料變化

```text
50 -> left 30 -> left 20，minimum=20。
```

#### 設計判斷

這個捷徑只適用 BST；一般 Binary Tree 必須搜尋所有 node。

#### 範例程式

檔名：`BstMinMaxDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class BstMinMaxDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("min=" + tree.minimum());
        System.out.println("max=" + tree.maximum());
        System.out.println("empty=" + new IntBst().minimum());
    }
}
```

執行結果：

```text
min=20
max=80
empty=null
```

#### 執行重點

Minimum node method 將在 two-child delete 中重用。

---

### 概念 7：Size、height 與 tree shape

#### 概念說明

Size 合併左右 subtree 數量；height 選擇左右較大值。相同 node 數可能形成不同 height，shape 直接影響 search path。

#### 實際應用

- 評估索引深度
- 觀察 skewed tree
- 建立平衡概念

#### 資料變化

```text
7-node complete shape height=2；依升冪插入 7 個值可能 height=6。
```

#### 設計判斷

BST 不會自動平衡。需要穩定 logarithmic operation 時要使用平衡樹實作。

#### 範例程式

檔名：`BstStatisticsDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }



}

public class BstStatisticsDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("size=" + tree.size());
        System.out.println("height=" + tree.height());
    }
}
```

執行結果：

```text
size=7
height=2
```

#### 執行重點

Height 定義延續 empty=-1、leaf=0。

---

### 概念 8：Delete case 1：leaf node

#### 概念說明

Leaf 沒有 child。刪除時讓 parent 原本指向 leaf 的 reference 改為 null。Recursive remove 可直接 return null。

#### 實際應用

- 移除索引端點
- 理解 reference replacement
- 三種 delete case 的起點

#### 資料變化

```text
刪除 20：30.left 由 node20 改為 null。
```

#### 設計判斷

必須先確認 target 存在，讓 public remove 能回報成功或失敗。

#### 範例程式

檔名：`BstDeleteLeafDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }
    
    private IntNode remove(IntNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            IntNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }
    
    private IntNode minimumNode(IntNode node) {
        while (node.left != null) node = node.left;
        return node;
    }


}

public class BstDeleteLeafDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("remove20=" + tree.remove(20));
        tree.inorder();
    }
}
```

執行結果：

```text
remove20=true
30 40 50 60 70 80 
```

#### 執行重點

Leaf delete 不需要搬移其他 key。

---

### 概念 9：Delete case 2：single child

#### 概念說明

只有一個 child 的 node 被刪除時，parent 應直接改連到該 child。Recursive remove 以 return non-null child 完成 reference replacement。

#### 實際應用

- 縮短只有單一路徑的 subtree
- 保留 target 下方資料
- 處理 root replacement

#### 資料變化

```text
若 30 只有 right child 40，刪除 30 後 50.left 直接指向 40。
```

#### 設計判斷

不能只把 target 設為 null，否則其 child subtree 會從 root 失去連結。

#### 範例程式

檔名：`BstDeleteOneChildDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }
    
    private IntNode remove(IntNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            IntNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }
    
    private IntNode minimumNode(IntNode node) {
        while (node.left != null) node = node.left;
        return node;
    }


}

public class BstDeleteOneChildDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 40}) tree.add(value);
        System.out.println("remove30=" + tree.remove(30));
        tree.inorder();
    }
}
```

執行結果：

```text
remove30=true
40 50 70 
```

#### 執行重點

Single child 也可能發生在 root，root 必須改為原本的 child。

---

### 概念 10：Delete case 3：two children 與 successor

#### 概念說明

有兩個 child 時不能直接移除 target。先找 right subtree minimum 作為 inorder successor，複製 successor value 到 target，再從 right subtree 刪除原 successor。

#### 實際應用

- 維持 BST property
- 刪除內部索引節點
- 理解兩階段 delete

#### 資料變化

```text
刪除 50：successor=60；root 改為60；再刪除原本的 node60。
```

#### 設計判斷

也可選 left subtree maximum 作 predecessor，但整份實作必須一致。

#### 範例程式

檔名：`BstDeleteTwoChildrenDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }

    boolean remove(int value) {
        if (!contains(value)) return false;
        root = remove(root, value);
        return true;
    }
    
    private IntNode remove(IntNode node, int value) {
        if (node == null) return null;
        if (value < node.value) {
            node.left = remove(node.left, value);
        } else if (value > node.value) {
            node.right = remove(node.right, value);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            IntNode successor = minimumNode(node.right);
            node.value = successor.value;
            node.right = remove(node.right, successor.value);
        }
        return node;
    }
    
    private IntNode minimumNode(IntNode node) {
        while (node.left != null) node = node.left;
        return node;
    }


}

public class BstDeleteTwoChildrenDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("remove50=" + tree.remove(50));
        tree.inorder();
    }
}
```

執行結果：

```text
remove50=true
20 30 40 60 70 80 
```

#### 執行重點

Successor 在 right subtree 中沒有 left child，因此第二次刪除較簡單。

---

### 概念 11：Validate BST 與邊界範圍

#### 概念說明

只檢查 node.left < node < node.right 不夠。每個 node 還必須符合所有 ancestor 帶下來的 minimum/maximum 範圍。

#### 實際應用

- 檢查手動連接的 tree
- 驗證 delete 後 invariant
- 偵測深層錯位 node

#### 資料變化

```text
Root=50 時，left subtree 所有值都必須在 (-infinity,50)。
```

#### 設計判斷

使用 long boundary 可避免 int minimum/maximum 本身成為無法驗證的特殊值。

#### 範例程式

檔名：`BstValidationDemo.java`

```java
class IntNode {
    int value;
    IntNode left;
    IntNode right;

    IntNode(int value) {
        this.value = value;
    }
}

class IntBst {
    private IntNode root;

    boolean add(int value) {
        if (root == null) {
            root = new IntNode(value);
            return true;
        }
        IntNode current = root;
        while (true) {
            if (value == current.value) return false;
            if (value < current.value) {
                if (current.left == null) {
                    current.left = new IntNode(value);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new IntNode(value);
                    return true;
                }
                current = current.right;
            }
        }
    }

    boolean contains(int value) {
        IntNode current = root;
        while (current != null) {
            if (value == current.value) return true;
            current = value < current.value ? current.left : current.right;
        }
        return false;
    }

    Integer minimum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.left != null) current = current.left;
        return current.value;
    }

    Integer maximum() {
        if (root == null) return null;
        IntNode current = root;
        while (current.right != null) current = current.right;
        return current.value;
    }

    int size() {
        return size(root);
    }

    private int size(IntNode node) {
        return node == null ? 0 : 1 + size(node.left) + size(node.right);
    }

    int height() {
        return height(root);
    }

    private int height(IntNode node) {
        return node == null
                ? -1
                : 1 + Math.max(height(node.left), height(node.right));
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(IntNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.value + " ");
        inorder(node.right);
    }


    boolean isValid() {
        return isValid(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private boolean isValid(IntNode node, long minimum, long maximum) {
        if (node == null) return true;
        if (node.value <= minimum || node.value >= maximum) return false;
        return isValid(node.left, minimum, node.value)
                && isValid(node.right, node.value, maximum);
    }

}

public class BstValidationDemo {
    public static void main(String[] args) {
        IntBst tree = new IntBst();
        for (int value : new int[]{50, 30, 70, 20, 40, 60, 80}) {
            tree.add(value);
        }
        System.out.println("valid=" + tree.isValid());
    }
}
```

執行結果：

```text
valid=true
```

#### 執行重點

Validation 是全域規則檢查，不只是 parent-child 比較。

---

### 概念 12：綜合應用：商品 BST 索引

#### 概念說明

商品以 id 作為 key，BST node 保存完整 Product object。系統完成新增、防重複、依 id 查找與 inorder 報表。

#### 實際應用

- 商品與會員索引
- 依 key 排序輸出 object
- 結合 OOP 與 tree

#### 資料變化

```text
加入 id 300,100,500,200 後，inorder 依 100,200,300,500。
```

#### 設計判斷

若主要需求只有精確 id 查找，可比較 HashMap；若同時需要有序輸出與範圍操作，tree 更有意義。

#### 範例程式

檔名：`ProductBstIndexSystem.java`

```java
class Product {
    int id;
    String name;
    int stock;

    Product(int id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = Math.max(0, stock);
    }

    @Override
    public String toString() {
        return id + " " + name + " stock=" + stock;
    }
}

class ProductNode {
    Product data;
    ProductNode left;
    ProductNode right;

    ProductNode(Product data) {
        this.data = data;
    }
}

class ProductBst {
    private ProductNode root;

    boolean add(Product product) {
        if (product == null) return false;
        if (root == null) {
            root = new ProductNode(product);
            return true;
        }
        ProductNode current = root;
        while (true) {
            if (product.id == current.data.id) return false;
            if (product.id < current.data.id) {
                if (current.left == null) {
                    current.left = new ProductNode(product);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new ProductNode(product);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Product find(int id) {
        ProductNode current = root;
        while (current != null) {
            if (id == current.data.id) return current.data;
            current = id < current.data.id ? current.left : current.right;
        }
        return null;
    }

    void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(ProductNode node) {
        if (node == null) return;
        inorder(node.left);
        System.out.print(node.data + " | ");
        inorder(node.right);
    }
}

public class ProductBstIndexSystem {
    public static void main(String[] args) {
        ProductBst tree = new ProductBst();
        System.out.println(tree.add(new Product(300, "Keyboard", 5)));
        System.out.println(tree.add(new Product(100, "Mouse", 8)));
        System.out.println(tree.add(new Product(500, "Monitor", 2)));
        System.out.println(tree.add(new Product(200, "Hub", 4)));
        System.out.println(tree.add(new Product(100, "Duplicate", 1)));
        tree.inorder();
        System.out.println("find=" + tree.find(200));
        System.out.println("missing=" + tree.find(999));
    }
}
```

執行結果：

```text
true
true
true
true
false
100 Mouse stock=8 | 200 Hub stock=4 | 300 Keyboard stock=5 | 500 Monitor stock=2 | 
find=200 Hub stock=4
missing=null
```

#### 執行重點

Node 保存完整 object，但比較規則只使用穩定且唯一的 id。

---
## 程式執行追蹤

### 追蹤一：Search 65

| Current | 比較 | 後續動作 |
|---:|---|---|
| 50 | 65 > 50 | right 70 |
| 70 | 65 < 70 | left 60 |
| 60 | 65 > 60 | right null |
| null | 找不到 | false |

### 追蹤二：Delete 50

1. Target 50 有 left 30 與 right 70。
2. Right subtree minimum 為 60。
3. 將 target value 改為 60。
4. 從原 right subtree 刪除 node 60。
5. Inorder 仍為升冪且沒有重複 60。

## 除錯練習

### 除錯練習一：Search 方向寫反

若 `target < current.value` 卻移到 `current.right`，會離 target 越來越遠。較小值必須往 left，較大值往 right。

### 除錯練習二：Two-child delete 只複製 successor

只將 target 改成 successor value，卻未刪除原 successor，會產生 duplicate key。複製後必須再從 right subtree remove successor。

## 課堂實作題

### 課堂實作題一：BST Search Trace

指定檔名：`BstSearchTrace.java`。輸出每次比較的 current value、方向與 comparison count，測試找到 root、leaf、internal node 與 missing value。

### 課堂實作題二：Duplicate Policy

指定檔名：`BstDuplicateCounter.java`。Node 增加 count，相同 key 不建立新 node而增加 count。Inorder 輸出 `key(count)`。

### 課堂實作題三：Min、Max 與範圍

指定檔名：`BstRangeReport.java`。完成 min、max 與 `printRange(low, high)`，包含端點並處理 low > high。

### 課堂實作題四：三種 Delete Case

指定檔名：`BstDeleteCases.java`。依序刪除 leaf、single-child、two-child node，每次輸出 inorder、size 與 valid result。

### 課堂實作題五：Skewed Tree Report

指定檔名：`SkewedBstReport.java`。分別用排序資料與平衡順序建立 tree，比較 size、height 與 search comparison count。

### 課堂實作題六：BST Validation

指定檔名：`BstInvariantChecker.java`。建立 valid tree 與至少三棵深層違規 tree，使用 min/max boundary 驗證。

## 課後作業

### 課後作業一：學號索引

指定檔名：`StudentBstIndex.java`。Node 保存 Student，依 studentId search/insert/delete，重複 id 不得加入。

### 課後作業二：商品庫存 BST

指定檔名：`ProductInventoryBst.java`。完成新增、查詢、補貨、扣庫存、delete 與 inorder report，所有修改先依 id 找 object。

### 課後作業三：排名範圍查詢

指定檔名：`ScoreRangeBst.java`。Key 使用 score+studentId 複合順序，支援同分資料並輸出指定分數範圍。

### 課後作業四：完整 Delete 測試

指定檔名：`BstDeleteTestSuite.java`。測試 empty、missing、single root、root with one child、root with two children 與連續刪除到 empty。

### 課後作業五：Tree Shape Experiment

指定檔名：`BstShapeExperiment.java`。使用相同 15 個值以三種順序插入，比較 height 與全部 search comparison count。

### 課後作業六：訂單索引系統

指定檔名：`OrderBstSystem.java`。Order 以 orderId 排序，完成 add、find、cancel、updateAmount、range report 與 summary。

## 常見錯誤與診斷

| 問題 | 原因 | 修正 |
|---|---|---|
| Inorder 不排序 | Insert 接錯方向 | 每次插入後檢查 invariant |
| Duplicate 無限前進 | Equal case 未處理 | 明確拒絕、計數或更新 |
| Delete 遺失 subtree | Single-child case 回傳 null | 回傳 non-null child |
| Two-child 仍有重複 | 未刪除 successor | 複製後 remove successor |
| Root 刪除無效 | 未接回 `root = remove(...)` | 保存 recursive result |
| Missing delete 回報成功 | 無存在檢查 | Public method 先 contains 或回傳狀態 |
| Validation 漏掉深層錯誤 | 只比 parent-child | 傳遞 min/max boundary |
| 效率退化 | Tree 形成 skewed shape | 觀察 height，評估平衡樹 |

## 形成性評量

1. BST property 必須對哪些 node 成立？
2. Search 為何可排除一個 subtree？
3. 新插入 node 為何先成為 leaf？
4. Duplicate key 有哪些合理 policy？
5. Minimum 為何持續往 left？
6. 三種 delete case 的 reference 變化是什麼？
7. Successor 為何選 right subtree minimum？
8. Root delete 為何要重新指定 root？
9. Parent-child comparison 為何不足以 validate BST？
10. Skewed tree 對 search 有何影響？

## 評分規準

| 項目 | 完整達成 | 部分達成 | 尚未達成 |
|---|---|---|---|
| Property | 所有操作維持 invariant | 一般案例正確 | Inorder 不排序 |
| Search/insert | 方向、missing、duplicate 完整 | 邊界不足 | 無法前進或錯誤方向 |
| Delete | 三種 case 與 root 正確 | 兩種 case 正確 | 遺失 subtree |
| Validation | 使用全域 boundary | 只檢查部分 | 無法偵測違規 |
| Edge cases | Empty、single、skewed、missing 均測試 | 部分測試 | 只測完整 tree |
| 程式完整性 | 檔名正確且可編譯 | 少量錯誤 | 缺少主要 method |
| GitHub | `0825` 連結完整 | 命名缺漏 | 未 push |

## 參考教材

- [Algorithms, 4th Edition：Binary Search Trees](https://algs4.cs.princeton.edu/32bst/)
- [Algorithms, 4th Edition：BST slides](https://algs4.cs.princeton.edu/lectures/32BinarySearchTrees.pdf)
- [Java SE 17 API](https://docs.oracle.com/en/java/javase/17/docs/api/)
