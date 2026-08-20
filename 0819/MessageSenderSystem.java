interface MessageSender {
    boolean send(String receiver, String message);

    String channelName();
}

class EmailSender implements MessageSender {
    @Override
    public boolean send(String receiver, String message) {
        if (!receiver.contains("@")) {
            System.out.println("  EMAIL 失敗：收件者不是 email 格式 -> " + receiver);
            return false;
        }
        System.out.println("  EMAIL 寄送給 " + receiver + "：" + message);
        return true;
    }

    @Override
    public String channelName() {
        return "Email";
    }
}

class SmsSender implements MessageSender {
    @Override
    public boolean send(String receiver, String message) {
        if (message.length() > 20) {
            System.out.println("  SMS 失敗：簡訊超過 20 字 -> " + message.length() + " 字");
            return false;
        }
        System.out.println("  SMS 傳送給 " + receiver + "：" + message);
        return true;
    }

    @Override
    public String channelName() {
        return "SMS";
    }
}

class ConsoleSender implements MessageSender {
    @Override
    public boolean send(String receiver, String message) {
        System.out.println("  CONSOLE " + receiver + " -> " + message);
        return true;
    }

    @Override
    public String channelName() {
        return "Console";
    }
}

public class MessageSenderSystem {

    static boolean notify(MessageSender sender, String receiver, String message) {
        if (sender == null) {
            System.out.println("通知失敗：沒有指定 sender");
            return false;
        }
        if (receiver == null || receiver.isBlank()) {
            System.out.println("[" + sender.channelName() + "] 通知失敗：receiver 為空白");
            return false;
        }
        if (message == null || message.isBlank()) {
            System.out.println("[" + sender.channelName() + "] 通知失敗：message 為空白");
            return false;
        }
        System.out.println("[" + sender.channelName() + "] 準備發送");
        return sender.send(receiver, message);
    }

    public static void main(String[] args) {
        MessageSender[] senders = {
            new EmailSender(),
            new SmsSender(),
            new ConsoleSender()
        };

        System.out.println("=== 正常發送 ===");
        for (MessageSender sender : senders) {
            System.out.println("結果：" + notify(sender, "amy@example.com", "課程 9:10 開始"));
        }

        System.out.println();
        System.out.println("=== 空白 receiver ===");
        for (MessageSender sender : senders) {
            System.out.println("結果：" + notify(sender, "   ", "課程即將開始"));
        }

        System.out.println();
        System.out.println("=== 空白 message ===");
        System.out.println("結果：" + notify(senders[0], "amy@example.com", ""));
        System.out.println("結果：" + notify(senders[1], "0912345678", null));

        System.out.println();
        System.out.println("=== 各 sender 自己的驗證規則 ===");
        System.out.println("結果：" + notify(new EmailSender(), "0912345678", "格式不符的收件者"));
        System.out.println("結果：" + notify(new SmsSender(), "0912345678",
                "這是一則超過二十個字的很長很長的簡訊內容測試"));

        System.out.println();
        System.out.println("=== null sender ===");
        System.out.println("結果：" + notify(null, "amy@example.com", "測試"));

        System.out.println();
        System.out.println("notify() 只依賴 MessageSender interface，");
        System.out.println("新增 LineSender 之類的實作時完全不需要修改 notify()。");
    }
}
