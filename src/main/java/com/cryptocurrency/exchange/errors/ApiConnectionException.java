package com.cryptocurrency.exchange.errors;

import java.io.IOException;

public class ApiConnectionException extends RuntimeException {

    public ApiConnectionException(String message) {
        super(message);
    }
}
