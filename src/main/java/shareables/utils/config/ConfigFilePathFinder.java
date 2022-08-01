package shareables.utils.config;

public class ConfigFilePathFinder {
    public String getPath(ConfigFileIdentifier configFileIdentifier, ConfigReader configReader) {
        openConfigPathsProperties(configReader);
        String configFilesFolderPath = getConfigFilesFolderPath(configReader);
        String configKeyString = configFileIdentifier.getConfigKeyString();
        return configFilesFolderPath + configReader.getProperty(configKeyString);
    }

    private String getConfigFilesFolderPath(ConfigReader configReader) {
        return configReader.getProperty("configFilesFolderPath");
    }

    private void openConfigPathsProperties(ConfigReader configReader) {
        configReader.initializeFileReader(ConfigConstants.PATH_TO_CONFIG_PATHS);
    }
}
