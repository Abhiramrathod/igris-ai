package org.abhi.aigris.guardrails;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import org.abhi.aigris.api.services.IGuardrailService;
import org.abhi.aigris.exception.GuardrailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GuardrailService implements IGuardrailService {

    private static final Logger logger = LoggerFactory.getLogger(GuardrailService.class);
    private static final int MAX_PROMPT_LENGTH = 10000;

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private ObjectMapper objectMapper;

    @Override
    public void checkPrompt(String prompt) {
        if (prompt == null || prompt.isBlank()) {
            throw new GuardrailException("IV-001", "Input cannot be empty or blank.");
        }
        if (prompt.length() > MAX_PROMPT_LENGTH) {
            throw new GuardrailException("IV-002", "Input exceeds maximum length of " + MAX_PROMPT_LENGTH + " characters.");
        }

        try {
            Resource[] resources = applicationContext.getResources("classpath:guardrails/*.json");
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                if (filename == null) continue;

                String content = Files.readString(Path.of(resource.getURI()));
                JsonNode root = objectMapper.readTree(content);
                String ruleName = root.has("name") ? root.get("name").asText() : filename;

                switch (ruleName) {
                    case "prompt_injection" -> checkPromptInjection(prompt, root);
                    case "pii_filter" -> checkPII(prompt, root);
                    case "content_policy" -> checkContentPolicy(prompt, root);
                    default -> logger.debug("Skipping rule: {}", ruleName);
                }
            }
        } catch (GuardrailException e) {
            throw e;
        } catch (Exception e) {
            throw new GuardrailException("GR-000", "Error reading guardrail files: " + e.getMessage());
        }
    }

    @Override
    public String getSystemPrompt() {
        try {
            Resource resource = applicationContext.getResource("classpath:guardrails/system_prompt.json");
            String content = Files.readString(Path.of(resource.getURI()));
            JsonNode root = objectMapper.readTree(content);

            String basePrompt = root.get("systemPrompt").asText();
            int maxRules = root.has("maxRulesInPrompt") ? root.get("maxRulesInPrompt").asInt() : 5;

            List<String> rules = new ArrayList<>();
            JsonNode rulesNode = root.get("rules");
            if (rulesNode != null && rulesNode.isArray()) {
                for (JsonNode rule : rulesNode) {
                    rules.add(rule.asText());
                    if (rules.size() >= maxRules) break;
                }
            }

            StringBuilder sb = new StringBuilder(basePrompt);
            if (!rules.isEmpty()) {
                sb.append("\n\nRules:\n");
                for (int i = 0; i < rules.size(); i++) {
                    sb.append(i + 1).append(". ").append(rules.get(i)).append("\n");
                }
            }

            return sb.toString();
        } catch (Exception e) {
            logger.error("Failed to load system prompt: {}", e.getMessage());
            return "You are an AI assistant.";
        }
    }

    private void checkPromptInjection(String prompt, JsonNode root) {
        String lowerPrompt = prompt.toLowerCase();
        JsonNode patterns = root.get("patterns");
        String message = root.has("violationMessage") ? root.get("violationMessage").asText() : "Prompt injection detected.";

        if (patterns != null && patterns.isArray()) {
            for (JsonNode pattern : patterns) {
                if (lowerPrompt.contains(pattern.asText().toLowerCase())) {
                    logger.warn("Prompt injection detected: {}", pattern.asText());
                    throw new GuardrailException("PI-001", message);
                }
            }
        }
    }

    private void checkPII(String prompt, JsonNode root) {
        JsonNode patterns = root.get("patterns");
        String message = root.has("violationMessage") ? root.get("violationMessage").asText() : "PII detected.";

        if (patterns != null && patterns.isArray()) {
            for (JsonNode patternNode : patterns) {
                String regex = patternNode.get("regex").asText();
                try {
                    Pattern pattern = Pattern.compile(regex);
                    if (pattern.matcher(prompt).find()) {
                        String piiType = patternNode.has("name") ? patternNode.get("name").asText() : "Unknown";
                        logger.warn("PII detected: {} in prompt", piiType);
                        throw new GuardrailException("PII-" + patternNode.get("id").asText(), message + " Type: " + piiType);
                    }
                } catch (PatternSyntaxException e) {
                    logger.error("Invalid regex pattern for PII rule {}: {}", patternNode.get("id").asText(), regex);
                }
            }
        }
    }

    private void checkContentPolicy(String prompt, JsonNode root) {
        String lowerPrompt = prompt.toLowerCase();
        JsonNode blockedTerms = root.get("blockedTerms");
        String message = root.has("violationMessage") ? root.get("violationMessage").asText() : "Content policy violation.";

        if (blockedTerms != null && blockedTerms.isArray()) {
            for (JsonNode term : blockedTerms) {
                if (lowerPrompt.contains(term.asText().toLowerCase())) {
                    logger.warn("Content policy violation: {}", term.asText());
                    throw new GuardrailException("CP-001", message);
                }
            }
        }
    }
}
