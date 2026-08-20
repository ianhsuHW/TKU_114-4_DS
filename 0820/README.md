# 8/20 課堂實作與作業

## 單元名稱

泛型與 Java Collections Framework

## 課程定位

本日把「型態安全」與「標準集合」放在一起，讓資料結構的實作與使用都不再依賴 cast，也建立 List、Set、Map 的選擇思維。

## 學習目標

1. 使用 generic class 與 generic method 取得編譯期型態檢查。
2. 分辨 List、Set、Map 的資料語意與適用情境。
3. 以 Iterator 在走訪過程中安全刪除元素。
4. 使用 wildcard、`Comparable` 與 `Comparator` 處理彈性需求。

完整說明請見 [`03_0820_泛型與Java_Collections_Framework.md`](../03_0820_泛型與Java_Collections_Framework.md)。

## 核心概念範例（12 個）

| 檔案 | 對應概念 |
|---|---|
| `GenericBoxDemo.java` | 概念 1：為什麼需要 Generic |
| `GenericPairDemo.java` | 概念 2：Generic class 可以有多個 type parameter |
| `GenericMethodDemo.java` | 概念 3：Generic method |
| `BoundedGenericDemo.java` | 概念 4：Bounded type 限制可用型態 |
| `CollectionHierarchyDemo.java` | 概念 5：Collections Framework 的 interface 與 implementation |
| `ListSetMapDemo.java` | 概念 6：List、Set 與 Map 的資料語意 |
| `IteratorRemovalDemo.java` | 概念 7：Iterator 與走訪中安全刪除 |
| `RawTypeSafetyDemo.java` | 概念 8：Raw type 與 compile-time type safety |
| `WildcardPecsDemo.java` | 概念 9：Wildcard 與 PECS |
| `ComparableComparatorDemo.java` | 概念 10：`Comparable`、`Comparator` 與多種排序規則 |
| `HashSetEqualityDemo.java` | 概念 11：`equals`/`hashCode` 對 Set 的影響 |
| `CourseRegistrationCollectionsSystem.java` | 概念 12：綜合應用，課程報名、標籤與成績排序 |

## 課堂實作題（5 題）

| 檔案 | 題目 |
|---|---|
| `GenericResultDemo.java` | 一：Generic Result |
| `GenericArrayTools.java` | 二：Generic 陣列工具 |
| `CourseTagReport.java` | 三：課程標籤統計 |
| `WildcardNumberTools.java` | 四：Wildcard 數值工具 |
| `ProductComparatorPractice.java` | 五：多規則商品排序 |

## 課後作業（5 題）

| 檔案 | 題目 |
|---|---|
| `GenericRepositorySystem.java` | 一：Generic Repository |
| `WordIndexSystem.java` | 二：文字索引系統 |
| `EnrollmentCleanup.java` | 三：安全清理名單 |
| `EnrollmentSetSystem.java` | 四：課程報名身分集合 |
| `CourseCollectionManager.java` | 五：課程管理集合系統 |

## 編譯與執行

整包編譯：

```bash
javac -encoding UTF-8 -d ../bin/0820 0820/*.java
java -cp ../bin/0820 GenericBoxDemo
```

只編譯單一檔案：

```bash
javac -encoding UTF-8 -d ../bin/0820 0820/GenericResultDemo.java
java -cp ../bin/0820 GenericResultDemo
```
