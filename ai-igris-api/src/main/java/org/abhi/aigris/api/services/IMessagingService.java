package org.abhi.aigris.api.services;

public interface IMessagingService {

    void publishResponse(String sessionId, String response);
}
