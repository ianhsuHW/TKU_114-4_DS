// 課後作業一：圖書索引
// 需求：自行實作 Hash Table，支援新增、更新、搜尋、刪除、size、
//       load factor 與 bucket report。

import java.util.ArrayList;
import java.util.List;

public class BookIsbnHashTable {

    public record Book(String title, String author, int year) {
        public Book {
            if (title == null || title.isBlank()) throw new IllegalArgumentException("title");
            if (author == null || author.isBlank()) throw new IllegalArgumentException("author");
            if (year < 1400) throw new IllegalArgumentException("year");
        }

        @Override
        public String toString() {
            return title + "/" + author + "/" + year;
        }
    }

    private static final class BookEntry {
        final String isbn;
        Book book;

        BookEntry(String isbn, Book book) {
            this.isbn = isbn;
            this.book = book;
        }

        @Override
        public String toString() {
            return isbn + "=" + book.title();
        }
    }

    private final List<List<BookEntry>> buckets = new ArrayList<>();
    private int size;

    public BookIsbnHashTable(int bucketCount) {
        if (bucketCount <= 0) throw new IllegalArgumentException("bucketCount");
        for (int i = 0; i < bucketCount; i++) buckets.add(new ArrayList<>());
    }

    private List<BookEntry> chainOf(String isbn) {
        if (isbn == null || isbn.isBlank()) throw new IllegalArgumentException("isbn");
        return buckets.get(Math.floorMod(isbn.trim().hashCode(), buckets.size()));
    }

    // 新增與更新用同一個入口：chain 裡找得到就換掉 value，size 不變
    public Book put(String isbn, Book book) {
        if (book == null) throw new IllegalArgumentException("book");
        List<BookEntry> chain = chainOf(isbn);
        String key = isbn.trim();
        for (BookEntry entry : chain) {
            if (entry.isbn.equals(key)) {
                Book old = entry.book;
                entry.book = book;
                return old;
            }
        }
        chain.add(new BookEntry(key, book));
        size++;
        return null;
    }

    public Book get(String isbn) {
        for (BookEntry entry : chainOf(isbn)) {
            if (entry.isbn.equals(isbn.trim())) return entry.book;
        }
        return null;
    }

    public boolean contains(String isbn) {
        return get(isbn) != null;
    }

    public boolean remove(String isbn) {
        List<BookEntry> chain = chainOf(isbn);
        String key = isbn.trim();
        for (int i = 0; i < chain.size(); i++) {
            if (chain.get(i).isbn.equals(key)) {
                chain.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    public double loadFactor() {
        return (double) size / buckets.size();
    }

    public void bucketReport() {
        int used = 0;
        int longest = 0;
        System.out.printf("bucketCount=%d size=%d load=%.2f%n",
                buckets.size(), size, loadFactor());
        for (int i = 0; i < buckets.size(); i++) {
            List<BookEntry> chain = buckets.get(i);
            if (!chain.isEmpty()) used++;
            longest = Math.max(longest, chain.size());
            System.out.println("  " + i + " -> " + chain
                    + (chain.size() > 1 ? "  <- chain " + chain.size() : ""));
        }
        System.out.println("  usedBuckets=" + used + " longestChain=" + longest);
    }

    public static void main(String[] args) {
        BookIsbnHashTable library = new BookIsbnHashTable(7);

        System.out.println("[insert]");
        library.put("978-0135166307", new Book("Effective Java", "Bloch", 2018));
        library.put("978-0132350884", new Book("Clean Code", "Martin", 2008));
        library.put("978-0201633610", new Book("Design Patterns", "GoF", 1994));
        library.put("978-0262033848", new Book("Introduction to Algorithms", "CLRS", 2009));
        library.put("978-1449331818", new Book("Learning Java", "Niemeyer", 2013));
        System.out.println("size=" + library.size());

        System.out.println();
        System.out.println("[update]");
        Book old = library.put("978-0132350884", new Book("Clean Code 2e", "Martin", 2020));
        System.out.println("old=" + old);
        System.out.println("new=" + library.get("978-0132350884"));
        System.out.println("size=" + library.size() + " (更新不增加 size)");

        System.out.println();
        System.out.println("[search]");
        System.out.println("get=" + library.get("978-0201633610"));
        System.out.println("contains=" + library.contains("978-0262033848"));
        System.out.println("missing=" + library.get("000-0000000000"));

        System.out.println();
        library.bucketReport();

        System.out.println();
        System.out.println("[remove]");
        System.out.println("remove=" + library.remove("978-0201633610")
                + " size=" + library.size());
        System.out.println("remove again=" + library.remove("978-0201633610"));
        System.out.printf("load=%.2f%n", library.loadFactor());

        System.out.println();
        try {
            library.put("  ", new Book("X", "Y", 2020));
        } catch (IllegalArgumentException e) {
            System.out.println("blank isbn -> IllegalArgumentException: " + e.getMessage());
        }
    }
}
