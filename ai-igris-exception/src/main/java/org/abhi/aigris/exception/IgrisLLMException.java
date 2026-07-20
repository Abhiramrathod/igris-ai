package org.abhi.aigris.exception;

public class IgrisLLMException extends AiIgrisException {
    public IgrisLLMException(String message) {
        super("LLM_CALL_ERROR", message);
    }

    public IgrisLLMException(String message, Throwable cause) {
        super("LLM_CALL_ERROR", message, cause);
    }
}
