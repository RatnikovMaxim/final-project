package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class InvalidLimitException extends Exception {
    public InvalidLimitException() {
    }

    public InvalidLimitException(String message) {
        super(message);
    }

    public InvalidLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidLimitException(Throwable cause) {
        super(cause);
    }

    public InvalidLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
