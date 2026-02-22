package ar.edu.undec.core.exceptions;

public class InvalidStateException extends RuntimeException {
    public InvalidStateException(String message) {
        super(message);
    }

    public InvalidStateException(String message, Throwable cause) {
        super(message, cause);
    }
}
