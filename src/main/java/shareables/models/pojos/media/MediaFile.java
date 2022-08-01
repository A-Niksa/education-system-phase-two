package shareables.models.pojos.media;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class MediaFile {
    private MediaFileIdentifier mediaFileIdentifier;
    private String path;
    private byte[] encodedBytes;

    public MediaFile(MediaFileIdentifier mediaFileIdentifier, String path) {
        this.mediaFileIdentifier = mediaFileIdentifier;
        this.path = path;
        initializeEncodedBytes();
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
}
