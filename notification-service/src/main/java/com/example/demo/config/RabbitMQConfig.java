package com.example.demo.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    @Bean
    public Queue filaVerificacaoEmail() {
        return new Queue("usuario.email.verificacao");
    }

    public Queue filaResetSenha() {
        return new Queue("usuario.reset.senha");
    }

}
