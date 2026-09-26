package com.example.talkivo.exception;

/**
 * Thrown when the request fails validation (empty text, unsupported
 * language/voice, text over the allowed length, etc).
 */
public class InvalidTextException extends RuntimeException {

    public InvalidTextException(String message) {
        super(message);
    }
}
