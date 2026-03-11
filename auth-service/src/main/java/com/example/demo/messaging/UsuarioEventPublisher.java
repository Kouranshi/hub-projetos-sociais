package com.example.demo.messaging;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioEventPublisher {
    
    private final RabbitTemplate rabbitTemplate;

    public void enviarCodigoEmail(String email, String codigo) {
        rabbitTemplate.convertAndSend(
            "usuario.email.verificacao",
            Map.of("email", email, "codigo", codigo)
        );
    }

    
    public void enviarCodigoReset(String email, String codigo) {
        rabbitTemplate.convertAndSend(
            "usuario.reset.senha",
            Map.of(
                "email", email,
                "codigo", codigo
            )
        );
    }
}