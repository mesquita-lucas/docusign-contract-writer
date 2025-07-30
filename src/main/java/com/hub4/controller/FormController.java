package com.hub4.controller;

import com.docusign.esign.client.auth.ApiKeyAuth;
import com.hub4.docusign.ConfigLoader;
import com.hub4.dto.ContractDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class FormController {
    private final ApiKeyValidator apiKeyValidator;

    public FormController(ApiKeyValidator apiKeyValidator) {
        this.apiKeyValidator = apiKeyValidator;
    }

    @PostMapping("/form-data")
    public ResponseEntity<String> receiveFormData(
            @RequestBody ContractDTO formData,
            @RequestHeader(value = "x-api-key", required = false) String apiKey
    ) throws IOException {
        ConfigLoader configLoader = new ConfigLoader("/etc/secrets/app.config");

        String expectedApiKey = configLoader.get("apiKey");
        System.out.println("apiKey no arquivo: [" + expectedApiKey + "]");

        if (!apiKeyValidator.isValid(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid API key");
        }

        System.out.println(formData.toString());

        return ResponseEntity.ok().body(formData.toString());
    }
}
