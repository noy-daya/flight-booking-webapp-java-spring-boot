package com.example.SwipeFlight.server.utils.custom_exceptions;

/**
 * The class represents an exception for a case when necessary session attribute is not found.
 */
public class SessionIsInvalidException extends RuntimeException {
    public SessionIsInvalidException(String message) {
        super(message);
    }
}
