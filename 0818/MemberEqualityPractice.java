class Member {
    private final String id;
    private String name;

    Member(String id, String name) { this.id = id; this.name = name; }
    String getId() { return id; }
    String getName() { return name; }
    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Member)) return false;
        Member m = (Member) other;
        return id.equals(m.id);
    }
    @Override public int hashCode() { return id.hashCode(); }
}

public class MemberEqualityPractice {
    public static void main(String[] args) {
        Member a = new Member("M001", "Amy");
        Member b = new Member("M001", "Amy Chen");
        System.out.println(a.equals(b));
        System.out.println(a == b);
    }
}
