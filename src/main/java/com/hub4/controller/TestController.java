package com.hub4.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody MessageDTO dto) {
        System.out.println("Mensagem recebida: " + dto.text());
        return ResponseEntity.ok("mensagem recebida com sucesso");
    }
}
