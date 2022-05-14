package org.example.advice;

import org.example.dto.ExceptionDTO;
import org.example.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.InvalidAttributeValueException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(InvalidLimitException e) {
        e.printStackTrace();
        return new ExceptionDTO("invalid_limit");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDTO handle(ForbiddenException e) {
        e.printStackTrace();
        return new ExceptionDTO("not_enough_authority");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDTO handle(NofFoundException e) {
        e.printStackTrace();
        return new ExceptionDTO("part.not_found");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(PasswordNotMatchesException e) {
        e.printStackTrace();
        return new ExceptionDTO("password_not_matches");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(NotAuthenticatedException e) {
        e.printStackTrace();
        return new ExceptionDTO("not_authenticated");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(LoginAlreadyExistsException e) {
        e.printStackTrace();
        return new ExceptionDTO("login_already_exists");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(InvalidAttributeValueException e) {
        e.printStackTrace();
        return new ExceptionDTO("invalid_value_quantity");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDTO handle(AccountRemovedException e) {
        e.printStackTrace();
        return new ExceptionDTO("account_removed");
    }
}
