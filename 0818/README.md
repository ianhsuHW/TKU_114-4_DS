# 8/18 課堂實作與作業

## 單元名稱

Java 物件導向複習與封裝

## 課程定位

本日重點是整理 class、object、constructor、encapsulation 與 composition 的基礎思維。後續資料結構的每個節點、並列資料與操作流程，都依賴這些設計觀念。

## 學習目標

1. 使用 `private` 欄位控制資料存取。
2. 透過 constructor 建立合法物件狀態。
3. 使用 domain method 管理餘額與庫存。
4. 認識 object reference 與 composition。

完整說明請見 [`01_0818_Java物件導向複習與封裝.md`](../01_0818_Java物件導向複習與封裝.md)。

## 核心概念範例（12 個）

| 檔案 | 對應概念 |
|---|---|
| `BankAccountReview.java` | 概念 1：用 state 與 behavior 描述物件 |
| `ConstructorValidationDemo.java` | 概念 2：Constructor 建立合法初始狀態 |
| `ProductEncapsulationDemo.java` | 概念 3：Encapsulation 不等於只有 getter 與 setter |
| `StaticMemberReview.java` | 概念 4：Instance member 與 static member |
| `ObjectReferenceReview.java` | 概念 5：Object reference、alias 與 null |
| `OrderCompositionDemo.java` | 概念 6：Composition 表達 has-a 關係 |
| `ObjectArrayReview.java` | 概念 7：用物件陣列取代平行陣列 |
| `ConstructorOverloadingDemo.java` | 概念 8：`this` 與 constructor overloading |
| `ObjectParameterDemo.java` | 概念 9：Object reference 作為 method parameter |
| `DefensiveCopyDemo.java` | 概念 10：Immutable object 與 defensive copy |
| `ObjectEqualityDemo.java` | 概念 11：`toString`、`equals` 與物件身分 |
| `WalletTransactionSystem.java` | 概念 12：綜合應用，電子錢包與交易紀錄 |

## 課堂實作題（5 題）

| 檔案 | 題目 |
|---|---|
| `EquipmentInventory.java` | 一：設備庫存物件 |
| `CourseComposition.java` | 二：課程與授課者 Composition |
| `BookArrayReport.java` | 三：物件陣列統計 |
| `MemberEqualityPractice.java` | 四：會員身分比較 |
| `InventorySnapshotPractice.java` | 五：Immutable 庫存快照 |

## 課後作業（5 題）

| 檔案 | 題目 |
|---|---|
| `DigitalWalletSystem.java` | 一：封裝式電子錢包 |
| `CustomerOrderSystem.java` | 二：訂單與顧客管理 |
| `CourseGradeManager.java` | 三：課程成績物件系統 |
| `AccountTransferService.java` | 四：跨帳戶轉帳服務 |
| `WalletHistoryManager.java` | 五：電子錢包交易系統擴充 |

## 編譯與執行

整包編譯：

```bash
javac -encoding UTF-8 -d ../bin/0818 0818/*.java
java -cp ../bin/0818 BankAccountReview
```

只編譯單一檔案：

```bash
javac -encoding UTF-8 -d ../bin/0818 0818/EquipmentInventory.java
java -cp ../bin/0818 EquipmentInventory
```
