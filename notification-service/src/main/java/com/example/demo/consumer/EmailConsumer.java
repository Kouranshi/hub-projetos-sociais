package com.example.demo.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
    
    @RabbitListener(queues = "usuario.email.verificacao")
    public void consumirCodigoEmail(Map<String, String> payload) {

        String email = payload.get("email");
        String codigo = payload.get("codigo");

        System.out.println("EMAIL VERIFICACAO");
        System.out.println("Enviar para: " + email);
        System.out.println("Código: " + codigo);

    }

    public void consumirResetSenha(Map<String, String> payload) {

        String email = payload.get("email");
        String codigo = payload.get("codigo");

        System.out.println("RESET SENHA");
        System.out.println("Enviar para: " + email);
        System.out.println("Código: " + codigo);

    }

}
