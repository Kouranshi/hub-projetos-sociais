package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CadastroDTO;
import com.example.demo.dto.EmailVerificacaoDTO;
import com.example.demo.dto.EsqueciSenhaDTO;
import com.example.demo.dto.ResetSenhaDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailVerificacaoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    private final EmailVerificacaoService emailVerificacaoService;

    @PostMapping("/cadastro")
    public UsuarioDTO cadastrar(@RequestBody @Valid CadastroDTO dto) {
        return authService.cadastrar(dto);
    }

    @PostMapping("/verificar-email")
    public void verificar(@RequestBody EmailVerificacaoDTO dto) {
        emailVerificacaoService.confirmarEmail(dto.getEmail(), dto.getCodigo());
    }

    @PostMapping("/esqueci-senha")
    public void esqueciSenha(@RequestBody EsqueciSenhaDTO dto) {
        emailVerificacaoService.solicitarResetSenha(dto);
    }

    @PostMapping("/reset-senha")
    public void resetSenha(@RequestBody ResetSenhaDTO dto) {
        emailVerificacaoService.resetSenha(dto);
    }
}
