package exception.findAllException;

public class FindAllException extends RuntimeException {
    public FindAllException(Throwable throwable,String message) {
        super(message,throwable);
    }
}