package shareables.models.pojos.media;

import shareables.utils.images.ImageIdentifier;
import shareables.utils.images.ImageManager;

public class Picture extends MediaFile {
    private static String defaultProfilePicturePath;
    static {
        defaultProfilePicturePath = ImageManager.getImagePath(ImageIdentifier.DEFAULT_PROFILE_PICTURE);
    }

    /**
     * sets the default profile picture as its corresponding picture
     */
    public Picture() {
        super(MediaFileIdentifier.PICTURE, defaultProfilePicturePath);
    }

    public Picture(String path) {
        super(MediaFileIdentifier.PICTURE, path);
    }
}
