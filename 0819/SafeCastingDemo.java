abstract class MediaAsset {
    private final String title;

    MediaAsset(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    abstract void play();
}

class VideoAsset extends MediaAsset {
    private String resolution;

    VideoAsset(String title, String resolution) {
        super(title);
        this.resolution = resolution;
    }

    @Override
    void play() {
        System.out.println("Play video: " + getTitle() + " " + resolution);
    }

    void changeResolution(String resolution) {
        this.resolution = resolution;
    }
}

class AudioAsset extends MediaAsset {
    AudioAsset(String title) {
        super(title);
    }

    @Override
    void play() {
        System.out.println("Play audio: " + getTitle());
    }
}

public class SafeCastingDemo {
    static void prepare(MediaAsset asset) {
        if (asset instanceof VideoAsset video) {
            video.changeResolution("1080p");
        }
        asset.play();
    }

    public static void main(String[] args) {
        prepare(new VideoAsset("Tree Tutorial", "720p"));
        prepare(new AudioAsset("Queue Podcast"));
    }
}
