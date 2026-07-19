package com.san_andres.backend.shared.exception;

/*
    403 Forbidden
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}