package com.example.usermanagementservice.exceptions;

public class NoActiveSessionFoundException extends RuntimeException {
    public NoActiveSessionFoundException(String message) {
        super(message);
    }
}
