// 第 4 題：Interface 通知路由
// 重點：route() 只依賴 Channel interface，新增管道不必改 route()。

import java.util.ArrayList;
import java.util.List;

public class Q04_NotificationRouter {

    public interface Channel {
        String name();
        boolean supports(String destination);
        String send(String destination, String message);
    }

    public static class EmailChannel implements Channel {
        @Override
        public String name() {
            return "EMAIL";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) return false;
            int at = destination.indexOf('@');
            return at > 0 && at < destination.length() - 1;   // @ 不能在開頭或結尾
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static class SmsChannel implements Channel {
        @Override
        public String name() {
            return "SMS";
        }

        @Override
        public boolean supports(String destination) {
            if (destination == null) return false;
            String digits = destination.replace("-", "");
            if (digits.length() != 10) return false;
            for (int i = 0; i < digits.length(); i++) {
                if (!Character.isDigit(digits.charAt(i))) return false;
            }
            return true;
        }

        @Override
        public String send(String destination, String message) {
            return name() + "|" + destination + "|" + message;
        }
    }

    public static List<String> route(List<Channel> channels, String destination, String message) {
        List<String> result = new ArrayList<>();
        if (channels == null || destination == null || message == null) {
            return result;
        }
        for (Channel channel : channels) {
            if (channel == null) continue;                    // 略過 null channel
            if (channel.supports(destination)) {
                result.add(channel.send(destination, message));
            }
        }
        return result;
    }

    public static void main(String[] args) {
        var channels = java.util.List.of(
                new Q04_NotificationRouter.EmailChannel(),
                new Q04_NotificationRouter.SmsChannel()
        );
        System.out.println(Q04_NotificationRouter.route(channels, "a@b.com", "Ready"));
        System.out.println(Q04_NotificationRouter.route(channels, "0912-345-678", "Go"));

        System.out.println("--- 邊界測試 ---");
        System.out.println(route(channels, "@b.com", "Bad"));
        System.out.println(route(channels, "a@", "Bad"));
        System.out.println(route(channels, "0912-345-67a", "Bad"));
        System.out.println(route(channels, "091234567", "Bad"));
        System.out.println(route(null, "a@b.com", "Bad"));
        System.out.println(route(channels, null, "Bad"));
        System.out.println(route(channels, "a@b.com", null));

        List<Channel> withNull = new ArrayList<>();
        withNull.add(null);
        withNull.add(new EmailChannel());
        System.out.println(route(withNull, "a@b.com", "Ready"));
    }
}
