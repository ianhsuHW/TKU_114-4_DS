class LibraryMember {
    private String memberId;
    private String name;
    private String email;

    LibraryMember(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
    }

    String getMemberId() {
        return memberId;
    }

    @Override
    public String toString() {
        return "memberId=" + memberId + " name=" + name + " email=" + email;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof LibraryMember)) {
            return false;
        }
        LibraryMember another = (LibraryMember) other;
        return memberId.equals(another.memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }
}

public class MemberEqualityPractice {
    public static void main(String[] args) {
        LibraryMember first = new LibraryMember("M001", "Amy", "amy@mail.com");
        LibraryMember second = new LibraryMember("M001", "Amy Chen", "amy.chen@mail.com");
        LibraryMember third = new LibraryMember("M002", "Ben", "ben@mail.com");
        LibraryMember alias = first;

        System.out.println("=== 1. toString ===");
        System.out.println(first);
        System.out.println(second);
        System.out.println(third);

        System.out.println();
        System.out.println("=== 2. 同 id 不同 email 的比較 ===");
        System.out.println("first == second：" + (first == second));
        System.out.println("first.equals(second)：" + first.equals(second));
        System.out.println("hashCode 相同：" + (first.hashCode() == second.hashCode()));

        System.out.println();
        System.out.println("=== 3. 不同 id 的比較 ===");
        System.out.println("first == third：" + (first == third));
        System.out.println("first.equals(third)：" + first.equals(third));

        System.out.println();
        System.out.println("=== 4. alias 比較 ===");
        System.out.println("first == alias：" + (first == alias));
        System.out.println("first.equals(alias)：" + first.equals(alias));

        System.out.println();
        System.out.println("=== 5. 邊界條件 ===");
        System.out.println("first.equals(null)：" + first.equals(null));
        Object notAMember = "M001";
        System.out.println("first.equals(字串 M001)：" + first.equals(notAMember));

        System.out.println();
        System.out.println("結論：== 比較 reference 是否指向同一個物件，");
        System.out.println("equals() 依照 memberId 判斷是否為同一位會員。");
    }
}
