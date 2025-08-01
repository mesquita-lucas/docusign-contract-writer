package com.hub4.api.controller;

import com.hub4.api.security.ApiKeyValidator;
import com.hub4.api.dto.ContractDTO;
import com.hub4.service.ContractHandler;
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
        System.out.println("Request received for form data");
        System.out.println(formData.toString());

        ContractHandler contractHandler = new ContractHandler(formData);
        contractHandler.handleContract();

        System.out.println("Contract received");


        return ResponseEntity.ok().body(formData.toString());
    }
}
