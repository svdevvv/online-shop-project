package exception.poolTakeException;

public class PoolTakeException extends RuntimeException {
    public PoolTakeException(Throwable throwable, String message) {
        super(message, throwable);
    }
}