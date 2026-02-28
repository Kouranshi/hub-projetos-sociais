package com.example.demo.service;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Usuario;
import com.example.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificacaoService {
    
    private final UsuarioRepository usuarioRepository;

    public void confirmar(String token) {

        Usuario usuario = usuarioRepository.findAll()
            .stream()
            .filter(u -> token.equals(u.getEmailToken()))
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Token inválido."));

        usuario.setEmailVerificado(true);
        usuario.setEmailToken(null);

        usuarioRepository.save(usuario);
    }

}
