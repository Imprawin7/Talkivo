package com.example.talkivo.exception;

/**
 * Thrown when the upstream Text-to-Speech provider fails, is unreachable,
 * or rejects the request (bad API key, quota exceeded, outage, etc).
 */
public class TtsProviderException extends RuntimeException {

    public TtsProviderException(String message) {
        super(message);
    }

    public TtsProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
