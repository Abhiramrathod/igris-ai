package org.abhi.aigris.app.controller;

import org.abhi.aigris.api.delegate.IAiDelegate;
import org.abhi.aigris.llm.config.LlmConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AiControllerTest {

    private MockMvc mockMvc;
    private IAiDelegate aiDelegate;
    private LlmConfig llmConfig;

    @BeforeEach
    void setUp() {
        aiDelegate = mock(IAiDelegate.class);
        llmConfig = mock(LlmConfig.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new AiController(aiDelegate, llmConfig)).build();
    }

    @Test
    void chat_validRequest_shouldReturn200() throws Exception {
        when(aiDelegate.toDelegate(anyString())).thenReturn("AI response here");

        mockMvc.perform(post("/ai/chat")
                        .contentType("application/json")
                        .content("{\"userName\":\"test\",\"sessionId\":\"s1\",\"prompt\":\"Hello\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("AI response here"));

        verify(aiDelegate).toDelegate(anyString());
    }

    @Test
    void getConfig_shouldReturnModelAndBaseUrl() throws Exception {
        when(llmConfig.getModel()).thenReturn("gpt-4");
        when(llmConfig.getBaseUrl()).thenReturn("https://api.openai.com/v1");

        mockMvc.perform(get("/ai/config"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.model").value("gpt-4"))
                .andExpect(jsonPath("$.baseUrl").value("https://api.openai.com/v1"));
    }

    @Test
    void chat_emptyBody_shouldReturn400() throws Exception {
        mockMvc.perform(post("/ai/chat")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }
}
