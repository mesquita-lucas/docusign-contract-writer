package com.hub4.api.controller;

import com.hub4.api.dto.ContractDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController{

    @GetMapping("/debug-req")
    public ResponseEntity<String> debugRequest(
            @RequestBody ContractDTO formData
    ) {
        System.out.println("DEBUG: Recebido payload para teste. Location: " + formData.location());

        return ResponseEntity.ok(formData.toString());
    }
}
