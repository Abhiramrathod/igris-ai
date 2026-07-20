package org.abhi.aigris.api.services;

public interface IGuardrailService {

    void checkPrompt(String prompt);

    String getSystemPrompt();
}
