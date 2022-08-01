package shareables.utils.logging;

import java.io.PrintStream;

import static shareables.utils.timing.timekeeping.TimeManager.getTime;

public interface BasicLogger {
    public default void log(int callerId, LoggerCaller loggerCaller, LogIdentifier logIdentifier, String message,
                    String methodName, String className, PrintStream printStream) {
        printStream.println(String.format("%s - %s - %s - %s - %s - %s - %s", getTime(), logIdentifier.toString(), callerId,
                loggerCaller, message, methodName, className)); // format is used for explicitness. it's redundant though
    }

    public default void info(int callerId, LoggerCaller loggerCaller, String message, String methodName, Class<?> clazz,
                     PrintStream printStream) {
        printStream.println(String.format("%s - %s - %s - %s - %s - %s - %s", getTime(), LogIdentifier.INFO, callerId,
                loggerCaller, message, methodName, clazz.getName()));
    }

    public default void error(int callerId, LoggerCaller loggerCaller, String message, String methodName, Class<?> clazz,
                      PrintStream printStream) {
        printStream.println(String.format("%s - %s - %s - %s - %s - %s - %s", getTime(), LogIdentifier.ERROR, callerId,
                loggerCaller, message, methodName, clazz.getName()));
    }

    public default void fatal(int callerId, LoggerCaller loggerCaller, String message, String methodName, Class<?> clazz,
                      PrintStream printStream) {
        printStream.println(String.format("%s - %s - %s - %s - %s - %s - %s", getTime(), LogIdentifier.FATAL, callerId,
                loggerCaller, message, methodName, clazz.getName()));
    }
}
