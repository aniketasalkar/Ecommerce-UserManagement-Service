package com.example.usermanagementservice.exceptions;

public class FieldNotModifiableException extends RuntimeException {
    public FieldNotModifiableException(String message) {
        super(message);
    }
}
