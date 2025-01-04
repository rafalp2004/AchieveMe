package com.achiveme.mvp.exception.Challenge;


public class ChallengeDoesNotExistException extends RuntimeException {
    public ChallengeDoesNotExistException(String message){
        super(message);
    }
}
