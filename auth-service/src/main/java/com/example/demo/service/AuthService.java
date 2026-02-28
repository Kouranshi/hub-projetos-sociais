package com.example.demo.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.dto.CadastroDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.keycloak.KeycloakAdminClient;
import com.example.demo.messaging.UsuarioEventPublisher;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioEventPublisher usuarioEventPublisher;
    private final KeycloakAdminClient keycloak;

    public UsuarioDTO cadastrar(CadastroDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este email já está cadastrado.");
        }

        String token = UUID.randomUUID().toString();

        Usuario usuario = Usuario.builder()
            .nome(dto.getNome())
            .email(dto.getEmail())
            .senha(passwordEncoder.encode(dto.getSenha()))
            .emailVerificado(false)
            .emailToken(token)
            .build();
            
        
        usuarioRepository.save(usuario);

        keycloak.criarUsuario(dto.getEmail(), dto.getSenha(), dto.getNome());

        usuarioEventPublisher.enviarEmailVerificacao(usuario.getEmail(), token);

        return UsuarioDTO.builder()
            .id(usuario.getId())
            .nome(usuario.getNome())
            .email(usuario.getEmail())
            .emailVerificado(false)
            .build();
    }

}
