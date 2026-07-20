package org.abhi.aigris.core;

import jakarta.inject.Inject;
import org.abhi.aigris.api.services.IChatModel;
import org.abhi.aigris.api.services.ILlmClient;

public class ChatClient implements IChatModel {

    @Inject
    private ILlmClient llmClient;


    @Override
    public String call(String systemPrompt, String userPrompt) {
        return llmClient.chat(systemPrompt, userPrompt);
    }


}
