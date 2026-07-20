package org.abhi.aigris.messaging.publisher.service;

import jakarta.inject.Inject;
import org.abhi.aigris.api.services.IMessagingService;
import org.abhi.aigris.messaging.publisher.topic.IAiResponseTopic;
import org.abhi.kafkasdk.core.service.IPublishService;

import java.util.Map;

public class MessagingService implements IMessagingService {

    @Inject
    private  IPublishService publishService;

    @Override
    public void publishResponse(String sessionId, String response) {
        publishService.publish(
                IAiResponseTopic.class,
                response,
                Map.of("sessionId", sessionId)
        );
    }
}
