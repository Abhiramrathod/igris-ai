package org.abhi.aigris.app.controller;

import org.abhi.aigris.api.delegate.IAiDelegate;
import org.abhi.aigris.llm.config.LlmConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Autowired
    private IAiDelegate aiDelegate;

    @Autowired
    private LlmConfig llmConfig;

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody String prompt) {
        String aiResponse = aiDelegate.toDelegate(prompt);
        return ResponseEntity.ok(aiResponse);
    }

    @GetMapping("/config")
    public ResponseEntity<Map<String, String>> getConfig() {
        return ResponseEntity.ok(Map.of(
            "model", llmConfig.getModel(),
            "baseUrl", llmConfig.getBaseUrl()
        ));
    }
}
