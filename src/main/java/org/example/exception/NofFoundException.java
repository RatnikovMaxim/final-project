package org.example.exception;

public class NofFoundException extends Exception {
    public NofFoundException() {
    }

    public NofFoundException(String message) {
        super(message);
    }

    public NofFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NofFoundException(Throwable cause) {
        super(cause);
    }

    public NofFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
