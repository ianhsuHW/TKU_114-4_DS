class Instructor {
    private String id;
    private String name;

    Instructor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    String getId() {
        return id;
    }

    String getName() {
        return name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}

class Course {
    private String courseCode;
    private String title;
    private Instructor instructor;

    Course(String courseCode, String title, Instructor instructor) {
        this.courseCode = courseCode;
        this.title = title;
        this.instructor = instructor;
    }

    Instructor getInstructor() {
        return instructor;
    }

    String summary() {
        String teacher = instructor == null
                ? "尚未指派授課者"
                : instructor.getId() + " " + instructor.getName();
        return courseCode + " " + title + "，授課者：" + teacher;
    }
}

public class CourseComposition {
    public static void main(String[] args) {
        Instructor wang = new Instructor("T001", "王老師");
        Instructor lin = new Instructor("T002", "林老師");

        Course dataStructure = new Course("CS201", "資料結構", wang);
        Course algorithm = new Course("CS202", "演算法", wang);
        Course database = new Course("CS301", "資料庫系統", lin);
        Course unassigned = new Course("CS999", "尚未開課", null);

        System.out.println("=== 課程資訊 ===");
        System.out.println(dataStructure.summary());
        System.out.println(algorithm.summary());
        System.out.println(database.summary());
        System.out.println(unassigned.summary());

        System.out.println();
        System.out.println("=== 共用同一個 Instructor 物件 ===");
        System.out.println("兩門課的 instructor 是同一個物件："
                + (dataStructure.getInstructor() == algorithm.getInstructor()));
        System.out.println("與資料庫課程的 instructor 相同："
                + (dataStructure.getInstructor() == database.getInstructor()));

        System.out.println();
        System.out.println("=== Composition 的好處 ===");
        System.out.println("Course 沒有另外保存 instructorName，只保存 Instructor reference，");
        System.out.println("所以授課者資料只有一份，修改一次全部課程同步更新。");
    }
}
