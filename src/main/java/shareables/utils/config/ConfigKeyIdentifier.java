package shareables.utils.config;

public enum ConfigKeyIdentifier {
    // CONFIG_FILES:
    CONFIG_FILES_FOLDER_PATH("configFilesFolderPath"),
    GUI_PATH("GUIPath"),
    CONSTANTS_PATH("constantsPath"),
    TEXTS_PATH("textsPath"),
    ADDRESSES_PATH("addressesPath"),
    NETWORK_PATH("networkPath"),

    // ADDRESSES:
    IMAGES_FOLDER_PATH("imagesFolderPath"),
    DEFAULT_PROFILE_PICTURE_PATH("defaultProfilePicturePath"),
    CAPTCHA_5710_PATH("captcha5710Path"),
    CAPTCHA_7447_PATH("captcha7447Path"),
    CAPTCHA_8843_PATH("captcha8843Path"),
    CAPTCHA_8947_PATH("captcha8947Path"),
    CAPTCHA_9125_PATH("captcha9125Path"),
    DATABASE_FOLDER_PATH("databaseFolderPath"),
    HIBERNATE_CONFIG_PATH("hibernateConfigPath");

    private String identifierString;

    private ConfigKeyIdentifier(String identifierString) {
        this.identifierString = identifierString;
    }

    @Override
    public String toString() {
        return identifierString;
    }
}
