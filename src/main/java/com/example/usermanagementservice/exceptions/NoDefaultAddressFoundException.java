package com.example.usermanagementservice.exceptions;

public class NoDefaultAddressFoundException extends RuntimeException {
    public NoDefaultAddressFoundException(String message) {
        super(message);
    }
}
