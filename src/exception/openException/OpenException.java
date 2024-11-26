package exception.openException;

public class OpenException extends RuntimeException {
    public OpenException(Throwable throwable, String message) {
        super(message, throwable);
    }
}