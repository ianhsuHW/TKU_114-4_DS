interface DocExportable {
    String export();
}

interface DocCompressible {
    int compress();

    int originalSize();
}

class BackupDocument implements DocExportable, DocCompressible {
    private String title;
    private int sizeInKb;

    BackupDocument(String title, int sizeInKb) {
        this.title = title;
        this.sizeInKb = Math.max(0, sizeInKb);
    }

    @Override
    public String export() {
        return title + ".bak (" + sizeInKb + " KB)";
    }

    @Override
    public int compress() {
        return sizeInKb * 40 / 100;
    }

    @Override
    public int originalSize() {
        return sizeInKb;
    }

    void describe() {
        System.out.println("BackupDocument：" + title + "，原始大小 " + sizeInKb + " KB");
    }
}

public class DocumentCapabilityDemo {
    public static void main(String[] args) {
        BackupDocument document = new BackupDocument("2026-08-21-月結", 2500);
        document.describe();

        DocExportable asExportable = document;
        DocCompressible asCompressible = document;

        System.out.println();
        System.out.println("=== 以 DocExportable reference 呼叫 ===");
        System.out.println("export()：" + asExportable.export());

        System.out.println();
        System.out.println("=== 以 DocCompressible reference 呼叫 ===");
        System.out.println("originalSize()：" + asCompressible.originalSize() + " KB");
        System.out.println("compress()：" + asCompressible.compress() + " KB");

        System.out.println();
        System.out.println("=== 兩個 reference 指向同一個物件 ===");
        System.out.println("asExportable == asCompressible：" + (asExportable == asCompressible));
        System.out.println("asExportable == document：" + (asExportable == document));

        System.out.println();
        System.out.println("=== 可見的 method 不同 ===");
        System.out.println("DocExportable reference 只看得到 export()。");
        System.out.println("DocCompressible reference 只看得到 compress() 與 originalSize()。");
        System.out.println("要同時使用兩組能力，必須用 BackupDocument 型別，或轉型回另一個 interface：");
        System.out.println("轉型後 export()：" + ((DocExportable) asCompressible).export());
        System.out.println("轉型後 compress()：" + ((DocCompressible) asExportable).compress());

        System.out.println();
        System.out.println("結論：物件只有一個，reference 型別決定 compiler 允許呼叫哪些 method。");
    }
}
