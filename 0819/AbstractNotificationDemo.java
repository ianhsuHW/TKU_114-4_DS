abstract class Notification {
    private String receiver;
    Notification(String receiver) { this.receiver = receiver; }
    String getReceiver() { return receiver; }
    abstract void send(String message);
}
class EmailNotification extends Notification {
    EmailNotification(String receiver) { super(receiver); }
    @Override void send(String message) { System.out.println("Email to " + getReceiver() + ": " + message); }
}
class SmsNotification extends Notification {
    SmsNotification(String receiver) { super(receiver); }
    @Override void send(String message) { System.out.println("SMS to " + getReceiver() + ": " + message); }
}
public class AbstractNotificationDemo {
    public static void main(String[] args) {
        Notification[] notifications = { new EmailNotification("amy@example.com"), new SmsNotification("0912345678") };
        for (Notification n : notifications) n.send("Class starts at 10:10");
    }
}
