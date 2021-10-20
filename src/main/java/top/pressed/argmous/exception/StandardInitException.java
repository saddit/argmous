package top.pressed.argmous.exception;

public class StandardInitException extends ArgmousBaseException {
    public StandardInitException() {
        super();
    }

    public StandardInitException(String message) {
        super(message);
    }

    public StandardInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public StandardInitException(Throwable cause) {
        super(cause);
    }

    public StandardInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
