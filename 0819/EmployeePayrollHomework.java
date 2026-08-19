class PayrollEmployee {
    private String name;
    private int basePay;

    PayrollEmployee(String name, int basePay) {
        this.name = name;
        this.basePay = Math.max(0, basePay);
    }

    String getName() {
        return name;
    }

    int calculatePay() {
        return basePay;
    }
}

class CommissionEmployee extends PayrollEmployee {
    private int sales;
    private int rate;

    CommissionEmployee(String name, int basePay, int sales, int rate) {
        super(name, basePay);
        this.sales = Math.max(0, sales);
        this.rate = Math.max(0, rate);
    }

    @Override
    int calculatePay() {
        return super.calculatePay() + sales * rate / 100;
    }
}

public class EmployeePayrollHomework {
    public static void main(String[] args) {
        PayrollEmployee fullTime = new PayrollEmployee("Amy", 45000);
        PayrollEmployee commission = new CommissionEmployee("Ben", 20000, 300000, 10);

        System.out.println(fullTime.getName() + "：" + fullTime.calculatePay());
        System.out.println(commission.getName() + "：" + commission.calculatePay());
    }
}
