package org.abhi.aigris.messaging.Consumer;

import org.springframework.messaging.Message;

public interface IConsumer <T> {

    void consume(Message<T> message);
}
