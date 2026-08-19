interface Printable { void print(); }
interface Exportable { String export(); }
class Report implements Printable, Exportable {
    private String title;
    Report(String title){ this.title = title; }
    @Override public void print(){ System.out.println(title); }
    @Override public String export(){ return title + " exported"; }
}
public class DocumentCapabilityDemo {
    public static void main(String[] args) {
        Report r = new Report("Summary");
        Printable p = r; Exportable e = r; p.print(); System.out.println(e.export());
    }
}
