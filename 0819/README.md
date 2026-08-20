# 8/19 課堂實作與作業

## 單元名稱

繼承、介面與多型

## 課程定位

本日把單一 class 的封裝觀念延伸到多個 class 的關係，建立「以 supertype 撰寫程式」的習慣，後續集合框架與資料結構的替換能力都建立在這個基礎上。

## 學習目標

1. 使用繼承共用實作，並以 `super(...)` 建立 constructor chain。
2. 以 override 改變 subclass 的行為。
3. 用 interface 表達「能做什麼」，用繼承表達「是什麼」。
4. 以 polymorphism 讓主程式不依賴具體型態。

完整說明請見 [`02_0819_繼承介面與多型.md`](../02_0819_繼承介面與多型.md)。

## 核心概念範例（12 個）

| 檔案 | 對應概念 |
|---|---|
| `InheritanceBasics.java` | 概念 1：Inheritance 表達 is-a 關係 |
| `OverridePayDemo.java` | 概念 2：Method overriding 與 `@Override` |
| `PolymorphismArrayDemo.java` | 概念 3：Polymorphism 與 dynamic dispatch |
| `AbstractNotificationDemo.java` | 概念 4：Abstract class 定義共同骨架 |
| `PaymentInterfaceDemo.java` | 概念 5：Interface 定義可替換的行為契約 |
| `MultipleInterfaceDemo.java` | 概念 6：一個 class 可以實作多個 interface |
| `StrategyCompositionDemo.java` | 概念 7：Abstract class、interface 與 composition 的選擇 |
| `ConstructorChainDemo.java` | 概念 8：`super` 與 constructor chain |
| `SafeCastingDemo.java` | 概念 9：Upcasting、downcasting 與 `instanceof` |
| `DefaultMethodDemo.java` | 概念 10：Interface default method |
| `PolymorphicFactoryDemo.java` | 概念 11：Polymorphic parameter 與 return type |
| `CheckoutNotificationSystem.java` | 概念 12：綜合應用，多管道通知與費用計算 |

## 課堂實作題（5 題）

| 檔案 | 題目 |
|---|---|
| `TransportFareSystem.java` | 一：交通票價多型系統 |
| `MessageSenderSystem.java` | 二：訊息發送 Interface |
| `DocumentCapabilityDemo.java` | 三：匯出與壓縮能力 |
| `EmployeeConstructorChain.java` | 四：建構鏈與員工類型 |
| `DeviceInspectionSystem.java` | 五：安全型態判斷 |

## 課後作業（5 題）

| 檔案 | 題目 |
|---|---|
| `PayrollPolymorphismSystem.java` | 一：員工薪資與獎金系統 |
| `DeliveryStrategySystem.java` | 二：多方式配送系統 |
| `MediaProcessingSystem.java` | 三：媒體檔案處理 |
| `ReportExporterFactory.java` | 四：報表輸出 Factory |
| `FlexibleCheckoutSystem.java` | 五：通知與費用系統擴充 |

> `FlexibleCheckoutSystem.java` 依題目要求延伸 `CheckoutNotificationSystem.java`，
> 直接重用該檔的 `PricingPolicy`、`NotificationChannel` 與既有實作，
> 只以新增 class 的方式擴充，因此兩個檔案需要一起編譯。

## 編譯與執行

整包編譯：

```bash
javac -encoding UTF-8 -d ../bin/0819 0819/*.java
java -cp ../bin/0819 InheritanceBasics
```

只編譯單一檔案：

```bash
javac -encoding UTF-8 -d ../bin/0819 0819/TransportFareSystem.java
java -cp ../bin/0819 TransportFareSystem
```
