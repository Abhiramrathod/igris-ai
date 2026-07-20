package org.abhi.aigris.exception;

import lombok.Getter;

@Getter
public class AiIgrisException extends RuntimeException {

    private final String errorCode;

    public AiIgrisException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AiIgrisException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
