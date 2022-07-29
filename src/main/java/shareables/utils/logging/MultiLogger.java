package shareables.utils.logging;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiLogger implements Logger {
    private ArrayList<Logger> loggers;

    private MultiLogger() {
        loggers = new ArrayList<>();
    }

    public static MultiLogger createMultiLogger(Logger... loggers) {
        MultiLogger multiLogger = new MultiLogger();
        multiLogger.loggers.addAll(Arrays.asList(loggers));
        return multiLogger;
    }

    @Override
    public void clientLog(int callerId, LogIdentifier logIdentifier, String message, String methodName, String className) {
        for (Logger logger : loggers) {
            logger.clientLog(callerId, logIdentifier, message, methodName, className);
        }
    }

    @Override
    public void clientInfo(int callerId, String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.clientInfo(callerId, message, methodName, clazz);
        }
    }

    @Override
    public void clientError(int callerId, String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.clientError(callerId, message, methodName, clazz);
        }
    }

    @Override
    public void clientFatal(int callerId, String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.clientFatal(callerId, message, methodName, clazz);
        }
    }

    @Override
    public void serverLog(LogIdentifier logIdentifier, String message, String methodName, String className) {
        for (Logger logger : loggers) {
            logger.serverLog(logIdentifier, message, methodName, className);
        }
    }

    @Override
    public void serverInfo(String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.serverInfo(message, methodName, clazz);
        }
    }

    @Override
    public void serverError(String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.serverError(message, methodName, clazz);
        }
    }

    @Override
    public void serverFatal(String message, String methodName, Class<?> clazz) {
        for (Logger logger : loggers) {
            logger.serverFatal(message, methodName, clazz);
        }
    }
}
