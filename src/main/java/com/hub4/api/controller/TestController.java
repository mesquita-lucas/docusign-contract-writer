package com.hub4.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        System.out.println("LOG: Servidor acessado em: " + now.format(formatter));
        String responseBody = "<h1>Servidor acessado!</h1>" +
                "<p>Requisição recebida com sucesso em: " + now.format(formatter) + "</p>";
        return ResponseEntity.ok(responseBody);
    }
}
