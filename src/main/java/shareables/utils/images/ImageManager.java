package shareables.utils.images;

import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigKeyIdentifier;
import shareables.utils.config.ConfigManager;

import java.awt.*;

public class ImageManager {
    private static ImageManager imageManager;

    private ImageCache imageCache;
    private ImageIdentifierParser imageIdentifierParser;

    private ImageManager() {
        imageCache = new ImageCache();
        imageIdentifierParser = new ImageIdentifierParser();
    }

    private static ImageManager getInstance() {
        if (imageManager == null) imageManager = new ImageManager();
        return imageManager;
    }

    public static Image getImage(ImageIdentifier imageIdentifier) {
        return getInstance().getImageByInstance(imageIdentifier);
    }

    private Image getImageByInstance(ImageIdentifier imageIdentifier) {
        ImageLoader imageLoader = (identifier) -> Toolkit.getDefaultToolkit().getImage(getImagePath(identifier));
        return imageCache.getImageFromCache(imageIdentifier, imageLoader);
    }

    private String getImagePath(ImageIdentifier imageIdentifier) {
        String imagesFolderPath = getImagesFolderPath();
        ConfigKeyIdentifier configKeyIdentifier = imageIdentifierParser.getConfigKeyIdentifier(imageIdentifier);
        return imagesFolderPath + ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, configKeyIdentifier);
    }

    private String getImagesFolderPath() {
        return ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, ConfigKeyIdentifier.IMAGES_FOLDER_PATH);
    }
}
