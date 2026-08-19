interface MessageSender { void send(String message); }
class EmailSender implements MessageSender { @Override public void send(String message){ System.out.println("Email: " + message); } }
class SmsSender implements MessageSender { @Override public void send(String message){ System.out.println("SMS: " + message); } }
public class MessageSenderSystem {
    public static void main(String[] args) {
        MessageSender[] senders = { new EmailSender(), new SmsSender() };
        for (MessageSender s : senders) s.send("Class starts");
    }
}
