package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.idgeneration.SequentialIdGenerator;

@JsonTypeName("MP3 File")
public class MP3 extends MediaFile {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    public MP3() {
        super(MediaFileIdentifier.MP3);
        initializeId(sequentialIdGenerator);
    }

    public MP3(String path) {
        super(MediaFileIdentifier.MP3, path);
        initializeId(sequentialIdGenerator);
    }
}
