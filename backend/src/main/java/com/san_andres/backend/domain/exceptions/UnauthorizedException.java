package com.san_andres.backend.domain.exceptions;

/*
    401 Unauthorized
*/
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}