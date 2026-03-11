package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    private String codigoVerificacaoEmail;
    private LocalDateTime expiracaoCodigoEmail;

    private String codigoResetSenha;
    private LocalDateTime expiracaoCodigoResetSenha;

    private boolean emailVerificado;
}