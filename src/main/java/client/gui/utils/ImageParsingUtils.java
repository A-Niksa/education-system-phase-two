package client.gui.utils;

import shareables.models.pojos.media.Picture;

import javax.swing.*;

public class ImageParsingUtils {
    public static ImageIcon convertPictureToImageIcon(Picture picture) {
        return new ImageIcon(picture.getEncodedBytes());
    }
}
