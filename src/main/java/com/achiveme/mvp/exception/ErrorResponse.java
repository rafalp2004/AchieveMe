package com.achiveme.mvp.exception;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponse {
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;
    private List<String> errors;


    public ErrorResponse(int status, String message, LocalDateTime timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}