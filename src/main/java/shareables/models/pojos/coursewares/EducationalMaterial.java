package shareables.models.pojos.coursewares;

import shareables.models.idgeneration.IdentifiableWithTime;
import shareables.models.pojos.media.MediaFile;

public class EducationalMaterial extends IdentifiableWithTime {
    private String text;
    private MediaFile mediaFile;

    public EducationalMaterial() {
        initializeId();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MediaFile getMediaFile() {
        return mediaFile;
    }

    public void setMediaFile(MediaFile mediaFile) {
        this.mediaFile = mediaFile;
    }
}
