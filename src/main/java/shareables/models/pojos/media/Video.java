package shareables.models.pojos.media;

public class Video extends MediaFile {
    public Video(String path) {
        super(MediaFileIdentifier.VIDEO, path);
    }
}
