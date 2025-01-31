package com.example.usermanagementservice.exceptions;

public class DuplicateAddressException extends RuntimeException {
    public DuplicateAddressException(String message) {
        super(message);
    }
}
