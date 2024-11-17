package com.example.usermanagementservice.exceptions;

public class MaximumAddressesReachedException extends RuntimeException {
    public MaximumAddressesReachedException(String message) {
        super(message);
    }
}
