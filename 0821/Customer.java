// 課堂實作題三 CounterWaitingQueue.java 用來管理 Deque<Customer> 的資料類別，
// 獨立成一個檔案存放。
class Customer {
    private String id;
    private String name;

    Customer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
