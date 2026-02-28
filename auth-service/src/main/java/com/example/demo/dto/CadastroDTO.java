package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CadastroDTO {
    
    @NotBlank
    private String nome;

    @Email
    private String email;

    @NotBlank
    private String senha;
}
