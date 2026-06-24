package com.san_andres.backend.domain.exceptions;

/**
 * 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}