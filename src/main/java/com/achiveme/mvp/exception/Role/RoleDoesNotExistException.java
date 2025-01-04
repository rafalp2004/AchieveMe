package com.achiveme.mvp.exception.Role;

public class RoleDoesNotExistException extends RuntimeException{
    public RoleDoesNotExistException(String message){
        super(message);
    }
}
