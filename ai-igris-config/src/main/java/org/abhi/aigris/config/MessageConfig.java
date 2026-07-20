package org.abhi.aigris.config;

import org.abhi.aigris.messaging.Consumer.AiResponseConsumer;
import org.abhi.aigris.messaging.Consumer.IConsumer;
import org.abhi.aigris.messaging.publisher.topic.IAiResponseTopic;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@AutoConfiguration
public class MessageConfig {

    @Bean
    public IConsumer<String> getAiResponseConsumer() {
        return new AiResponseConsumer();
    }

    @Bean
    public Consumer<Message<String>> getMessageConsumer(IConsumer<String> aiResponseConsumer) {
        return aiResponseConsumer::consume;
    }

    @Bean
    public IAiResponseTopic getAiResponseTopic() {
        return new IAiResponseTopic() {};
    }

}
