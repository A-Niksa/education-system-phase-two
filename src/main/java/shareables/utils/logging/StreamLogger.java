package shareables.utils.logging;

import java.io.PrintStream;

import static shareables.utils.timekeeping.TimeManager.getTime;

public class StreamLogger implements Logger {
    private PrintStream printStream;

    public StreamLogger(PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void log(String message, LogIdentifier logIdentifier, String methodName, String className) {
        printStream.println(String.format("%s - %s - %s - %s - %s", getTime(), logIdentifier.toString(), message,
                methodName, className));
    }

    @Override
    public void info(String message, String methodName, Class<?> clazz) {
        printStream.println(String.format("%s - %s - %s - %s - %s", getTime(), LogIdentifier.INFO.toString(), message,
                methodName, clazz.getName()));
    }

    @Override
    public void error(String message, String methodName, Class<?> clazz) {
        printStream.println(String.format("%s - %s - %s - %s - %s", getTime(), LogIdentifier.ERROR.toString(), message,
                methodName, clazz.getName()));
    }

    @Override
    public void fatal(String message, String methodName, Class<?> clazz) {
        printStream.println(String.format("%s - %s - %s - %s - %s", getTime(), LogIdentifier.FATAL.toString(), message,
                methodName, clazz.getName()));
    }
}
