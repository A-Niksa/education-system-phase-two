package client.gui.utils;

import shareables.models.pojos.media.Picture;

import javax.swing.*;
import java.awt.*;

public class ImageParsingUtils {
    public static ImageIcon convertPictureToScaledImageIcon(Picture picture, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(picture.getEncodedBytes());
        ImageIcon scaledImageIcon = new ImageIcon(
                imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)
        );
        return scaledImageIcon;
    }
}
