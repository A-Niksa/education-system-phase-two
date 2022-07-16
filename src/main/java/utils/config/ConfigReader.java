package utils.config;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader extends Properties {
    private static final String PATH_TO_CONFIG_PATHS = "src/main/resources/config/configpaths.properties";

    private FileReader reader;

    public ConfigReader() {

    }

    private void initializeReader() {
        try {
            reader = new FileReader(PATH_TO_CONFIG_PATHS);
            load(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
