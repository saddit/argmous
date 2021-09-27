package cn.shijh.argmous.exception;


public class RuleCreateException extends ArgmousBaseException {
    public RuleCreateException() {
    }

    public RuleCreateException(String message) {
        super(message);
    }

    public RuleCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleCreateException(Throwable cause) {
        super(cause);
    }

    public RuleCreateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
