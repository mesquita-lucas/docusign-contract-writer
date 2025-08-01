package com.hub4.controller;

import com.hub4.dto.ContractDTO;
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
        ContractHandler contractHandler = new ContractHandler(formData);
        contractHandler.handleContract();

        System.out.println(formData.toString());

        return ResponseEntity.ok().body(formData.toString());
    }
}
