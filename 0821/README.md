# 8/21 課堂實作與作業

## 單元名稱

List、Stack、Queue 與集合實作比較

## 課程定位

本日比較 `ArrayList`、`LinkedList`、`Deque` 與手寫 stack 的差異，並加強選擇資料結構的思維，而不是只記住 API。後半段自行實作 dynamic array、singly linked list 與 circular queue，銜接後續的資料結構課程。

## 學習目標

1. 使用 `List` interface 讓 implementation 可替換。
2. 比較 `ArrayList` 與 `LinkedList` 的操作特性。
3. 以 `Deque` 實作 stack 與 queue。
4. 自行實作固定容量 stack、dynamic array、linked list 與 circular queue。

完整說明請見 [`04_0821_List_Stack_Queue與集合實作比較.md`](../04_0821_List_Stack_Queue與集合實作比較.md)。

## 核心概念範例（12 個）

| 檔案 | 對應概念 |
|---|---|
| `ListPolymorphismDemo.java` | 概念 1：用 List interface 隔離 implementation |
| `ListOperationTrace.java` | 概念 2：ArrayList 與 LinkedList 的內部概念 |
| `DequeEndsDemo.java` | 概念 3：Deque 表示雙端操作 |
| `UndoStackDemo.java` | 概念 4：使用 Deque 實作 Stack |
| `ServiceQueueDemo.java` | 概念 5：使用 Deque 實作 Queue |
| `CustomStringStackDemo.java` | 概念 6：自行實作固定容量 Stack |
| `StackImplementationComparison.java` | 概念 7：自行實作與內建集合的比較 |
| `WorkflowCollectionsDemo.java` | 概念 8：綜合應用，多種集合組成工作流程 |
| `CustomDynamicArrayDemo.java` | 概念 9：自行實作 dynamic array 與擴容 |
| `SinglyLinkedListDemo.java` | 概念 10：自行實作 singly linked list |
| `CircularArrayQueueDemo.java` | 概念 11：Circular array queue |
| `BracketMatchingDemo.java` | 概念 12：Stack 應用，括號配對 |

## 課堂實作題（6 題）

| 檔案 | 題目 |
|---|---|
| `ListImplementationLab.java` | 一：List Implementation 比較 |
| `BrowserBackStack.java` | 二：瀏覽器返回功能 |
| `CounterWaitingQueue.java` | 三：櫃台等候 Queue |
| `GenericArrayStackDemo.java` | 四：固定容量 Generic Stack |
| `DynamicArrayPractice.java` | 五：Dynamic array 插入與刪除 |
| `CircularQueuePractice.java` | 六：Circular queue 狀態追蹤 |

## 課後作業（6 題）

| 檔案 | 題目 |
|---|---|
| `TextEditorHistory.java` | 一：文字編輯 Undo/Redo |
| `ClinicQueueSystem.java` | 二：診所掛號系統 |
| `DeliveryWorkflowSystem.java` | 三：物流工作流程 |
| `CollectionChoiceReport.java` | 四：集合選擇報告與實作 |
| `LinkedTaskListSystem.java` | 五：單向鏈結清單 |
| `ServiceCenterWorkflow.java` | 六：服務中心排隊與取消 |

## 編譯與執行

整包編譯：

```bash
javac -encoding UTF-8 -d ../bin/0821 0821/*.java
java -cp ../bin/0821 ListPolymorphismDemo
```

只編譯單一檔案：

```bash
javac -encoding UTF-8 -d ../bin/0821 0821/CircularQueuePractice.java
java -cp ../bin/0821 CircularQueuePractice
```
