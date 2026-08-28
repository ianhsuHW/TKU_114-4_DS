// 第 3 題：繼承與薪資計算
// 重點：abstract method 由子類別實作，summary() 透過 polymorphism 取得實際薪資。

import java.util.ArrayList;
import java.util.List;

public class Q03_EmployeePayroll {

    public static abstract class Employee {
        private final String id;
        private final String name;

        protected Employee(String id, String name) {
            if (isBlank(id)) {
                throw new IllegalArgumentException("id 不可為 null 或空字串");
            }
            if (isBlank(name)) {
                throw new IllegalArgumentException("name 不可為 null 或空字串");
            }
            this.id = id.trim();
            this.name = name.trim();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public abstract int monthlyPay();

        public String summary() {
            return id + "|" + name + "|" + monthlyPay();   // 動態繫結到實際子類別
        }
    }

    public static class SalariedEmployee extends Employee {
        private final int salary;

        public SalariedEmployee(String id, String name, int salary) {
            super(id, name);
            this.salary = Math.max(0, salary);
        }

        @Override
        public int monthlyPay() {
            return salary;
        }
    }

    public static class HourlyEmployee extends Employee {
        private static final int NORMAL_HOURS = 160;

        private final int hours;
        private final int hourlyRate;

        public HourlyEmployee(String id, String name, int hours, int hourlyRate) {
            super(id, name);
            this.hours = Math.max(0, hours);
            this.hourlyRate = Math.max(0, hourlyRate);
        }

        @Override
        public int monthlyPay() {
            if (hours <= NORMAL_HOURS) {
                return hours * hourlyRate;
            }
            int overtime = hours - NORMAL_HOURS;
            double pay = NORMAL_HOURS * hourlyRate + overtime * hourlyRate * 1.5;
            return (int) pay;                              // 超過 160 小時的部分算 1.5 倍
        }
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static int totalPayroll(List<Employee> employees) {
        if (employees == null) return 0;
        int total = 0;
        for (Employee employee : employees) {
            if (employee == null) continue;
            total += employee.monthlyPay();
        }
        return total;
    }

    public static void main(String[] args) {
        var employees = java.util.List.of(
                new Q03_EmployeePayroll.SalariedEmployee("E1", "Amy", 50000),
                new Q03_EmployeePayroll.HourlyEmployee("E2", "Bo", 170, 200)
        );
        System.out.println(employees.get(0).summary());
        System.out.println(employees.get(1).summary());
        System.out.println(Q03_EmployeePayroll.totalPayroll(employees));

        System.out.println("--- 邊界測試 ---");
        System.out.println(new SalariedEmployee("E3", "Cindy", -100).summary());
        System.out.println(new HourlyEmployee("E4", "Dan", 160, 150).summary());
        System.out.println(new HourlyEmployee("E5", "Eve", -5, -5).summary());
        System.out.println(totalPayroll(null));

        List<Employee> withNull = new ArrayList<>();
        withNull.add(null);
        withNull.add(new SalariedEmployee("E6", "Fay", 1000));
        System.out.println(totalPayroll(withNull));

        try {
            new SalariedEmployee("", "NoId", 100);
        } catch (IllegalArgumentException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}
