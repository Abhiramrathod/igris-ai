package org.abhi.aigris.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abhi.aigris.api.delegate.IAiDelegate;
import org.abhi.aigris.api.services.*;
import org.abhi.aigris.core.delegate.AiDelegate;
import org.abhi.aigris.core.services.AiService;
import org.abhi.aigris.guardrails.GuardrailService;
import org.abhi.aigris.llm.client.LlmClient;
import org.abhi.aigris.llm.config.LlmConfig;
import org.abhi.aigris.messaging.publisher.service.MessagingService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(LlmConfig.class)
public class AiConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public IMessagingService messagingService() {
        return new MessagingService();
    }

    @Bean
    public IAiService aiService() {
        return new AiService();
    }

    @Bean
    public IGuardrailService guardrailService() {
        return new GuardrailService();
    }

    @Bean
    public ILlmClient llmClient() {
        return new LlmClient();
    }

    @Bean
    public IChatModel chatModel() {
        return new org.abhi.aigris.core.ChatClient();
    }

    @Bean
    public IAiDelegate aiDelegate() {
        return new AiDelegate();
    }

}
