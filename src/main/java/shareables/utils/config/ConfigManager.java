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

    public static int getPort() { // added a separate method since it needs to check for default config if necessary
        String portReadFromConfigFile = getString(ConfigFileIdentifier.NETWORK, "port");
        if (portReadFromConfigFile == null) return ConfigConstants.DEFAULT_PORT;
        return Integer.parseInt(portReadFromConfigFile);
    }

    public static String getIp() { // added a separate method since it needs to check for default config if necessary
        String ipReadFromConfigFile = getString(ConfigFileIdentifier.NETWORK, "port");
        if (ipReadFromConfigFile == null) return ConfigConstants.DEFAULT_IP;
        return ipReadFromConfigFile;
    }

    public static double getDouble(ConfigFileIdentifier configFileIdentifier, String configKeyString) {
        return Double.parseDouble(getString(configFileIdentifier, configKeyString));
    }

    public static long getLong(ConfigFileIdentifier configFileIdentifier, String configKeyString) {
        return Long.parseLong(getString(configFileIdentifier, configKeyString));
    }

    public static int getInt(ConfigFileIdentifier configFileIdentifier, String configKeyString) {
        return Integer.parseInt(getString(configFileIdentifier, configKeyString));
    }

    public static String getString(ConfigFileIdentifier configFileIdentifier, String configKeyString) {
        return getInstance().getValue(configFileIdentifier, configKeyString);
    }

    public static String getConfigPath(ConfigFileIdentifier configFileIdentifier) {
        return getInstance().configFilePathFinder.getPath(configFileIdentifier, getInstance().configReader);
    }

    private String getValue(ConfigFileIdentifier configFileIdentifier, String configKeyString) {
        String configPath = getConfigPath(configFileIdentifier);
        configReader.initializeFileReader(configPath);
        return configReader.getProperty(configKeyString);
    }
}