package shareables.utils.config;

public class ConfigIdSupplier {
    private static ConfigIdSupplier configIdSupplier;

    private ConfigFileIdentifier configFileIdentifier;
    private ConfigWriter configWriter;

    private ConfigIdSupplier() {
        configFileIdentifier = ConfigFileIdentifier.ID_GENERATION;
        configWriter = new ConfigWriter();
    }

    private void initializeConfigWriterReader() {
        String configPath = ConfigManager.getConfigPath(configFileIdentifier);
        configWriter.initializeFileWriter(configPath);
    }

    private static ConfigIdSupplier getInstance() {
        if (configIdSupplier == null) configIdSupplier = new ConfigIdSupplier();
        return configIdSupplier;
    }

    public static synchronized int nextClientId() {
        int nextClientId = getCurrentClientId();
        incrementCurrentClientId();
        return nextClientId;
    }

    public static synchronized int getCurrentClientId() { // TODO: should this really be synchronized?
        return ConfigManager.getInt(getInstance().configFileIdentifier, "currentClientId");
    }

    private static synchronized void incrementCurrentClientId() {
        getInstance().initializeConfigWriterReader();
        getInstance().configWriter.write("currentClientId", getCurrentClientId() + 1);
    }

    public static void resetCurrentClientId() {
        getInstance().initializeConfigWriterReader();
        getInstance().configWriter.write("currentClientId", 1);
    }
}
