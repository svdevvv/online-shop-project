package exception.saveMethodException;

public class SaveMethodException extends RuntimeException {
    public SaveMethodException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
