package utils.imageresources;

import java.awt.*;

@FunctionalInterface
public interface ImageLoader {
    Image getImage(ImageIdentifier imageIdentifier);
}
