// 延伸 CheckoutNotificationSystem.java：
// 直接重用該檔已完成的 PricingPolicy、NotificationChannel、
// StandardPricing、VipPricing、EmailChannel、ConsoleChannel，
// 只以「新增 class」的方式擴充，不修改任何既有 class。

class BulkDiscountPricing implements PricingPolicy {
    @Override
    public int finalPrice(int originalPrice) {
        int price = Math.max(0, originalPrice);
        return price >= 2000 ? price - 300 : price;
    }
}

class SmsChannel implements NotificationChannel {
    @Override
    public boolean send(String receiver, String message) {
        if (receiver == null || receiver.length() != 10 || !receiver.startsWith("09")) {
            return false;
        }
        System.out.println("SMS " + receiver + " -> " + message);
        return true;
    }
}

class CheckoutResult {
    private final String orderId;
    private final int originalPrice;
    private final int finalPrice;
    private final boolean notified;
    private final String note;

    CheckoutResult(String orderId, int originalPrice, int finalPrice,
                   boolean notified, String note) {
        this.orderId = orderId;
        this.originalPrice = originalPrice;
        this.finalPrice = finalPrice;
        this.notified = notified;
        this.note = note;
    }

    String getOrderId() {
        return orderId;
    }

    int getFinalPrice() {
        return finalPrice;
    }

    boolean isNotified() {
        return notified;
    }

    int discount() {
        return originalPrice - finalPrice;
    }

    @Override
    public String toString() {
        return orderId + " 原價=" + originalPrice + " 實付=" + finalPrice
                + " 折抵=" + discount()
                + " 通知=" + (notified ? "成功" : "失敗")
                + "（" + note + "）";
    }
}

class FlexibleCheckoutService {
    private final PricingPolicy pricing;
    private final NotificationChannel channel;
    private final String policyName;
    private final String channelName;

    FlexibleCheckoutService(PricingPolicy pricing, String policyName,
                            NotificationChannel channel, String channelName) {
        this.pricing = pricing;
        this.channel = channel;
        this.policyName = policyName;
        this.channelName = channelName;
    }

    CheckoutResult checkout(String orderId, int originalPrice, String receiver) {
        if (orderId == null || orderId.isBlank()) {
            return new CheckoutResult("(無編號)", originalPrice, originalPrice,
                    false, "orderId 為空白");
        }
        if (originalPrice < 0) {
            return new CheckoutResult(orderId, originalPrice, 0, false, "金額不可為負數");
        }

        int amount = pricing.finalPrice(originalPrice);
        boolean sent = channel.send(receiver,
                "order=" + orderId + ", amount=" + amount);
        return new CheckoutResult(orderId, originalPrice, amount, sent,
                policyName + " + " + channelName);
    }
}

public class FlexibleCheckoutSystem {
    public static void main(String[] args) {
        FlexibleCheckoutService[] services = {
            new FlexibleCheckoutService(new StandardPricing(), "原價",
                    new EmailChannel(), "Email"),
            new FlexibleCheckoutService(new StandardPricing(), "原價",
                    new SmsChannel(), "SMS"),
            new FlexibleCheckoutService(new VipPricing(), "VIP 八五折",
                    new EmailChannel(), "Email"),
            new FlexibleCheckoutService(new VipPricing(), "VIP 八五折",
                    new ConsoleChannel(), "Console"),
            new FlexibleCheckoutService(new BulkDiscountPricing(), "滿 2000 折 300",
                    new SmsChannel(), "SMS"),
            new FlexibleCheckoutService(new BulkDiscountPricing(), "滿 2000 折 300",
                    new ConsoleChannel(), "Console")
        };

        String[] receivers = {
            "amy@example.com", "0912345678", "ben@example.com",
            "counter-1", "0987654321", "counter-2"
        };

        System.out.println("=== 六種 pricing / channel 組合（金額 2500）===");
        CheckoutResult[] results = new CheckoutResult[services.length];
        for (int i = 0; i < services.length; i++) {
            results[i] = services[i].checkout("O" + (100 + i), 2500, receivers[i]);
            System.out.println(results[i]);
        }

        System.out.println();
        System.out.println("=== 未達門檻的金額（1800）===");
        for (int i = 0; i < services.length; i++) {
            System.out.println(services[i].checkout("O" + (200 + i), 1800, receivers[i]));
        }

        System.out.println();
        System.out.println("=== 通知失敗但結帳金額仍算出 ===");
        FlexibleCheckoutService vipEmail = services[2];
        System.out.println(vipEmail.checkout("O301", 2000, "not-an-email"));
        FlexibleCheckoutService bulkSms = services[4];
        System.out.println(bulkSms.checkout("O302", 2000, "0912"));

        System.out.println();
        System.out.println("=== 不合法輸入 ===");
        System.out.println(vipEmail.checkout("", 1000, "amy@example.com"));
        System.out.println(vipEmail.checkout("O303", -500, "amy@example.com"));

        System.out.println();
        System.out.println("=== 以 CheckoutResult 做後續統計 ===");
        int totalPaid = 0;
        int notifiedCount = 0;
        for (CheckoutResult result : results) {
            totalPaid += result.getFinalPrice();
            if (result.isNotified()) {
                notifiedCount++;
            }
        }
        System.out.println("六筆訂單實付總額：" + totalPaid);
        System.out.println("通知成功筆數：" + notifiedCount + " / " + results.length);
        System.out.println("checkout() 回傳物件而不是 boolean，才做得到這種統計。");
    }
}
