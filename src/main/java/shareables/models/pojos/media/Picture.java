package shareables.models.pojos.media;

import com.fasterxml.jackson.annotation.JsonTypeName;
import shareables.utils.images.ImageIdentifier;
import shareables.utils.images.ImageManager;

@JsonTypeName("Picture")
public class Picture extends MediaFile {
    private static String defaultProfilePicturePath;
    static {
        defaultProfilePicturePath = ImageManager.getImagePath(ImageIdentifier.DEFAULT_PROFILE_PICTURE);
    }

    public Picture() {
        super(MediaFileIdentifier.PICTURE);
    }

    /**
     * sets the default profile picture as its corresponding picture
     */
    public Picture(boolean isDefaultProfilePicture) {
        super(MediaFileIdentifier.PICTURE, defaultProfilePicturePath);
    }

    public Picture(String path) {
        super(MediaFileIdentifier.PICTURE, path);
    }
}
