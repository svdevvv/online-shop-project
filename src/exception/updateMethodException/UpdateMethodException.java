package exception.updateMethodException;

public class UpdateMethodException extends RuntimeException {
    public UpdateMethodException(Throwable throwable, String message) {
        super(message, throwable);
    }
}