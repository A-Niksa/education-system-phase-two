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
    
    public static void clientLog(int callerId, LogIdentifier logIdentifier, String message, String methodName,
                                 String className) {
        getInstance().multiLogger.clientLog(callerId, logIdentifier, message, methodName, className);
    }
    
    public static void clientInfo(int callerId, String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.clientInfo(callerId, message, methodName, clazz);
    }
    
    public static void clientError(int callerId, String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.clientError(callerId, message, methodName, clazz);
    }
    
    public static void clientFatal(int callerId, String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.clientFatal(callerId, message, methodName, clazz);
    }
    
    public static void serverLog(LogIdentifier logIdentifier, String message, String methodName, String className) {
        getInstance().multiLogger.serverLog(logIdentifier, message, methodName, className);
    }
    
    public static void serverInfo(String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.serverInfo(message, methodName, clazz);
    }
    
    public static void serverError(String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.serverError(message, methodName, clazz);
    }
    
    public static void serverFatal(String message, String methodName, Class<?> clazz) {
        getInstance().multiLogger.serverFatal(message, methodName, clazz);
    }
}
