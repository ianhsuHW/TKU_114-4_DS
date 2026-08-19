class Product {
    private String id;
    private String name;
    private int stock;

    Product(String id, String name, int stock) {
        this.id = id;
        this.name = name;
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

    int getStock() {
        return stock;
    }

    String summary() {
        return id + " " + name + " stock=" + stock;
    }
}

public class ProductEncapsulationDemo {
    public static void main(String[] args) {
        Product keyboard = new Product("P-100", "Mechanical Keyboard", 8);

        System.out.println(keyboard.summary());
        System.out.println("sell 3=" + keyboard.sell(3));
        System.out.println("sell 9=" + keyboard.sell(9));
        keyboard.restock(5);
        System.out.println(keyboard.summary());
    }
}
