interface ReportExporter {
    String formatName();

    String export(String title, int[] values);
}

class CsvExporter implements ReportExporter {
    @Override
    public String formatName() {
        return "CSV";
    }

    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("title,").append(title).append("\n");
        sb.append("index,value");
        if (values == null) {
            return sb.append("\n(no data)").toString();
        }
        for (int i = 0; i < values.length; i++) {
            sb.append("\n").append(i).append(",").append(values[i]);
        }
        return sb.toString();
    }
}

class JsonExporter implements ReportExporter {
    @Override
    public String formatName() {
        return "JSON";
    }

    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"title\":\"").append(title).append("\",\"values\":[");
        if (values != null) {
            for (int i = 0; i < values.length; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(values[i]);
            }
        }
        return sb.append("]}").toString();
    }
}

class TextExporter implements ReportExporter {
    @Override
    public String formatName() {
        return "TEXT";
    }

    @Override
    public String export(String title, int[] values) {
        StringBuilder sb = new StringBuilder();
        sb.append("== ").append(title).append(" ==");
        if (values == null || values.length == 0) {
            return sb.append("\n（沒有資料）").toString();
        }
        int total = 0;
        for (int value : values) {
            sb.append("\n  ").append(value);
            total += value;
        }
        sb.append("\n  合計 ").append(total);
        return sb.toString();
    }
}

public class ReportExporterFactory {

    static ReportExporter createExporter(String format) {
        if (format == null) {
            return new TextExporter();
        }
        switch (format.toLowerCase()) {
            case "csv":
                return new CsvExporter();
            case "json":
                return new JsonExporter();
            default:
                return new TextExporter();
        }
    }

    static void exportReport(ReportExporter exporter, String title, int[] values) {
        if (exporter == null) {
            System.out.println("匯出失敗：沒有指定 exporter");
            return;
        }
        String safeTitle = (title == null || title.isBlank()) ? "未命名報表" : title;
        System.out.println("--- 格式：" + exporter.formatName() + " ---");
        System.out.println(exporter.export(safeTitle, values));
    }

    public static void main(String[] args) {
        int[] values = { 120, 340, 85, 610 };

        System.out.println("=== 支援的格式 ===");
        exportReport(createExporter("csv"), "八月銷售", values);
        System.out.println();
        exportReport(createExporter("json"), "八月銷售", values);
        System.out.println();
        exportReport(createExporter("text"), "八月銷售", values);

        System.out.println();
        System.out.println("=== 不支援的格式回傳 TextExporter ===");
        exportReport(createExporter("pdf"), "八月銷售", values);
        System.out.println();
        exportReport(createExporter("XML"), "八月銷售", values);
        System.out.println();
        exportReport(createExporter(null), "八月銷售", values);

        System.out.println();
        System.out.println("=== 大小寫不敏感 ===");
        System.out.println("createExporter(\"CSV\")：" + createExporter("CSV").formatName());
        System.out.println("createExporter(\"Json\")：" + createExporter("Json").formatName());

        System.out.println();
        System.out.println("=== values 為 null 不發生例外 ===");
        exportReport(createExporter("csv"), "空報表", null);
        System.out.println();
        exportReport(createExporter("json"), "空報表", null);
        System.out.println();
        exportReport(createExporter("text"), "空報表", null);

        System.out.println();
        System.out.println("=== 空陣列與空標題 ===");
        exportReport(createExporter("text"), "  ", new int[0]);

        System.out.println();
        System.out.println("exportReport() 只依賴 ReportExporter interface，");
        System.out.println("整個主流程沒有使用 instanceof 選擇輸出格式。");
    }
}
