package com.achiveme.mvp.exception.User;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmailDoesNotExistException extends RuntimeException{
    public EmailDoesNotExistException(String message){
        super(message);
    }
}
