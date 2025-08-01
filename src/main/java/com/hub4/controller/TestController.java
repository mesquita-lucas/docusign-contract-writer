package com.hub4.controller;

import com.hub4.docusign.DocusignClient;
import com.hub4.dto.MessageDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody MessageDTO dto) throws IOException {
        System.out.println("Mensagem recebida: " + dto.text());

        DocusignClient.main(null);
        return ResponseEntity.ok("mensagem recebida com sucesso");
    }
}
