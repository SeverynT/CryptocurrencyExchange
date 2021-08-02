package com.cryptocurrency.exchange.errors;

public class ApiConnectionException extends RuntimeException {
    public ApiConnectionException(String message) {
        super(message);
    }
}
