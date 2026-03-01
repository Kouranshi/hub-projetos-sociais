package com.example.demo.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestAuthController {

    @GetMapping("/public/hello")
    public String publicHello() {
        return "Public endpoint OK";
    }

    @GetMapping("/private/hello")
    public String privateHello(@AuthenticationPrincipal Jwt jwt) {
        return "Private OK user: " + jwt.getSubject();
    }
}