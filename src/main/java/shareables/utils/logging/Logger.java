package shareables.utils.logging;

public interface Logger {
    public void log(String message, LogIdentifier logIdentifier, String methodName, String className);

    public void info(String message, String methodName, Class<?> clazz);

    public void error(String message, String methodName, Class<?> clazz);

    public void fatal(String message, String methodName, Class<?> clazz);
}
