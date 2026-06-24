package com.san_andres.backend.domain.exceptions;

/*
    409 Conflict
*/
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}