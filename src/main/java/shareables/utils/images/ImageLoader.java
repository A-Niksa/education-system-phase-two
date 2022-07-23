package shareables.utils.images;

import java.awt.*;

@FunctionalInterface
public interface ImageLoader {
    Image getImage(ImageIdentifier imageIdentifier);
}