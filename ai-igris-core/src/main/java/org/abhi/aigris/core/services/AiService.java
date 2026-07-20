package org.abhi.aigris.core.services;

import jakarta.inject.Inject;
import org.abhi.aigris.api.services.IAiService;
import org.abhi.aigris.api.services.IChatModel;


public class AiService implements IAiService {

    @Inject
    private IChatModel chatModel;


    @Override
    public String generateResponse(String systemPrompt, String userPrompt) {

        return chatModel.call(systemPrompt, userPrompt);
    }
}
