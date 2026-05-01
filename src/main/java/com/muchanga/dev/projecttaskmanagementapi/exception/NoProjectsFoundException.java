package com.muchanga.dev.projecttaskmanagementapi.exception;

public class NoProjectsFoundException extends RuntimeException{
    public NoProjectsFoundException(String message){
        super(message);
    }
}
