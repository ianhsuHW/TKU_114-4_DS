interface DeliveryStrategy { int charge(int distance); }
class StandardDelivery implements DeliveryStrategy { @Override public int charge(int distance){ return distance * 40; } }
class ExpressDelivery implements DeliveryStrategy { @Override public int charge(int distance){ return distance * 70; } }
class DeliveryOrder { private DeliveryStrategy strategy; DeliveryOrder(DeliveryStrategy strategy){ this.strategy = strategy; } int fee(int distance){ return strategy.charge(distance); } }
public class DeliveryStrategySystem {
    public static void main(String[] args) {
        DeliveryOrder normal = new DeliveryOrder(new StandardDelivery());
        DeliveryOrder express = new DeliveryOrder(new ExpressDelivery());
        System.out.println("Normal=" + normal.fee(10));
        System.out.println("Express=" + express.fee(10));
    }
}
