package com.example.demo.keycloak;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.TokenResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakAdminClient {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public void criarUsuario(String email, String senha) {

        String token = obterTokenAdmin();

        String url = serverUrl + "/admin/realms/" + realm + "/users";

        Map<String, Object> credentials = Map.of(
                "type", "password",
                "value", senha,
                "temporary", false
        );

        Map<String, Object> body = Map.of(
                "username", email,
                "email", email,
                "enabled", true,
                "credentials", List.of(credentials)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Void.class
        );

        if (response.getStatusCode() != HttpStatus.CREATED) {
            throw new RuntimeException("Erro ao criar usuário no Keycloak: " + response.getStatusCode());
        }
    }

    private String obterTokenAdmin() {

            String url = serverUrl + "/realms/" + realm + "/protocol/openid-connect/token";

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "client_credentials");
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                            url,
                            request,
                            TokenResponse.class);

            if (response.getBody() == null || response.getBody().getAccess_token() == null) {
                    throw new RuntimeException("Falha ao obter token admin do Keycloak");
            }

            return response.getBody().getAccess_token();
    }
    
    public String buscarUserIdPorEmail(String email) {

            String token = obterTokenAdmin();

            String url = serverUrl + "/admin/realms/" + realm + "/users?email=" + email;

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            new ParameterizedTypeReference<List<Map<String, Object>>>() {
                            });

            List<Map<String, Object>> users = response.getBody();

            if (users == null || users.isEmpty()) {
                    throw new RuntimeException("Usuário não encontrado no Keycloak");
            }

            return users.get(0).get("id").toString();
    }

    public void atualizarSenha(String email, String novaSenha) {

        String userId = buscarUserIdPorEmail(email);
        String token = obterTokenAdmin();

        String url = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/reset-password";

        Map<String, Object> body = Map.of(
                "type", "password",
                "value", novaSenha,
                "temporary", false
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(body, headers),
                Void.class
        );
        }
}