package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.idgeneration.SequentialIdGenerator;

@JsonTypeName("MP3 File")
public class SoundFile extends MediaFile {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    public SoundFile() {
        super(MediaFileIdentifier.SOUND_FILE);
        initializeId(sequentialIdGenerator);
    }

    public SoundFile(String path) {
        super(MediaFileIdentifier.SOUND_FILE, path);
        initializeId(sequentialIdGenerator);
    }
}
