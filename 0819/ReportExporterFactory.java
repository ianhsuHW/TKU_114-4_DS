interface FeePolicy { int calculate(int amount); }
class RegularFee implements FeePolicy { @Override public int calculate(int amount){ return Math.max(0, amount); } }
class MemberFee implements FeePolicy { @Override public int calculate(int amount){ return Math.max(0, amount) * 90 / 100; } }
public class ReportExporterFactory {
    static FeePolicy createPolicy(String type) { return "member".equalsIgnoreCase(type) ? new MemberFee() : new RegularFee(); }
    public static void main(String[] args) { FeePolicy p = createPolicy("member"); System.out.println(p.calculate(1000)); }
}
