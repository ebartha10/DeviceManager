package com.platform.user.handlers.exceptions.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;

public class UserAlreadyExistsException extends CustomException{
    private static final String MESSAGE = "User already exists!";
    private static final HttpStatus httpStatus = HttpStatus.CONFLICT;

    public UserAlreadyExistsException(String resource) {
        super(MESSAGE,httpStatus, resource, new ArrayList<>());
    }

}
