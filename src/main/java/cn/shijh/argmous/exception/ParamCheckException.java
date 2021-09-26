package cn.shijh.argmous.exception;

public class ParamCheckException extends ArgmousBaseException{
    public ParamCheckException() {
    }

    public ParamCheckException(String message) {
        super(message);
    }

    public ParamCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParamCheckException(Throwable cause) {
        super(cause);
    }

    public ParamCheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
