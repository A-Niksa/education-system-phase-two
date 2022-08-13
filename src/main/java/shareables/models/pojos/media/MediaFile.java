package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.idgeneration.SequentialIdGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SoundFile.class),
        @JsonSubTypes.Type(value = PDF.class),
        @JsonSubTypes.Type(value = Picture.class),
        @JsonSubTypes.Type(value = Video.class)
})
public abstract class MediaFile extends IdentifiableWithTime {
    private MediaFileIdentifier mediaFileIdentifier;
    private String path;
    private byte[] encodedBytes;

    public MediaFile() {
    }

    public MediaFile(MediaFileIdentifier mediaFileIdentifier) {
        this.mediaFileIdentifier = mediaFileIdentifier;
    }

    public MediaFile(MediaFileIdentifier mediaFileIdentifier, String path) {
        this.mediaFileIdentifier = mediaFileIdentifier;
        this.path = path;
        initializeEncodedBytes();
    }

    @Override
    protected void initializeId(SequentialIdGenerator sequentialIdGenerator) {
        id = idGenerator.nextId(this, sequentialIdGenerator);
    }

    private void initializeEncodedBytes() {
        try {
            encodedBytes = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaFileIdentifier getMediaFileIdentifier() {
        return mediaFileIdentifier;
    }

    public String getPath() {
        return path;
    }

    public byte[] getEncodedBytes() {
        return encodedBytes;
    }

    // TODO: to be removed
    @Override
    public String toString() {
        return "MediaFile{" +
                "mediaFileIdentifier=" + mediaFileIdentifier +
                ", path='" + path + '\'' +
                ", encodedBytes=" + Arrays.toString(encodedBytes) +
                '}';
    }
}
