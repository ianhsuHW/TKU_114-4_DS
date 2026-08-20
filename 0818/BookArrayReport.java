class Book {
    private String isbn;
    private String title;
    private int price;
    private int stock;

    Book(String isbn, String title, int price, int stock) {
        this.isbn = isbn;
        this.title = title;
        this.price = Math.max(0, price);
        this.stock = Math.max(0, stock);
    }

    int getPrice() {
        return price;
    }

    int getStock() {
        return stock;
    }

    int stockValue() {
        return price * stock;
    }

    @Override
    public String toString() {
        return isbn + " 《" + title + "》 售價=" + price + " 庫存=" + stock;
    }
}

public class BookArrayReport {
    public static void main(String[] args) {
        Book[] books = {
            new Book("B001", "Java 物件導向", 520, 8),
            new Book("B002", "資料結構入門", 680, 3),
            new Book("B003", "演算法圖解", 450, 12),
            new Book("B004", "資料庫系統概論", 750, 2),
            new Book("B005", "作業系統", 600, 0)
        };

        System.out.println("=== 1. 所有書籍 ===");
        for (Book book : books) {
            System.out.println(book);
        }

        System.out.println();
        System.out.println("=== 2. 庫存總價值 ===");
        int totalValue = 0;
        for (Book book : books) {
            totalValue += book.stockValue();
        }
        System.out.println("庫存總價值：" + totalValue);

        System.out.println();
        System.out.println("=== 3. 價格最高的書 ===");
        Book highest = books[0];
        for (Book book : books) {
            if (book.getPrice() > highest.getPrice()) {
                highest = book;
            }
        }
        System.out.println(highest);

        System.out.println();
        System.out.println("=== 4. 庫存小於或等於 3 的書 ===");
        int lowStockCount = 0;
        for (Book book : books) {
            if (book.getStock() <= 3) {
                System.out.println(book);
                lowStockCount++;
            }
        }
        System.out.println("需要補貨的書共 " + lowStockCount + " 本");
    }
}
