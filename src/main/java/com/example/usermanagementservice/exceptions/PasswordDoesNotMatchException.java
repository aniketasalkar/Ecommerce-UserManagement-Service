package com.example.usermanagementservice.exceptions;

public class PasswordDoesNotMatchException extends RuntimeException {
    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
