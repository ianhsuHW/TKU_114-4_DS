class SupportTicket {
    private static int nextId = 1001;
    private final int ticketId;
    private String title;

    SupportTicket(String title) {
        this.ticketId = nextId++;
        this.title = title == null || title.isBlank() ? "Untitled" : title;
    }

    int getTicketId() { return ticketId; }
    String getTitle() { return title; }
    static int getNextId() { return nextId; }
}

public class StaticMemberReview {
    public static void main(String[] args) {
        SupportTicket a = new SupportTicket("Login issue");
        SupportTicket b = new SupportTicket("Print error");
        System.out.println(a.getTicketId() + " " + a.getTitle());
        System.out.println(b.getTicketId() + " " + b.getTitle());
        System.out.println("Next id=" + SupportTicket.getNextId());
    }
}
