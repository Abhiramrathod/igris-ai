package org.abhi.aigris.messaging.publisher.topic;

import org.abhi.kafkasdk.core.ITopicPublish;
import org.springframework.stereotype.Component;

public interface IAiResponseTopic extends ITopicPublish {

    @Override
    default String getBinding() {
        return "ai-response-out-0";
    }
}
