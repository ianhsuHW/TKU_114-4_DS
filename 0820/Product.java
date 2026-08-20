// 課後作業一 GenericRepositorySystem.java 用來測試 Repository<Product> 的資料類別，
// 獨立成一個檔案存放。
class Product {
    private final String id;
    private final String name;
    private final int price;

    Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = Math.max(0, price);
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Product)) {
            return false;
        }
        return id.equals(((Product) other).id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id + " " + name + " $" + price;
    }
}
