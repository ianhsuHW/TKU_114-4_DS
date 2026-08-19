class PayrollEmployee { private String name; private int basePay; PayrollEmployee(String name, int basePay){ this.name = name; this.basePay = Math.max(0, basePay); } int calculatePay(){ return basePay; } String getName(){ return name; } }
class CommissionEmployee extends PayrollEmployee { private int sales; private int rate; CommissionEmployee(String name, int basePay, int sales, int rate){ super(name, basePay); this.sales = sales; this.rate = rate; } @Override int calculatePay(){ return super.calculatePay() + sales * rate / 100; } }
public class PayrollPolymorphismSystem {
    public static void main(String[] args) {
        PayrollEmployee[] employees = { new PayrollEmployee("Amy", 45000), new CommissionEmployee("Ben", 20000, 300000, 10) };
        for (PayrollEmployee employee : employees) System.out.println(employee.getName() + "=" + employee.calculatePay());
    }
}
