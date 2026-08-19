class ScoreRecord {
    private String studentId;
    private int score;

    ScoreRecord(String studentId, int score) {
        this.studentId = studentId;
        this.score = Math.max(0, score);
    }

    void updateScore(int newScore) {
        score = Math.max(0, newScore);
    }

    int getScore() { return score; }
    String getStudentId() { return studentId; }
}

public class ObjectReferenceReview {
    public static void main(String[] args) {
        ScoreRecord record = new ScoreRecord("S101", 70);
        ScoreRecord copy = record;

        copy.updateScore(92);
        System.out.println(record.getStudentId() + " -> " + record.getScore());
        System.out.println("same reference=" + (record == copy));
    }
}
