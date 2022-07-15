package utils.config;

public class ConfigMaster {
    private static ConfigMaster configMaster;

    private ConfigMaster() {
    }

    private static ConfigMaster getInstance() {
        if (configMaster == null) configMaster = new ConfigMaster();
        return configMaster;
    }


}