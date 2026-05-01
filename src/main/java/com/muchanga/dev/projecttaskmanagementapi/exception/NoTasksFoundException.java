package com.muchanga.dev.projecttaskmanagementapi.exception;

public class NoTasksFoundException extends RuntimeException {
    public NoTasksFoundException(String message) {
        super(message);
    }
}
