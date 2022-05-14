package org.example.exception;

public class AccountRemovedException extends Exception {
    public AccountRemovedException() {
    }

    public AccountRemovedException(String message) {
        super(message);
    }

    public AccountRemovedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountRemovedException(Throwable cause) {
        super(cause);
    }

    public AccountRemovedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
