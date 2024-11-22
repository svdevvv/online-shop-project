package exception.propertiesLoadException;

public class LoadPropertiesException extends RuntimeException {
    public LoadPropertiesException(Throwable throwable, String message) {
        super(message, throwable);
    }
}