package client.locallogic.login;

import shareables.utils.images.ImageIdentifier;

import java.awt.*;

public class Captcha {
    private int captchaNumber;
    private ImageIdentifier captchaImageIdentifier;

    public Captcha(int captchaNumber) {
        this.captchaNumber = captchaNumber;
        setCaptchaImageIdentifier();
    }

    private void setCaptchaImageIdentifier() {
        switch (captchaNumber) {
            case 5710:
                captchaImageIdentifier = ImageIdentifier.CAPTCHA_5710;
                break;
            case 7447:
                captchaImageIdentifier = ImageIdentifier.CAPTCHA_7447;
                break;
            case 8843:
                captchaImageIdentifier = ImageIdentifier.CAPTCHA_8843;
                break;
            case 8947:
                captchaImageIdentifier = ImageIdentifier.CAPTCHA_8947;
                break;
            case 9125:
                captchaImageIdentifier = ImageIdentifier.CAPTCHA_9125;
        }
    }

    public int getCaptchaNumber() {
        return captchaNumber;
    }

    public ImageIdentifier getCaptchaImageIdentifier() {
        return captchaImageIdentifier;
    }
}
