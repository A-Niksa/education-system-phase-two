package shareables.utils.logging;

public interface Logger extends BasicLogger {
    public void serverLog(LogIdentifier logIdentifier, String message, String methodName, String className);

    public void serverInfo(String message, String methodName, Class<?> clazz);

    public void serverError(String message, String methodName, Class<?> clazz);

    public void serverFatal(String message, String methodName, Class<?> clazz);

    public void clientLog(int callerId, LogIdentifier logIdentifier, String message,  String methodName, String className);

    public void clientInfo(int callerId, String message, String methodName, Class<?> clazz);

    public void clientError(int callerId, String message, String methodName, Class<?> clazz);

    public void clientFatal(int callerId, String message, String methodName, Class<?> clazz);
}
