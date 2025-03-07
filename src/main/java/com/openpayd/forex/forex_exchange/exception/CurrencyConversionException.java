package com.openpayd.forex.forex_exchange.exception;

import org.springframework.http.HttpStatus;

public class CurrencyConversionException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus status;

    public CurrencyConversionException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }
} 