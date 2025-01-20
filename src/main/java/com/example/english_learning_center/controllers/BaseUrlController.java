package com.example.english_learning_center.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class BaseUrlController {

    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/baseUrl")
    public ResponseEntity<String> getBaseUrl() {
        String baseUrl = "http://localhost:" + serverPort;
        return ResponseEntity.ok(baseUrl);
    }
}
