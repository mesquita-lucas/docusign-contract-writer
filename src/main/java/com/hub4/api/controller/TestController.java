package com.hub4.api.controller;

import com.hub4.api.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody MessageDTO dto) {
        System.out.println("Mensagem recebida: " + dto.text());

        //DocusignClient.main(null);
        return ResponseEntity.ok("mensagem recebida com sucesso");
    }
}
