package shareables.utils.logging;

import shareables.utils.config.ConfigFileIdentifier;
import shareables.utils.config.ConfigManager;

import java.io.*;

import static shareables.utils.timekeeping.TimeManager.getTime;

public class FileLogger extends StreamLogger {
    public FileLogger() {
        super(initializeFileStream());
    }

    private static PrintStream initializeFileStream() {
        File file = new File(getPathToLog());
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println(String.format("%s - %s - %s - %s - %s", getTime(), LogIdentifier.FATAL,
                            "The log file could not be created", "initializeFileStream", "utils.logging.FileLogger"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return new PrintStream(new FileOutputStream(file, true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getPathToLog() {
        String logsFolderPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "logsFolderPath");
        String masterLogPath = ConfigManager.getString(ConfigFileIdentifier.ADDRESSES, "masterLogPath");
        return logsFolderPath + masterLogPath;
    }
}
