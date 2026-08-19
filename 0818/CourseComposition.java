class Course { private String code; private String title; Course(String code, String title) { this.code = code; this.title = title; } String summary() { return code + " " + title; } }
class CourseSchedule { private Course course; private String day; CourseSchedule(Course course, String day) { this.course = course; this.day = day; } String summary(){ return course.summary() + " @ " + day; } }
public class CourseComposition {
    public static void main(String[] args) {
        Course course = new Course("CS101", "Java");
        CourseSchedule schedule = new CourseSchedule(course, "Tue");
        System.out.println(schedule.summary());
    }
}
