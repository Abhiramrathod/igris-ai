package org.abhi.aigris.core.delegate;

import jakarta.inject.Inject;
import org.abhi.aigris.api.delegate.IAiDelegate;
import org.abhi.aigris.api.model.UserInput;
import org.abhi.aigris.api.services.IAiService;
import org.abhi.aigris.api.services.IGuardrailService;
import org.slf4j.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AiDelegate implements IAiDelegate {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AiDelegate.class);

    @Inject
    private IAiService aiService;

    @Inject
    private IGuardrailService guardrailService;

    @Inject
    private ObjectMapper objectMapper;


    @Override
    public String toDelegate(String input) {

        UserInput userInput = createUserInput(input);

        initialCheck(userInput);

        String systemPrompt = guardrailService.getSystemPrompt();
        return aiService.generateResponse(systemPrompt, userInput.prompt());
    }

    private UserInput createUserInput(String input) {

        try {
            JsonNode jsonNode = objectMapper.readTree(input);

            return UserInput.builder()
                    .userName(jsonNode.path("userName").asText())
                    .sessionId(jsonNode.path("sessionId").asText())
                    .prompt(jsonNode.path("prompt").asText())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initialCheck(UserInput userInput) {

        if (!checkForUserBalance(userInput.userName())) {
            logger.error("limit exceeded for the user : {}, sessionId : {}", userInput.userName(), userInput.sessionId());
            throw new RuntimeException("limit exceeded for the user : " + userInput.userName() + ", sessionId : " + userInput.sessionId());
        }
        if (!checkIfSessionIsValid(userInput.sessionId())) {
            logger.error("Invalid or expired session for the user : {}, sessionId : {}", userInput.userName(), userInput.sessionId());
            throw new RuntimeException("Invalid or expired session for the user : " + userInput.userName() + ", sessionId : " + userInput.sessionId());
        }

        guardrailChecks(userInput.prompt());
    }

    private void guardrailChecks(String prompt) {

        guardrailService.checkPrompt(prompt);
    }

    private boolean checkForUserBalance(String s) {
        /*To-do*/
        return true;
    }

    private boolean checkIfSessionIsValid(String s) {
        /*To-do*/
        return true;
    }

}
