package org.abhi.aigris.exception;

import lombok.Getter;

@Getter
public class RateLimitException extends AiIgrisException {

    public RateLimitException(String message) {
        super("RATE_LIMIT_EXCEEDED", message);
    }

    public RateLimitException(String message, Throwable cause) {
        super("RATE_LIMIT_EXCEEDED", message, cause);
    }
}
