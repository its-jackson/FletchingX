package scripts.api.exceptions;

public class ScriptCompleteException extends RuntimeException {

    public ScriptCompleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
