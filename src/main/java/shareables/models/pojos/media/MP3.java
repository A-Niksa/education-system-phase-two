package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("MP3 File")
public class MP3 extends MediaFile {
    public MP3() {
        super(MediaFileIdentifier.MP3);
    }

    public MP3(String path) {
        super(MediaFileIdentifier.MP3, path);
    }
}
