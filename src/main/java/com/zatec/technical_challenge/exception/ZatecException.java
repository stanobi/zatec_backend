package com.zatec.technical_challenge.exception;

import org.springframework.http.HttpStatus;

public class ZatecException extends Exception {

    private final HttpStatus httpStatus;

    public ZatecException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
