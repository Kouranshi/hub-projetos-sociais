package com.example.demo.messaging;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;

    public void enviarEmailVerificacao(String email, String token) {
        Map<String, String> msg = Map.of(
            "email", email,
            "token", token
        );

        rabbitTemplate.convertAndSend(
            RabbitConfig.EMAIL_EXCHANGE,
            RabbitConfig.EMAIL_ROUTING,
            msg
        );
    }

}