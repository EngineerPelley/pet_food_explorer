package com.petfood.explorer.web;

/**
 * Thrown by services when a requested resource does not exist. Mapped to a 404
 * JSON response by {@link GlobalExceptionHandler}.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
