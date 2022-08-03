package shareables.utils.config;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class ConfigWriter extends Properties {
    private FileWriter fileWriter;

    public ConfigWriter() {
    }

    public void initializeFileWriter(String configPath) {
        try {
            fileWriter = new FileWriter(configPath);
            // TODO: logging this?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String key, int value) {
        try {
            fileWriter.write(key + " = " + value);
            // TODO: logging this?
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileWriter.close();
            // TODO: logging this?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
