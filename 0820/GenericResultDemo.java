class Result<T> { private T value; void set(T value){ this.value = value; } T get(){ return value; } }
public class GenericResultDemo { public static void main(String[] args) { Result<String> name = new Result<>(); name.set("Amy"); Result<Integer> score = new Result<>(); score.set(90); System.out.println(name.get()); System.out.println(score.get()); } }
