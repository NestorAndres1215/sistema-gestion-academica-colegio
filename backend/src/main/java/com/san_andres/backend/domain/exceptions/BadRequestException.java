package com.san_andres.backend.domain.exceptions;

/*
    400 Bad Request
*/
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}