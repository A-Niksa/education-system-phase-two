package shareables.utils.config;

import shareables.utils.logging.MasterLogger;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader extends Properties {
    private FileReader fileReader;

    public ConfigReader() {
    }

    public void initializeFileReader(String configPath) {
        try {
            fileReader = new FileReader(configPath);
            load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
