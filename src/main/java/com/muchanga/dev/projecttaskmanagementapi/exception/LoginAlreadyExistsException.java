package com.muchanga.dev.projecttaskmanagementapi.exception;

public class LoginAlreadyExistsException extends RuntimeException {
    public LoginAlreadyExistsException(String login) {
        super("Login já cadastrado: " + login);
    }
}

