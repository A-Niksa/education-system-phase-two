package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("PDF File")
public class PDF extends MediaFile {
    public PDF() {
        super(MediaFileIdentifier.PDF);
    }

    public PDF(String path) {
        super(MediaFileIdentifier.PDF, path);
    }
}
