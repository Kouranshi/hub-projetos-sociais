package com.example.demo.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.demo.dto.CadastroDTO;
import com.example.demo.dto.UsuarioDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.keycloak.KeycloakAdminClient;
import com.example.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAdminClient keycloak;
    private final EmailVerificacaoService emailVerificacaoService;

    public UsuarioDTO cadastrar(CadastroDTO dto) {

        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Este email já está cadastrado.");
        }

        Usuario usuario = Usuario.builder()
            .nome(dto.getNome())
            .email(dto.getEmail())
            .senha(passwordEncoder.encode(dto.getSenha()))
            .emailVerificado(false)
            .build();
            
        
        usuarioRepository.save(usuario);
        
        keycloak.criarUsuario(dto.getEmail(), dto.getSenha());

        emailVerificacaoService.enviarCodigoVerificacaoEmail(usuario.getEmail());


        return UsuarioDTO.builder()
            .id(usuario.getId())
            .nome(usuario.getNome())
            .email(usuario.getEmail())
            .emailVerificado(false)
            .build();
    }

}
