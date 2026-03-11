package com.example.demo.dto;

import lombok.Data;

@Data
public class ResetSenhaDTO {
    private String email;
    private String codigo;
    private String novaSenha;
    private String confirmarSenha;
}
