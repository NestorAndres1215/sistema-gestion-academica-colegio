package com.san_andres.backend.domain.exceptions;

/*
    403 Forbidden
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}