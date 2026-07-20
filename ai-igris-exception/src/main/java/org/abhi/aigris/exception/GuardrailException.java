package org.abhi.aigris.exception;

import lombok.Getter;

@Getter
public class GuardrailException extends AiIgrisException {

    private final String ruleId;

    public GuardrailException(String ruleId, String message) {
        super("GUARDRAIL_VIOLATION", message);
        this.ruleId = ruleId;
    }

    public GuardrailException(String ruleId, String message, Throwable cause) {
        super("GUARDRAIL_VIOLATION", message, cause);
        this.ruleId = ruleId;
    }
}
