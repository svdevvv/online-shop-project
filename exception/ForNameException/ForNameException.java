package exception.ForNameException;

public class ForNameException extends RuntimeException {
    public ForNameException(Throwable throwable, String message) {
        super(message, throwable);
    }
}
