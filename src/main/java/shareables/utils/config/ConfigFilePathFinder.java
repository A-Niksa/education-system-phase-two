package shareables.utils.config;

public class ConfigFilePathFinder {
    public String getPath(ConfigFileIdentifier configFileIdentifier, ConfigReader configReader) {
        openConfigPathsProperties(configReader);
        String configFilesFolderPath = getConfigFilesFolderPath(configReader);

        String configKeyIdentifierString;
        switch (configFileIdentifier) {
            case GUI:
                configKeyIdentifierString = ConfigKeyIdentifier.GUI_PATH.toString();
                break;
            case CONSTANTS:
                configKeyIdentifierString = ConfigKeyIdentifier.CONSTANTS_PATH.toString();
                break;
            case TEXTS:
                configKeyIdentifierString = ConfigKeyIdentifier.TEXTS_PATH.toString();
                break;
            case ADDRESSES:
                configKeyIdentifierString = ConfigKeyIdentifier.ADDRESSES_PATH.toString();
                break;
            case NETWORK:
                configKeyIdentifierString = ConfigKeyIdentifier.NETWORK_PATH.toString();
                break;
            default:
                configKeyIdentifierString = "-";
        }
        return configFilesFolderPath + configReader.getProperty(configKeyIdentifierString);
    }

    private String getConfigFilesFolderPath(ConfigReader configReader) {
        return configReader.getProperty(ConfigKeyIdentifier.CONFIG_FILES_FOLDER_PATH.toString());
    }

    private void openConfigPathsProperties(ConfigReader configReader) {
        configReader.initializeFileReader(ConfigConstants.PATH_TO_CONFIG_PATHS);
    }
}
