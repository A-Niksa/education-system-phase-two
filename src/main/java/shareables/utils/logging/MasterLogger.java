package shareables.utils.logging;

public class MasterLogger {
    private static MasterLogger masterLogger;

    private MultiLogger multiLogger;

    private MasterLogger() {
        ConsoleLogger consoleLogger = new ConsoleLogger();
        FileLogger fileLogger = new FileLogger();
        multiLogger = MultiLogger.createMultiLogger(consoleLogger, fileLogger);
    }

    private static MasterLogger getInstance() {
        if (masterLogger == null) {
            masterLogger = new MasterLogger();
        }
        return masterLogger;
    }

    public static void log(String message, LogIdentifier logIdentifier, String methodName, String className) {
        getInstance().logByInstance(message, logIdentifier, methodName, className);
    }

    private void logByInstance(String message, LogIdentifier logIdentifier, String methodName, String className) {
        multiLogger.log(message, logIdentifier, methodName, className);
    }

    public static void info(String message, String methodName, Class<?> clazz) {
        getInstance().logInfoByInstance(message, methodName, clazz);
    }

    private void logInfoByInstance(String message, String methodName, Class<?> clazz) {
        multiLogger.info(message, methodName, clazz);
    }

    public static void error(String message, String methodName, Class<?> clazz) {
        getInstance().logErrorByInstance(message, methodName, clazz);
    }

    private void logErrorByInstance(String message, String methodName, Class<?> clazz) {
        multiLogger.error(message, methodName, clazz);
    }

    public static void fatal(String message, String methodName, Class<?> clazz) {
        getInstance().logFatalErrorByInstance(message, methodName, clazz);
    }

    private void logFatalErrorByInstance(String message, String methodName, Class<?> clazz) {
        multiLogger.fatal(message, methodName, clazz);
    }
}
