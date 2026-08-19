interface PricingPolicy { int finalPrice(int originalPrice); }
class StandardPricing implements PricingPolicy { @Override public int finalPrice(int originalPrice){ return Math.max(0, originalPrice); } }
class VipPricing implements PricingPolicy { @Override public int finalPrice(int originalPrice){ return Math.max(0, originalPrice) * 85 / 100; } }
public class FlexibleCheckoutSystem {
    public static void main(String[] args) {
        PricingPolicy[] policies = { new StandardPricing(), new VipPricing() };
        for (PricingPolicy p : policies) System.out.println(p.finalPrice(2000));
    }
}
