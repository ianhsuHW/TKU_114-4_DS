class CourseGrade { private String studentId; private int score; CourseGrade(String studentId, int score) { this.studentId = studentId; this.score = Math.max(0, score); } boolean passed() { return score >= 60; } int getScore() { return score; } }
public class CourseGradeManager {
    public static void main(String[] args) {
        CourseGrade[] grades = { new CourseGrade("S101", 82), new CourseGrade("S102", 55), new CourseGrade("S103", 91) };
        int total = 0;
        for (CourseGrade g : grades) { total += g.getScore(); }
        System.out.println("Average=" + total / grades.length);
        for (CourseGrade g : grades) System.out.println(g.getScore() + " passed=" + g.passed());
    }
}
