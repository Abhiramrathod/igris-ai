package org.abhi.aigris.exception;

import lombok.Getter;

@Getter
public class ContentPolicyException extends AiIgrisException {

    public ContentPolicyException(String message) {
        super("CONTENT_POLICY_VIOLATION", message);
    }

    public ContentPolicyException(String message, Throwable cause) {
        super("CONTENT_POLICY_VIOLATION", message, cause);
    }
}
