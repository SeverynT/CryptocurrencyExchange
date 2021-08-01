package com.cryptocurrency.exchange.errors;

public class CryptocurrencyNotExistsException extends RuntimeException {
    public CryptocurrencyNotExistsException(String message) {
        super(message);
    }

}
