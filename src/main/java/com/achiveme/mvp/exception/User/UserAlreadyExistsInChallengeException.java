package com.achiveme.mvp.exception.User;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsInChallengeException extends RuntimeException{
    public UserAlreadyExistsInChallengeException(String message){
        super(message);
    }
}
