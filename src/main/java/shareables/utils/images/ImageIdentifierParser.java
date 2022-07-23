package shareables.utils.images;

import shareables.utils.config.ConfigKeyIdentifier;

public class ImageIdentifierParser {
    public ConfigKeyIdentifier getConfigKeyIdentifier(ImageIdentifier imageIdentifier) {
        ConfigKeyIdentifier configKeyIdentifier;
        switch (imageIdentifier) {
            case DEFAULT_PROFILE_PICTURE:
                configKeyIdentifier = ConfigKeyIdentifier.DEFAULT_PROFILE_PICTURE_PATH;
                break;
            case CAPTCHA_5710:
                configKeyIdentifier = ConfigKeyIdentifier.CAPTCHA_5710_PATH;
                break;
            case CAPTCHA_7447:
                configKeyIdentifier = ConfigKeyIdentifier.CAPTCHA_7447_PATH;
                break;
            case CAPTCHA_8843:
                configKeyIdentifier = ConfigKeyIdentifier.CAPTCHA_8843_PATH;
                break;
            case CAPTCHA_8947:
                configKeyIdentifier = ConfigKeyIdentifier.CAPTCHA_8947_PATH;
                break;
            case CAPTCHA_9125:
                configKeyIdentifier = ConfigKeyIdentifier.CAPTCHA_9125_PATH;
                break;
            default: // this branch was added for explicitness
                configKeyIdentifier = null;
        }
        return configKeyIdentifier;
    }
}
