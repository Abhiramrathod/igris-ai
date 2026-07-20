package org.abhi.aigris.exception;

import lombok.Getter;

@Getter
public class SessionException extends AiIgrisException {

    public SessionException(String message) {
        super("SESSION_ERROR", message);
    }

    public SessionException(String message, Throwable cause) {
        super("SESSION_ERROR", message, cause);
    }
}
