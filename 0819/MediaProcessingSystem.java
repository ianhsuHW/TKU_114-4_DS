interface Playable {
    String play();

    int durationInSeconds();
}

interface Compressible {
    int compress();
}

abstract class MediaFile {
    private String fileName;
    private int sizeInKb;

    MediaFile(String fileName, int sizeInKb) {
        this.fileName = fileName;
        this.sizeInKb = Math.max(0, sizeInKb);
    }

    String getFileName() {
        return fileName;
    }

    int getSizeInKb() {
        return sizeInKb;
    }

    abstract String describe();
}

class ImageFile extends MediaFile implements Compressible {
    private int width;
    private int height;

    ImageFile(String fileName, int sizeInKb, int width, int height) {
        super(fileName, sizeInKb);
        this.width = Math.max(0, width);
        this.height = Math.max(0, height);
    }

    @Override
    String describe() {
        return "圖片 " + getFileName() + " " + width + "x" + height
                + " " + getSizeInKb() + " KB";
    }

    @Override
    public int compress() {
        return getSizeInKb() * 25 / 100;
    }
}

class AudioFile extends MediaFile implements Playable, Compressible {
    private int seconds;

    AudioFile(String fileName, int sizeInKb, int seconds) {
        super(fileName, sizeInKb);
        this.seconds = Math.max(0, seconds);
    }

    @Override
    String describe() {
        return "音訊 " + getFileName() + " 長度 " + seconds + " 秒 "
                + getSizeInKb() + " KB";
    }

    @Override
    public String play() {
        return "播放音訊 " + getFileName();
    }

    @Override
    public int durationInSeconds() {
        return seconds;
    }

    @Override
    public int compress() {
        return getSizeInKb() * 50 / 100;
    }
}

class VideoFile extends MediaFile implements Playable, Compressible {
    private int seconds;
    private String resolution;

    VideoFile(String fileName, int sizeInKb, int seconds, String resolution) {
        super(fileName, sizeInKb);
        this.seconds = Math.max(0, seconds);
        this.resolution = resolution;
    }

    @Override
    String describe() {
        return "影片 " + getFileName() + " " + resolution + " 長度 "
                + seconds + " 秒 " + getSizeInKb() + " KB";
    }

    @Override
    public String play() {
        return "播放影片 " + getFileName() + "（" + resolution + "）";
    }

    @Override
    public int durationInSeconds() {
        return seconds;
    }

    @Override
    public int compress() {
        return getSizeInKb() * 35 / 100;
    }
}

public class MediaProcessingSystem {
    public static void main(String[] args) {
        MediaFile[] files = {
            new ImageFile("cover.png", 2400, 1920, 1080),
            new AudioFile("lecture.mp3", 15000, 3600),
            new VideoFile("demo.mp4", 240000, 900, "1080p"),
            new ImageFile("chart.jpg", 800, 1280, 720)
        };

        System.out.println("=== 全部檔案（abstract method describe）===");
        for (MediaFile file : files) {
            System.out.println(file.describe());
        }

        System.out.println();
        System.out.println("=== 每個物件支援的操作 ===");
        for (MediaFile file : files) {
            System.out.println(file.getFileName() + "：");
            if (file instanceof Playable playable) {
                System.out.println("  Playable -> " + playable.play()
                        + "，長度 " + playable.durationInSeconds() + " 秒");
            } else {
                System.out.println("  不支援 Playable");
            }
            if (file instanceof Compressible compressible) {
                System.out.println("  Compressible -> 壓縮後 " + compressible.compress()
                        + " KB（原始 " + file.getSizeInKb() + " KB）");
            } else {
                System.out.println("  不支援 Compressible");
            }
        }

        System.out.println();
        System.out.println("=== 只處理可播放的檔案 ===");
        int totalSeconds = 0;
        for (MediaFile file : files) {
            if (file instanceof Playable playable) {
                totalSeconds += playable.durationInSeconds();
            }
        }
        System.out.println("可播放內容總長度：" + totalSeconds + " 秒");

        System.out.println();
        System.out.println("=== 全部壓縮後的空間節省 ===");
        int before = 0;
        int after = 0;
        for (MediaFile file : files) {
            before += file.getSizeInKb();
            after += (file instanceof Compressible c) ? c.compress() : file.getSizeInKb();
        }
        System.out.println("壓縮前 " + before + " KB，壓縮後 " + after + " KB，"
                + "節省 " + (before - after) + " KB");

        System.out.println();
        System.out.println("繼承表達「是什麼」（ImageFile is a MediaFile），");
        System.out.println("interface 表達「能做什麼」（AudioFile 可播放也可壓縮）。");
    }
}
