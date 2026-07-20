package org.abhi.aigris.core.delegate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.abhi.aigris.api.services.IAiService;
import org.abhi.aigris.api.services.IGuardrailService;
import org.abhi.aigris.exception.GuardrailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AiDelegateTest {

    @Mock
    private IAiService aiService;

    @Mock
    private IGuardrailService guardrailService;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AiDelegate aiDelegate;

    private String validJsonInput;

    @BeforeEach
    void setUp() {
        validJsonInput = """
                {"userName":"testuser","sessionId":"sess-123","prompt":"What is AI?"}
                """;
    }

    @Test
    void toDelegate_validInput_shouldReturnResponse() throws Exception {
        com.fasterxml.jackson.databind.JsonNode mockNode = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(validJsonInput);
        when(objectMapper.readTree(validJsonInput)).thenReturn(mockNode);
        when(guardrailService.getSystemPrompt()).thenReturn("You are an AI assistant.");
        when(aiService.generateResponse(anyString(), eq("What is AI?"))).thenReturn("AI is artificial intelligence.");

        String result = aiDelegate.toDelegate(validJsonInput);

        assertEquals("AI is artificial intelligence.", result);
        verify(guardrailService).checkPrompt("What is AI?");
        verify(guardrailService).getSystemPrompt();
        verify(aiService).generateResponse("You are an AI assistant.", "What is AI?");
    }

    @Test
    void toDelegate_invalidJson_shouldThrow() throws Exception {
        doThrow(new com.fasterxml.jackson.core.JsonProcessingException("Parse error") {})
                .when(objectMapper).readTree("not-json");
        assertThrows(RuntimeException.class, () -> aiDelegate.toDelegate("not-json"));
    }

    @Test
    void toDelegate_guardrailViolation_shouldThrow() throws Exception {
        com.fasterxml.jackson.databind.JsonNode mockNode = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(validJsonInput);
        when(objectMapper.readTree(validJsonInput)).thenReturn(mockNode);
        doThrow(new GuardrailException("PI-001", "Prompt injection detected"))
                .when(guardrailService).checkPrompt("What is AI?");

        GuardrailException ex = assertThrows(GuardrailException.class,
                () -> aiDelegate.toDelegate(validJsonInput));
        assertTrue(ex.getMessage().contains("Prompt injection"));
    }

    @Test
    void toDelegate_missingFields_shouldStillWork() throws Exception {
        String minimalJson = """
                {"userName":"","sessionId":"","prompt":"Hello"}
                """;
        com.fasterxml.jackson.databind.JsonNode mockNode = new com.fasterxml.jackson.databind.ObjectMapper()
                .readTree(minimalJson);
        when(objectMapper.readTree(minimalJson)).thenReturn(mockNode);
        when(guardrailService.getSystemPrompt()).thenReturn("System prompt");
        when(aiService.generateResponse(anyString(), eq("Hello"))).thenReturn("Hi there");

        String result = aiDelegate.toDelegate(minimalJson);

        assertEquals("Hi there", result);
    }
}
