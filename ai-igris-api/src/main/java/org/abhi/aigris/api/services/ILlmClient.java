package org.abhi.aigris.api.services;

public interface ILlmClient {
    String chat(String systemPrompt, String userPrompt);
}
