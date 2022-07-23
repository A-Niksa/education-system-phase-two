package shareables.utils.config;

public class ConfigManager {
    private static ConfigManager configMaster;

    private ConfigReader configReader;
    private ConfigFilePathFinder configFilePathFinder;

    private ConfigManager() {
        configReader = new ConfigReader();
        configFilePathFinder = new ConfigFilePathFinder();
    }

    private static ConfigManager getInstance() {
        if (configMaster == null) configMaster = new ConfigManager();
        return configMaster;
    }

    public static double getDouble(ConfigFileIdentifier configFileIdentifier, ConfigKeyIdentifier configKeyIdentifier) {
        return Double.parseDouble(getString(configFileIdentifier, configKeyIdentifier));
    }

    public static int getInt(ConfigFileIdentifier configFileIdentifier, ConfigKeyIdentifier configKeyIdentifier) {
        return Integer.parseInt(getString(configFileIdentifier, configKeyIdentifier));
    }

    public static String getString(ConfigFileIdentifier configFileIdentifier, ConfigKeyIdentifier configKeyIdentifier) {
        return getInstance().getValue(configFileIdentifier, configKeyIdentifier);
    }

    private String getValue(ConfigFileIdentifier configFileIdentifier, ConfigKeyIdentifier configKeyIdentifier) {
        String configPath = configFilePathFinder.getPath(configFileIdentifier, configReader);
        configReader.initializeFileReader(configPath);
        return configReader.getProperty(configKeyIdentifier.toString());
    }
}