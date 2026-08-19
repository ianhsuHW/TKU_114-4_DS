class Shape { String label() { return "Shape"; } }
class Circle extends Shape { @Override String label() { return "Circle"; } }
class Rectangle extends Shape { @Override String label() { return "Rectangle"; } }
public class PolymorphismArrayDemo {
    public static void main(String[] args) {
        Shape[] shapes = { new Circle(), new Rectangle(), new Circle() };
        for (Shape s : shapes) System.out.println(s.label());
    }
}
