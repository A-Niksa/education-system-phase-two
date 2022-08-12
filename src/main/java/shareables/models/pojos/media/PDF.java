package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.idgeneration.SequentialIdGenerator;

@JsonTypeName("PDF File")
public class PDF extends MediaFile {
    private static SequentialIdGenerator sequentialIdGenerator;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
    }

    public PDF() {
        super(MediaFileIdentifier.PDF);
        initializeId(sequentialIdGenerator);
    }

    public PDF(String path) {
        super(MediaFileIdentifier.PDF, path);
        initializeId(sequentialIdGenerator);
    }
}