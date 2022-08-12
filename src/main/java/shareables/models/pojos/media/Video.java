package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.idgeneration.SequentialIdGenerator;

@JsonTypeName("Video")
public class Video extends MediaFile {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    public Video() {
        super(MediaFileIdentifier.VIDEO);
        initializeId(sequentialIdGenerator);
    }

    public Video(String path) {
        super(MediaFileIdentifier.VIDEO, path);
        initializeId(sequentialIdGenerator);
    }
}
