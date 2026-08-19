interface FarePolicy { int calculate(int distance); }
class RegularFare implements FarePolicy { @Override public int calculate(int distance) { return distance * 30; } }
class StudentFare implements FarePolicy { @Override public int calculate(int distance) { return distance * 20; } }
public class TransportFareSystem {
    public static void main(String[] args) {
        FarePolicy policy = new StudentFare();
        System.out.println("Fare=" + policy.calculate(10));
    }
}
