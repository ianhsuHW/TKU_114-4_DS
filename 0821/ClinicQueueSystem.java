import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

class Patient {
    private String chartNo;
    private String name;

    Patient(String chartNo, String name) {
        this.chartNo = chartNo;
        this.name = name;
    }

    String getChartNo() {
        return chartNo;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return chartNo + " " + name;
    }
}

class Clinic {
    private Deque<Patient> waiting = new ArrayDeque<>();
    private List<Patient> finished = new ArrayList<>();

    boolean register(Patient patient) {
        if (patient == null) {
            System.out.println("掛號失敗：病人資料為 null");
            return false;
        }
        if (findWaiting(patient.getChartNo()) != null) {
            System.out.println("掛號失敗，病歷號重複：" + patient.getChartNo());
            return false;
        }
        waiting.offerLast(patient);
        System.out.println("掛號成功：" + patient + "，等候數 " + waiting.size());
        return true;
    }

    boolean cancel(String chartNo) {
        Iterator<Patient> it = waiting.iterator();
        while (it.hasNext()) {
            Patient p = it.next();
            if (p.getChartNo().equals(chartNo)) {
                it.remove();
                System.out.println("取消掛號：" + p + "，等候數 " + waiting.size());
                return true;
            }
        }
        System.out.println("取消失敗，等候中查無病歷號：" + chartNo);
        return false;
    }

    Patient callNext() {
        Patient next = waiting.pollFirst();
        if (next == null) {
            System.out.println("叫號失敗：目前沒有等候的病人");
            return null;
        }
        finished.add(next);
        System.out.println("叫號看診：" + next + "，剩餘等候數 " + waiting.size());
        return next;
    }

    Patient peekNext() {
        Patient next = waiting.peekFirst();
        System.out.println("下一位：" + (next == null ? "(無)" : next));
        return next;
    }

    private Patient findWaiting(String chartNo) {
        for (Patient p : waiting) {
            if (p.getChartNo().equals(chartNo)) {
                return p;
            }
        }
        return null;
    }

    void printWaiting() {
        System.out.println("等候名單：" + waiting);
    }

    void printFinished() {
        System.out.println("當日完成清單（共 " + finished.size() + " 位）：");
        for (Patient p : finished) {
            System.out.println("  " + p);
        }
    }
}

public class ClinicQueueSystem {
    public static void main(String[] args) {
        Clinic clinic = new Clinic();

        System.out.println("=== 空隊列 ===");
        clinic.peekNext();
        clinic.callNext();
        clinic.cancel("P001");

        System.out.println();
        System.out.println("=== 掛號 ===");
        clinic.register(new Patient("P001", "Amy"));
        clinic.register(new Patient("P002", "Ben"));
        clinic.register(new Patient("P003", "Cindy"));
        clinic.register(new Patient("P004", "Dora"));
        clinic.register(new Patient("P002", "Ben2"));
        clinic.printWaiting();

        System.out.println();
        System.out.println("=== 取消 ===");
        clinic.cancel("P003");
        clinic.cancel("P999");
        clinic.printWaiting();

        System.out.println();
        System.out.println("=== 叫號（維持 FIFO）===");
        clinic.peekNext();
        clinic.callNext();
        clinic.callNext();
        clinic.printWaiting();
        clinic.callNext();
        clinic.callNext();

        System.out.println();
        clinic.printFinished();
    }
}
