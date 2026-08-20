// 共用類別：由 OrderCompositionDemo.java（概念 6）與
// CustomerOrderSystem.java（課後作業二）共同使用，
// 因此獨立成一個檔案，避免同一個資料夾內重複宣告。
class Customer {
    private String id;
    private String name;
    private String phone;

    Customer(String id, String name) {
        this(id, name, "");
    }

    Customer(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone == null ? "" : phone;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    String getPhone() {
        return phone;
    }

    String label() {
        return id + " " + name;
    }

    @Override
    public String toString() {
        return phone.isEmpty() ? label() : label() + " (" + phone + ")";
    }
}
