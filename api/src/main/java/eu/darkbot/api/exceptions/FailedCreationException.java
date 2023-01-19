package eu.darkbot.api.exceptions;

public class FailedCreationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public FailedCreationException() {
        super();
    }

    public FailedCreationException(String message) {
        super(message);
    }

    public FailedCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedCreationException(Throwable cause) {
        super(cause);
    }

}
