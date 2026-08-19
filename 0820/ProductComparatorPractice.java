import java.util.*;
class Product { private final String name; private final int price; Product(String name, int price){ this.name = name; this.price = price; } int getPrice(){ return price; } String getName(){ return name; } @Override public String toString(){ return name + "=" + price; } }
public class ProductComparatorPractice {
    public static void main(String[] args) {
        List<Product> products = new ArrayList<>(Arrays.asList(new Product("Mouse", 200), new Product("Keyboard", 800), new Product("Monitor", 1200)));
        products.sort(Comparator.comparingInt(Product::getPrice));
        System.out.println(products);
    }
}
