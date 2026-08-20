abstract class Device {
    private String assetId;
    private String model;

    Device(String assetId, String model) {
        this.assetId = assetId;
        this.model = model;
    }

    String getAssetId() {
        return assetId;
    }

    String getModel() {
        return model;
    }

    abstract String runDiagnostic();
}

class Laptop extends Device {
    private int batteryHealth;

    Laptop(String assetId, String model, int batteryHealth) {
        super(assetId, model);
        this.batteryHealth = Math.max(0, Math.min(100, batteryHealth));
    }

    @Override
    String runDiagnostic() {
        String status = batteryHealth >= 80 ? "正常" : "電池需更換";
        return "筆電 " + getAssetId() + " " + getModel()
                + " 電池健康度 " + batteryHealth + "% -> " + status;
    }
}

class Printer extends Device {
    private int tonerLevel;
    private boolean headDirty;

    Printer(String assetId, String model, int tonerLevel, boolean headDirty) {
        super(assetId, model);
        this.tonerLevel = Math.max(0, Math.min(100, tonerLevel));
        this.headDirty = headDirty;
    }

    @Override
    String runDiagnostic() {
        String status = tonerLevel >= 20 ? "正常" : "碳粉不足";
        return "印表機 " + getAssetId() + " " + getModel()
                + " 碳粉 " + tonerLevel + "% -> " + status
                + "，噴頭" + (headDirty ? "需要清潔" : "乾淨");
    }

    String cleanPrintHead() {
        if (!headDirty) {
            return "  " + getAssetId() + " 噴頭已經是乾淨的，不需清潔";
        }
        headDirty = false;
        return "  " + getAssetId() + " 噴頭清潔完成";
    }
}

class Router extends Device {
    private int connectedClients;

    Router(String assetId, String model, int connectedClients) {
        super(assetId, model);
        this.connectedClients = Math.max(0, connectedClients);
    }

    @Override
    String runDiagnostic() {
        String status = connectedClients <= 50 ? "正常" : "連線數過高";
        return "路由器 " + getAssetId() + " " + getModel()
                + " 連線數 " + connectedClients + " -> " + status;
    }
}

public class DeviceInspectionSystem {
    public static void main(String[] args) {
        Device[] devices = {
            new Laptop("D-001", "ThinkPad X1", 92),
            new Printer("D-002", "LaserJet M404", 15, true),
            new Router("D-003", "RT-AX88U", 62),
            new Laptop("D-004", "MacBook Air", 71),
            new Printer("D-005", "EcoTank L3250", 88, false)
        };

        System.out.println("=== 全部設備執行 runDiagnostic()（polymorphism）===");
        for (Device device : devices) {
            System.out.println(device.runDiagnostic());
        }

        System.out.println();
        System.out.println("=== 只有 Printer 執行 cleanPrintHead()（pattern matching instanceof）===");
        for (Device device : devices) {
            if (device instanceof Printer printer) {
                System.out.println(printer.cleanPrintHead());
            }
        }

        System.out.println();
        System.out.println("=== 清潔後再次診斷 ===");
        for (Device device : devices) {
            System.out.println(device.runDiagnostic());
        }

        System.out.println();
        System.out.println("說明：runDiagnostic() 對所有型態都用同一行呼叫，不需要 cast。");
        System.out.println("只有 Printer 專屬的 cleanPrintHead() 才用 pattern matching instanceof，");
        System.out.println("寫法是 device instanceof Printer printer，判斷成功後直接使用 printer，");
        System.out.println("不需要再寫 (Printer) device 這種明確 cast。");
    }
}
