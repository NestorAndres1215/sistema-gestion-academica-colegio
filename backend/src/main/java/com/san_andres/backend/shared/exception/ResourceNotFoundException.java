package com.san_andres.backend.shared.exception;

/**
 * 404 Not Found
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}