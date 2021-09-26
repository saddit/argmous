package cn.shijh.argmous.exception;

public abstract class ArgmousBaseException extends RuntimeException{
    public ArgmousBaseException() {
    }

    public ArgmousBaseException(String message) {
        super(message);
    }

    public ArgmousBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgmousBaseException(Throwable cause) {
        super(cause);
    }

    public ArgmousBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
