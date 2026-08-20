abstract class PayrollEmployee {
    private String id;
    private String name;

    PayrollEmployee(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    abstract int calculatePay();

    abstract String employeeType();

    @Override
    public String toString() {
        return id + " " + name + " [" + employeeType() + "] 薪資=" + calculatePay();
    }
}

class MonthlySalaryEmployee extends PayrollEmployee {
    private int monthlySalary;

    MonthlySalaryEmployee(String id, String name, int monthlySalary) {
        super(id, name);
        this.monthlySalary = Math.max(0, monthlySalary);
    }

    @Override
    int calculatePay() {
        return monthlySalary;
    }

    @Override
    String employeeType() {
        return "月薪";
    }
}

class HourlyWageEmployee extends PayrollEmployee {
    private int hourlyRate;
    private int hours;

    HourlyWageEmployee(String id, String name, int hourlyRate, int hours) {
        super(id, name);
        this.hourlyRate = Math.max(0, hourlyRate);
        this.hours = Math.max(0, hours);
    }

    @Override
    int calculatePay() {
        if (hours <= 160) {
            return hourlyRate * hours;
        }
        int overtime = hours - 160;
        return hourlyRate * 160 + overtime * hourlyRate * 4 / 3;
    }

    @Override
    String employeeType() {
        return "時薪";
    }
}

class SalesCommissionEmployee extends PayrollEmployee {
    private int baseSalary;
    private int salesAmount;
    private int commissionRate;

    SalesCommissionEmployee(String id, String name, int baseSalary,
                            int salesAmount, int commissionRate) {
        super(id, name);
        this.baseSalary = Math.max(0, baseSalary);
        this.salesAmount = Math.max(0, salesAmount);
        this.commissionRate = Math.max(0, commissionRate);
    }

    @Override
    int calculatePay() {
        return baseSalary + salesAmount * commissionRate / 100;
    }

    @Override
    String employeeType() {
        return "業務";
    }
}

public class PayrollPolymorphismSystem {
    public static void main(String[] args) {
        PayrollEmployee[] employees = {
            new MonthlySalaryEmployee("E001", "Amy", 52000),
            new MonthlySalaryEmployee("E002", "Ben", 48000),
            new HourlyWageEmployee("E003", "Cindy", 220, 150),
            new HourlyWageEmployee("E004", "Dora", 220, 185),
            new SalesCommissionEmployee("E005", "Eric", 30000, 850000, 5),
            new SalesCommissionEmployee("E006", "Fiona", 30000, 0, 5)
        };

        System.out.println("=== 薪資明細 ===");
        for (PayrollEmployee employee : employees) {
            System.out.println(employee);
        }

        System.out.println();
        System.out.println("=== 統計 ===");
        int total = 0;
        PayrollEmployee highest = employees[0];
        for (PayrollEmployee employee : employees) {
            total += employee.calculatePay();
            if (employee.calculatePay() > highest.calculatePay()) {
                highest = employee;
            }
        }
        System.out.println("薪資總額：" + total);
        System.out.println("平均薪資：" + (total / employees.length));
        System.out.println("最高薪資：" + highest);

        System.out.println();
        System.out.println("=== 邊界條件：負數輸入 ===");
        PayrollEmployee[] invalid = {
            new MonthlySalaryEmployee("E007", "Gina", -52000),
            new HourlyWageEmployee("E008", "Henry", -220, -10),
            new SalesCommissionEmployee("E009", "Ivy", -30000, -500000, -5)
        };
        for (PayrollEmployee employee : invalid) {
            System.out.println(employee);
        }

        System.out.println();
        System.out.println("主程式只呼叫 calculatePay()，不需要知道對方是哪一種員工。");
    }
}
