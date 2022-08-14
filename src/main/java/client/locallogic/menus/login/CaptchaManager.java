package client.locallogic.menus.login;

import shareables.utils.images.ImageIdentifier;

import java.util.ArrayList;
import java.util.List;

public class CaptchaManager {
    private int captchaIndex;
    private Captcha currentCaptcha;
    private List<Captcha> captchasList;

    public CaptchaManager() {
        captchaIndex = 0;
        captchasList = new ArrayList<>();
        getAllCaptchas();
    }

    public void getAllCaptchas() {
        Captcha firstCaptcha = new Captcha(5710);
        captchasList.add(firstCaptcha);
        Captcha secondCaptcha = new Captcha(7447);
        captchasList.add(secondCaptcha);
        Captcha thirdCaptcha = new Captcha(8843);
        captchasList.add(thirdCaptcha);
        Captcha fourthCaptcha = new Captcha(8947);
        captchasList.add(fourthCaptcha);
        Captcha fifthCaptcha = new Captcha(9125);
        captchasList.add(fifthCaptcha);
    }

    public ImageIdentifier nextCaptchaImageIdentifier() {
        int currentIndex = captchaIndex % 5;
        captchaIndex++;
        currentCaptcha = captchasList.get(currentIndex);
        return currentCaptcha.getCaptchaImageIdentifier();
    }

    public Captcha getCurrentCaptcha() {
        return currentCaptcha;
    }
}
