abstract class EmployeeBase {
    private String id;
    private String name;

    EmployeeBase(String id, String name) {
        this.id = id;
        this.name = name;
        System.out.println("  EmployeeBase constructor 執行");
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    abstract int calculatePay();

    String label() {
        return id + " " + name;
    }
}

class FullTimeStaff extends EmployeeBase {
    private int monthlySalary;

    FullTimeStaff(String id, String name, int monthlySalary) {
        super(id, name);
        this.monthlySalary = Math.max(0, monthlySalary);
        System.out.println("  FullTimeStaff constructor 執行");
    }

    @Override
    int calculatePay() {
        return monthlySalary;
    }

    @Override
    public String toString() {
        return label() + " 正職 月薪=" + monthlySalary + " 實領=" + calculatePay();
    }
}

class PartTimeStaff extends EmployeeBase {
    private int hourlyRate;
    private int hours;

    PartTimeStaff(String id, String name, int hourlyRate, int hours) {
        super(id, name);
        this.hourlyRate = Math.max(0, hourlyRate);
        this.hours = Math.max(0, hours);
        System.out.println("  PartTimeStaff constructor 執行");
    }

    @Override
    int calculatePay() {
        return hourlyRate * hours;
    }

    @Override
    public String toString() {
        return label() + " 兼職 時薪=" + hourlyRate + " 時數=" + hours + " 實領=" + calculatePay();
    }
}

public class EmployeeConstructorChain {
    public static void main(String[] args) {
        System.out.println("=== 建立 FullTimeStaff ===");
        EmployeeBase amy = new FullTimeStaff("E001", "Amy", 52000);

        System.out.println();
        System.out.println("=== 建立 PartTimeStaff ===");
        EmployeeBase ben = new PartTimeStaff("E002", "Ben", 200, 80);

        System.out.println();
        System.out.println("=== 實際 constructor 執行順序 ===");
        System.out.println("1. new FullTimeStaff(...) 被呼叫");
        System.out.println("2. FullTimeStaff constructor 第一行 super(id, name) 先執行");
        System.out.println("3. EmployeeBase constructor 完成 id 與 name 的初始化");
        System.out.println("4. 回到 FullTimeStaff constructor，設定 monthlySalary");
        System.out.println("所以輸出順序一定是 EmployeeBase 先、subclass 後。");

        System.out.println();
        System.out.println("=== 薪資計算（由 subclass override）===");
        EmployeeBase[] staff = { amy, ben };
        int total = 0;
        for (EmployeeBase employee : staff) {
            System.out.println(employee);
            total += employee.calculatePay();
        }
        System.out.println("薪資總額：" + total);

        System.out.println();
        System.out.println("=== 邊界條件：負數轉為 0 ===");
        EmployeeBase badFullTime = new FullTimeStaff("E003", "Cindy", -50000);
        EmployeeBase badPartTime = new PartTimeStaff("E004", "Dora", -150, -20);
        System.out.println(badFullTime);
        System.out.println(badPartTime);
    }
}
