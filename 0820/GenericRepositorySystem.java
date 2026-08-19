class Repository<T> { private T value; void set(T value){ this.value = value; } T get(){ return value; } }
public class GenericRepositorySystem { public static void main(String[] args) { Repository<String> repo = new Repository<>(); repo.set("Course"); System.out.println(repo.get()); } }
