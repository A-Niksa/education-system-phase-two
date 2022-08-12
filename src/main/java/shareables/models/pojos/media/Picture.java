package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.models.idgeneration.SequentialIdGenerator;
import shareables.utils.images.ImageIdentifier;
import shareables.utils.images.ImageManager;

@JsonTypeName("Picture")
public class Picture extends MediaFile {
    private static SequentialIdGenerator sequentialIdGenerator;
    private static String defaultProfilePicturePath;
    static {
        sequentialIdGenerator = getNewSequentialIdGenerator();
        defaultProfilePicturePath = ImageManager.getImagePath(ImageIdentifier.DEFAULT_PROFILE_PICTURE);
    }

    public Picture() {
        super(MediaFileIdentifier.PICTURE);
        initializeId(sequentialIdGenerator);
    }

    /**
     * sets the default profile picture as its corresponding picture
     */
    public Picture(boolean isDefaultProfilePicture) {
        super(MediaFileIdentifier.PICTURE, defaultProfilePicturePath);
        initializeId(sequentialIdGenerator);
    }

    public Picture(String path) {
        super(MediaFileIdentifier.PICTURE, path);
        initializeId(sequentialIdGenerator);
    }
}
