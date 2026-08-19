class Book { private String title; private int pages; Book(String title, int pages) { this.title = title; this.pages = Math.max(0, pages); } int getPages(){ return pages; } String getTitle(){ return title; } }
public class BookArrayReport {
    public static void main(String[] args) {
        Book[] books = { new Book("Java", 300), new Book("DS", 420), new Book("Web", 210) };
        int total = 0;
        for (Book b : books) total += b.getPages();
        System.out.println("Total pages=" + total);
    }
}
