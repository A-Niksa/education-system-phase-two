package shareables.utils.images;

public class ImageIdentifierParser {
    public String getConfigKeyString(ImageIdentifier imageIdentifier) {
        String configKeyString;
        switch (imageIdentifier) {
            case DEFAULT_PROFILE_PICTURE:
                configKeyString = "defaultProfilePicturePath";
                break;
            case CAPTCHA_5710:
                configKeyString = "captcha5710Path";
                break;
            case CAPTCHA_7447:
                configKeyString = "captcha7447Path";
                break;
            case CAPTCHA_8843:
                configKeyString = "captcha8843Path";
                break;
            case CAPTCHA_8947:
                configKeyString = "captcha8947Path";
                break;
            case CAPTCHA_9125:
                configKeyString = "captcha9125Path";
                break;
            default: // this branch was added for explicitness
                configKeyString = null;
        }
        return configKeyString;
    }
}
