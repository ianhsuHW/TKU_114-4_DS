// 課後作業五：圖書館藏索引
// 需求：Book 包含 isbn、title、author、available。以 isbn 作 key，
//       完成 add、find、borrow、returnBook、remove、range query
//       與 inorder report。借出中的書不得 remove。

import java.util.ArrayList;
import java.util.List;

class Book {
    final String isbn;
    final String title;
    final String author;
    boolean available;

    Book(String isbn, String title, String author) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.available = true;
    }

    @Override
    public String toString() {
        return isbn + " " + title + " / " + author
                + (available ? " [available]" : " [borrowed]");
    }
}

class BookNode {
    Book data;
    BookNode left;
    BookNode right;

    BookNode(Book data) {
        this.data = data;
    }
}

class LibraryIndex {
    private BookNode root;

    boolean add(Book book) {
        if (book == null || book.isbn == null || book.isbn.trim().isEmpty()) {
            return false;
        }
        if (root == null) {
            root = new BookNode(book);
            return true;
        }
        BookNode current = root;
        while (true) {
            int order = book.isbn.compareTo(current.data.isbn);
            if (order == 0) return false;               // isbn 不可重複
            if (order < 0) {
                if (current.left == null) {
                    current.left = new BookNode(book);
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = new BookNode(book);
                    return true;
                }
                current = current.right;
            }
        }
    }

    Book find(String isbn) {
        if (isbn == null) return null;
        BookNode current = root;
        while (current != null) {
            int order = isbn.compareTo(current.data.isbn);
            if (order == 0) return current.data;
            current = order < 0 ? current.left : current.right;
        }
        return null;
    }

    // 只有在架上的書可以借出
    boolean borrow(String isbn) {
        Book book = find(isbn);
        if (book == null || !book.available) return false;
        book.available = false;
        return true;
    }

    // 只有借出中的書可以歸還
    boolean returnBook(String isbn) {
        Book book = find(isbn);
        if (book == null || book.available) return false;
        book.available = true;
        return true;
    }

    // 借出中的書不得 remove
    boolean remove(String isbn) {
        Book book = find(isbn);
        if (book == null || !book.available) return false;
        root = remove(root, isbn);
        return true;
    }

    private BookNode remove(BookNode node, String isbn) {
        if (node == null) return null;
        int order = isbn.compareTo(node.data.isbn);
        if (order < 0) {
            node.left = remove(node.left, isbn);
        } else if (order > 0) {
            node.right = remove(node.right, isbn);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            BookNode successor = minimumNode(node.right);
            node.data = successor.data;
            node.right = remove(node.right, successor.data.isbn);
        }
        return node;
    }

    private BookNode minimumNode(BookNode node) {
        while (node.left != null) node = node.left;
        return node;
    }

    List<Book> isbnRange(String lowIsbn, String highIsbn) {
        List<Book> result = new ArrayList<>();
        if (lowIsbn == null || highIsbn == null) return result;
        if (lowIsbn.compareTo(highIsbn) > 0) return result;
        isbnRange(root, lowIsbn, highIsbn, result);
        return result;
    }

    private void isbnRange(BookNode node, String low, String high,
                           List<Book> result) {
        if (node == null) return;
        String isbn = node.data.isbn;
        if (low.compareTo(isbn) < 0) {
            isbnRange(node.left, low, high, result);
        }
        if (low.compareTo(isbn) <= 0 && isbn.compareTo(high) <= 0) {
            result.add(node.data);
        }
        if (isbn.compareTo(high) < 0) {
            isbnRange(node.right, low, high, result);
        }
    }

    List<Book> inorder() {
        List<Book> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(BookNode node, List<Book> result) {
        if (node == null) return;
        inorder(node.left, result);
        result.add(node.data);
        inorder(node.right, result);
    }

    void report(String title) {
        System.out.println("[" + title + "]");
        int availableCount = 0;
        List<Book> books = inorder();
        for (Book book : books) {
            System.out.println("  " + book);
            if (book.available) availableCount++;
        }
        System.out.println("  total=" + books.size()
                + " available=" + availableCount
                + " borrowed=" + (books.size() - availableCount));
        System.out.println();
    }
}

public class LibraryBookBst {
    public static void main(String[] args) {
        LibraryIndex library = new LibraryIndex();

        System.out.println("add 978-0300="
                + library.add(new Book("978-0300", "Algorithms", "Sedgewick")));
        System.out.println("add 978-0100="
                + library.add(new Book("978-0100", "Effective Java", "Bloch")));
        System.out.println("add 978-0500="
                + library.add(new Book("978-0500", "Clean Code", "Martin")));
        System.out.println("add 978-0200="
                + library.add(new Book("978-0200", "Java Concurrency", "Goetz")));
        System.out.println("add 978-0400="
                + library.add(new Book("978-0400", "Refactoring", "Fowler")));

        System.out.println("duplicate="
                + library.add(new Book("978-0100", "Copy", "Nobody")));
        System.out.println("blank isbn="
                + library.add(new Book("  ", "Blank", "Nobody")));
        System.out.println("null book=" + library.add(null));

        library.report("after add");

        System.out.println("find(978-0400)=" + library.find("978-0400"));
        System.out.println("find(978-9999)=" + library.find("978-9999"));

        System.out.println("borrow(978-0300)=" + library.borrow("978-0300"));
        System.out.println("borrow again=" + library.borrow("978-0300"));
        System.out.println("borrow missing=" + library.borrow("978-9999"));

        System.out.println("remove borrowed 978-0300="
                + library.remove("978-0300"));
        System.out.println("returnBook(978-0300)="
                + library.returnBook("978-0300"));
        System.out.println("returnBook again="
                + library.returnBook("978-0300"));

        library.report("after borrow and return");

        System.out.println("isbnRange(978-0200, 978-0400)=");
        for (Book book : library.isbnRange("978-0200", "978-0400")) {
            System.out.println("  " + book);
        }
        System.out.println("isbnRange(978-0400, 978-0200)="
                + library.isbnRange("978-0400", "978-0200"));

        System.out.println("remove available 978-0200="
                + library.remove("978-0200"));
        System.out.println("remove missing=" + library.remove("978-9999"));

        library.report("after remove");
    }
}
