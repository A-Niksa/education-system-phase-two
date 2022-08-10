package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Video")
public class Video extends MediaFile {
    public Video() {
        super(MediaFileIdentifier.VIDEO);
    }

    public Video(String path) {
        super(MediaFileIdentifier.VIDEO, path);
    }
}
