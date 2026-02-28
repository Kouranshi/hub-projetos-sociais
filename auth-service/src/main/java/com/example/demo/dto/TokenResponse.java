package com.example.demo.dto;

import lombok.Data;

@Data
public class TokenResponse {
    private String access_token;
    private String expires_in;
    private String token_type;
}
