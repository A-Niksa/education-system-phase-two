package client.gui.menus.login;

import client.locallogic.menus.login.CaptchaManager;
import shareables.utils.images.ImageIdentifier;
import shareables.utils.images.ImageManager;

import javax.swing.*;
import java.awt.*;

public class CaptchaLoader {
    private CaptchaManager captchaManager;

    public CaptchaLoader() {
        captchaManager = new CaptchaManager();
    }

    public ImageIcon nextCaptchaImageIcon() {
        ImageIdentifier captchaImageIdentifier = captchaManager.nextCaptchaImageIdentifier();
        Image captchaImage = ImageManager.getImage(captchaImageIdentifier);
        return new ImageIcon(captchaImage);
    }

    public String getCurrentCaptchaString() {
        return captchaManager.getCurrentCaptcha().getCaptchaNumber() + "";
    }
}
