package org.abhi.aigris.api.services;

public interface IChatModel {

    String call(String systemPrompt, String userPrompt);

}
