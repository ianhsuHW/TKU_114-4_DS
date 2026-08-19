class Product {
    private String id;
    private String name;
    private int price;
    private int stock;

    Product(String id, String name, int price, int stock) {
        this.id = id;
        this.name = name;
        this.price = Math.max(0, price);
        this.stock = Math.max(0, stock);
    }

    boolean sell(int quantity) {
        if (quantity <= 0 || quantity > stock) {
            return false;
        }
        stock -= quantity;
        return true;
    }

    void restock(int quantity) {
        if (quantity > 0) {
            stock += quantity;
        }
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

    int getStock() {
        return stock;
    }
}

public class OrderSystemHomework {
    public static void main(String[] args) {
        Product keyboard = new Product("P-100", "Mechanical Keyboard", 1200, 8);

        System.out.println("商品：" + keyboard.getName());
        System.out.println("原始庫存：" + keyboard.getStock());
        System.out.println("販售 3 台：" + keyboard.sell(3));
        System.out.println("販售 10 台：" + keyboard.sell(10));
        keyboard.restock(5);
        System.out.println("補貨後庫存：" + keyboard.getStock());
    }
}
