package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.EsqueciSenhaDTO;
import com.example.demo.dto.ResetSenhaDTO;
import com.example.demo.entity.Usuario;
import com.example.demo.keycloak.KeycloakAdminClient;
import com.example.demo.messaging.UsuarioEventPublisher;
import com.example.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailVerificacaoService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioEventPublisher usuarioEventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakAdminClient keycloak;

    private String gerarCodigo() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // código de verificação do email
    public void enviarCodigoVerificacaoEmail(String email) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String codigo = gerarCodigo();

        usuario.setCodigoVerificacaoEmail(codigo);
        usuario.setExpiracaoCodigoEmail(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        usuarioEventPublisher.enviarCodigoEmail(usuario.getEmail(), codigo);
    }

    // confirmação do email
    public void confirmarEmail(String email, String codigo) {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getCodigoVerificacaoEmail() == null ||
            !usuario.getCodigoVerificacaoEmail().equals(codigo)) {
            throw new RuntimeException("Código inválido");
        }

        if (usuario.getExpiracaoCodigoEmail().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado");
        }

        usuario.setEmailVerificado(true);
        usuario.setCodigoVerificacaoEmail(null);
        usuario.setExpiracaoCodigoEmail(null);

        usuarioRepository.save(usuario);
    }

    // solicitar o reset da senha
    public void solicitarResetSenha(EsqueciSenhaDTO dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String codigo = gerarCodigo();

        usuario.setCodigoResetSenha(codigo);
        usuario.setExpiracaoCodigoResetSenha(LocalDateTime.now().plusMinutes(15));

        usuarioRepository.save(usuario);

        usuarioEventPublisher.enviarCodigoReset(usuario.getEmail(), codigo);
    }

    // oficialmente o reset da senha
    public void resetSenha(ResetSenhaDTO dto) {

        if (!dto.getNovaSenha().equals(dto.getConfirmarSenha())) {
            throw new RuntimeException("Senhas não coincidem");
        }

        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (usuario.getCodigoResetSenha() == null ||
            !usuario.getCodigoResetSenha().equals(dto.getCodigo())) {
            throw new RuntimeException("Código inválido");
        }

        if (usuario.getExpiracaoCodigoResetSenha().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Código expirado");
        }

        usuario.setSenha(passwordEncoder.encode(dto.getNovaSenha()));
        usuario.setCodigoResetSenha(null);
        usuario.setExpiracaoCodigoResetSenha(null);

        usuarioRepository.save(usuario);

        keycloak.atualizarSenha(usuario.getEmail(), dto.getNovaSenha());
    }
}