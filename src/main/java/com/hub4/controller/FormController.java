package com.hub4.controller;

import com.hub4.dto.ContractDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FormController {

    @PostMapping("/form-data")
    public ResponseEntity<String> receiveFormData(
            @RequestBody ContractDTO formData,
            @RequestHeader(value = "x-api-key", required = false) String apiKey
    ){
        if(!"hub41801".equals(apiKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid api key");
        }

        return ResponseEntity.ok().body(formData.toString());
    }
}
