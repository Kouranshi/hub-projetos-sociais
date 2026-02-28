package com.example.demo.client;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "notification-service", url = "http://localhost:8082")
public interface NotificationClient {
    
    @PostMapping("notifications/email/verificacao")
    void enviarEmailVerificacao(Map<String, String> body);
}
