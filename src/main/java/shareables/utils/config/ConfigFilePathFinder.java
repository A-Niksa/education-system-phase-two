package shareables.utils.config;

public class ConfigFilePathFinder {
    public String getPath(ConfigFileIdentifier configFileIdentifier, ConfigReader configReader) {
        openConfigPathsProperties(configReader);
        String configFilesFolderPath = getConfigFilesFolderPath(configReader);

        String configKeyString;
        switch (configFileIdentifier) {
            case GUI:
                configKeyString = "GUIPath";
                break;
            case CONSTANTS:
                configKeyString = "constantsPath";
                break;
            case TEXTS:
                configKeyString = "textsPath";
                break;
            case ADDRESSES:
                configKeyString = "addressesPath";
                break;
            case NETWORK:
                configKeyString = "networkPath";
                break;
            default:
                configKeyString = null;
        }
        return configFilesFolderPath + configReader.getProperty(configKeyString);
    }

    private String getConfigFilesFolderPath(ConfigReader configReader) {
        return configReader.getProperty("configFilesFolderPath");
    }

    private void openConfigPathsProperties(ConfigReader configReader) {
        configReader.initializeFileReader(ConfigConstants.PATH_TO_CONFIG_PATHS);
    }
}
