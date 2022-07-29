package shareables.utils.logging;

import java.io.PrintStream;

import static shareables.utils.timekeeping.TimeManager.getTime;

public class StreamLogger implements Logger {
    private PrintStream printStream;

    public StreamLogger(PrintStream printStream) {
        this.printStream = printStream;
    }


    @Override
    public void clientLog(int callerId, LogIdentifier logIdentifier, String message, String methodName, String className) {
        log(callerId, LoggerCaller.CLIENT, logIdentifier, message, methodName, className, printStream);
    }

    @Override
    public void clientInfo(int callerId, String message, String methodName, Class<?> clazz) {
        info(callerId, LoggerCaller.CLIENT, message, methodName, clazz, printStream);
    }

    @Override
    public void clientError(int callerId, String message, String methodName, Class<?> clazz) {
        error(callerId, LoggerCaller.CLIENT, message, methodName, clazz, printStream);
    }

    @Override
    public void clientFatal(int callerId, String message, String methodName, Class<?> clazz) {
        fatal(callerId, LoggerCaller.CLIENT, message, methodName, clazz, printStream);
    }

    @Override
    public void serverLog(LogIdentifier logIdentifier, String message, String methodName, String className) {
        // the server has an id of 0 by convention
        log(0, LoggerCaller.SERVER, logIdentifier, message, methodName, className, printStream);
    }

    @Override
    public void serverInfo(String message, String methodName, Class<?> clazz) {
        info(0, LoggerCaller.SERVER, message, methodName, clazz, printStream);
    }

    @Override
    public void serverError(String message, String methodName, Class<?> clazz) {
        error(0, LoggerCaller.SERVER, message, methodName, clazz, printStream);
    }

    @Override
    public void serverFatal(String message, String methodName, Class<?> clazz) {
        fatal(0, LoggerCaller.SERVER, message, methodName, clazz, printStream);
    }
}
