package org.abhi.aigris.api.services;

public interface IAiService {

    String generateResponse(String systemPrompt, String userPrompt);
}
