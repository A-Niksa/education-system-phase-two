package shareables.utils.config;

public enum ConfigKeyIdentifier {
    // CONFIG_FILES:
    CONFIG_FILES_FOLDER_PATH("configFilesFolderPath"),
    GUI_PATH("GUIPath"),
    CONSTANTS_PATH("constantsPath"),
    TEXTS_PATH("textsPath"),
    ADDRESSES_PATH("addressesPath"),
    NETWORK_PATH("networkPath");


    private String identifierString;

    private ConfigKeyIdentifier(String identifierString) {
        this.identifierString = identifierString;
    }

    @Override
    public String toString() {
        return identifierString;
    }
}
