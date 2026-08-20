interface DeliveryMethod {
    int calculateFee(int weightInGram, int orderAmount);

    String estimateDescription();

    String methodName();
}

class HomeDelivery implements DeliveryMethod {
    @Override
    public int calculateFee(int weightInGram, int orderAmount) {
        if (orderAmount >= 1500) {
            return 0;
        }
        int weight = Math.max(0, weightInGram);
        return weight <= 5000 ? 120 : 120 + (weight - 5000) / 1000 * 20;
    }

    @Override
    public String estimateDescription() {
        return "宅配到府，預計 2 至 3 個工作天送達，滿 1500 元免運";
    }

    @Override
    public String methodName() {
        return "宅配";
    }
}

class StorePickup implements DeliveryMethod {
    @Override
    public int calculateFee(int weightInGram, int orderAmount) {
        if (weightInGram > 5000) {
            return -1;
        }
        return orderAmount >= 800 ? 0 : 60;
    }

    @Override
    public String estimateDescription() {
        return "超商取貨，預計 3 至 5 個工作天到店，滿 800 元免運，限重 5 公斤";
    }

    @Override
    public String methodName() {
        return "超商取貨";
    }
}

class SelfPickup implements DeliveryMethod {
    @Override
    public int calculateFee(int weightInGram, int orderAmount) {
        return 0;
    }

    @Override
    public String estimateDescription() {
        return "門市自取，備貨完成後當日即可取貨，不收運費";
    }

    @Override
    public String methodName() {
        return "自取";
    }
}

class OrderService {
    private String orderId;
    private DeliveryMethod deliveryMethod;

    OrderService(String orderId, DeliveryMethod deliveryMethod) {
        this.orderId = orderId;
        this.deliveryMethod = deliveryMethod;
    }

    void setDeliveryMethod(DeliveryMethod deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    void printQuotation(int weightInGram, int orderAmount) {
        if (deliveryMethod == null) {
            System.out.println(orderId + "：尚未選擇配送方式");
            return;
        }
        int fee = deliveryMethod.calculateFee(weightInGram, orderAmount);
        System.out.println(orderId + " 使用「" + deliveryMethod.methodName() + "」");
        System.out.println("  " + deliveryMethod.estimateDescription());
        if (fee < 0) {
            System.out.println("  無法配送：包裹超過此方式的限重");
            return;
        }
        System.out.println("  商品金額 " + orderAmount + " 元，重量 " + weightInGram + " 公克");
        System.out.println("  運費 " + fee + " 元，應付總額 " + (orderAmount + fee) + " 元");
    }
}

public class DeliveryStrategySystem {
    public static void main(String[] args) {
        DeliveryMethod[] methods = {
            new HomeDelivery(),
            new StorePickup(),
            new SelfPickup()
        };

        System.out.println("=== 小額輕量訂單（600 元，1200 公克）===");
        for (DeliveryMethod method : methods) {
            new OrderService("O-1001", method).printQuotation(1200, 600);
        }

        System.out.println();
        System.out.println("=== 大額訂單（2000 元，3000 公克）===");
        for (DeliveryMethod method : methods) {
            new OrderService("O-1002", method).printQuotation(3000, 2000);
        }

        System.out.println();
        System.out.println("=== 超重訂單（900 元，8000 公克）===");
        for (DeliveryMethod method : methods) {
            new OrderService("O-1003", method).printQuotation(8000, 900);
        }

        System.out.println();
        System.out.println("=== 同一張訂單中途更換配送方式 ===");
        OrderService order = new OrderService("O-1004", new HomeDelivery());
        order.printQuotation(2000, 1000);
        order.setDeliveryMethod(new SelfPickup());
        order.printQuotation(2000, 1000);

        System.out.println();
        System.out.println("=== 未設定配送方式 ===");
        new OrderService("O-1005", null).printQuotation(1000, 500);

        System.out.println();
        System.out.println("OrderService 以 composition 保存一個 DeliveryMethod，");
        System.out.println("新增配送方式只要再實作 interface，OrderService 完全不用修改。");
    }
}
