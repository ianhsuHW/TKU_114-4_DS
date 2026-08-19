abstract class Media { private String title; Media(String title){ this.title = title; } String getTitle(){ return title; } abstract void play(); }
class Video extends Media { Video(String title){ super(title); } @Override void play(){ System.out.println("Playing video " + getTitle()); } }
class Audio extends Media { Audio(String title){ super(title); } @Override void play(){ System.out.println("Playing audio " + getTitle()); } }
public class MediaProcessingSystem {
    public static void main(String[] args) { Media[] media = { new Video("Tree"), new Audio("Lecture") }; for (Media m : media) m.play(); }
}
