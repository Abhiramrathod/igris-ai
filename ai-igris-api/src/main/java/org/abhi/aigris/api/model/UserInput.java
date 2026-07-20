package org.abhi.aigris.api.model;

public record UserInput(String userName, String sessionId, String prompt) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userName;
        private String sessionId;
        private String prompt;

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder sessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public UserInput build() {
            return new UserInput(userName, sessionId, prompt);
        }
    }
}
