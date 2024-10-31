package com.example.SwipeFlight.server.utils.custom_exceptions;

/**
 * The class represents an exception for a case when necessary request attribute is not found.
 */
public class RequestAttributeNotFoundException extends RuntimeException {
    public RequestAttributeNotFoundException(String message) {
        super(message);
    }
}