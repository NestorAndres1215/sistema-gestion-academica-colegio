package com.san_andres.backend.shared.exception;

/*
    409 Conflict
*/
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}