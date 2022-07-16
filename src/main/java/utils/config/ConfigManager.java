package utils.config;

public class ConfigManager {
    private static ConfigManager configMaster;

    private ConfigManager() {
    }

    private static ConfigManager getInstance() {
        if (configMaster == null) configMaster = new ConfigManager();
        return configMaster;
    }

    public static int getInt(ConfigFileIdentifier fileIdentifier, ConfigKeyIdentifier keyIdentifier) {
        return Integer.parseInt(getString(fileIdentifier, keyIdentifier));
    }

    public static String getString(ConfigFileIdentifier fileIdentifier, ConfigKeyIdentifier keyIdentifier) {
        return getInstance().getValue(fileIdentifier, keyIdentifier);
    }

    private String getValue(ConfigFileIdentifier fileIdentifier, ConfigKeyIdentifier keyIdentifier) {
        return null;
    }
}