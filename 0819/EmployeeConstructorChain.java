abstract class EmployeeBase { private final String id; private final String name; EmployeeBase(String id, String name){ this.id=id; this.name=name; } String label(){ return id + " " + name; } }
class SalariedEmployee extends EmployeeBase { private final int salary; SalariedEmployee(String id, String name, int salary){ super(id, name); this.salary = Math.max(0, salary); } int monthlyPay(){ return salary; } }
public class EmployeeConstructorChain {
    public static void main(String[] args) { EmployeeBase e = new SalariedEmployee("E01", "Amy", 50000); System.out.println(e.label()); }
}
