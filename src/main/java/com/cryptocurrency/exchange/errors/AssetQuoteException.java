package com.cryptocurrency.exchange.errors;

public class AssetQuoteException extends RuntimeException {
    public AssetQuoteException(String message) {
        super(message);
    }
}
