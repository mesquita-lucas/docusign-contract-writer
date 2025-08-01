package com.hub4.api.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyValidator {

    @Value("$app.api-key")
    private String apiKey;

    public boolean isValid(String providedKey) {
        return apiKey != null && apiKey.equals(providedKey);
    }
}
