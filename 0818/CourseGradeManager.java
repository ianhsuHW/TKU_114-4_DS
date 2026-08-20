class CourseGrade {
    private String studentId;
    private String name;
    private int usualScore;
    private int midtermScore;
    private int finalExamScore;
    private int attendanceScore;

    CourseGrade(String studentId, String name, int usualScore,
                int midtermScore, int finalExamScore, int attendanceScore) {
        this.studentId = studentId;
        this.name = name;
        this.usualScore = clamp(usualScore);
        this.midtermScore = clamp(midtermScore);
        this.finalExamScore = clamp(finalExamScore);
        this.attendanceScore = clamp(attendanceScore);
    }

    private int clamp(int score) {
        if (score < 0) {
            return 0;
        }
        return Math.min(score, 100);
    }

    String getStudentId() {
        return studentId;
    }

    String getName() {
        return name;
    }

    int calculateFinalScore() {
        return (usualScore * 50 + midtermScore * 20
                + finalExamScore * 20 + attendanceScore * 10) / 100;
    }

    String getLevel() {
        int score = calculateFinalScore();
        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }

    boolean isFailed() {
        return calculateFinalScore() < 60;
    }

    @Override
    public String toString() {
        return studentId + " " + name
                + " 平時=" + usualScore
                + " 期中=" + midtermScore
                + " 期末=" + finalExamScore
                + " 出席=" + attendanceScore
                + " 總分=" + calculateFinalScore()
                + " 等第=" + getLevel();
    }
}

public class CourseGradeManager {
    public static void main(String[] args) {
        CourseGrade[] grades = {
            new CourseGrade("S101", "Amy", 92, 88, 95, 100),
            new CourseGrade("S102", "Ben", 70, 65, 58, 90),
            new CourseGrade("S103", "Cindy", 55, 48, 60, 80),
            new CourseGrade("S104", "Dora", 85, 90, 82, 100),
            new CourseGrade("S105", "Eric", 45, 120, -20, 70)
        };

        System.out.println("=== 成績明細 ===");
        for (CourseGrade grade : grades) {
            System.out.println(grade);
        }

        System.out.println();
        System.out.println("=== 統計 ===");
        int total = 0;
        CourseGrade highest = grades[0];
        for (CourseGrade grade : grades) {
            total += grade.calculateFinalScore();
            if (grade.calculateFinalScore() > highest.calculateFinalScore()) {
                highest = grade;
            }
        }
        System.out.println("平均總分：" + (total / grades.length));
        System.out.println("最高分：" + highest.getStudentId() + " " + highest.getName()
                + " " + highest.calculateFinalScore() + " 分");

        System.out.println();
        System.out.println("=== 不及格名單（總分低於 60）===");
        int failedCount = 0;
        for (CourseGrade grade : grades) {
            if (grade.isFailed()) {
                System.out.println("  " + grade.getStudentId() + " " + grade.getName()
                        + " " + grade.calculateFinalScore() + " 分 " + grade.getLevel());
                failedCount++;
            }
        }
        if (failedCount == 0) {
            System.out.println("  （無）");
        }
        System.out.println("不及格人數：" + failedCount);

        System.out.println();
        System.out.println("註：S105 輸入期中 120 與期末 -20，Constructor 已限制為 100 與 0。");
    }
}
