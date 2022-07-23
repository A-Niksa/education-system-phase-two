package shareables.utils.images;

import java.awt.*;
import java.util.EnumMap;

public class ImageCache {
    private final EnumMap<ImageIdentifier, Image> imagesMap;

    ImageCache() {
        imagesMap = new EnumMap<>(ImageIdentifier.class);
    }

    public void addToCache(ImageIdentifier identifier, Image image) {
        imagesMap.put(identifier, image);
    }

    public Image getImageFromCache(ImageIdentifier identifier, ImageLoader loader) {
        return imagesMap.computeIfAbsent(identifier, loader::getImage);
    }
}
