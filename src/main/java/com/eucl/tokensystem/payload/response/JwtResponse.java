package com.eucl.tokensystem.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String names;
    private String email;
    private String phone;
    private String nationalId;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String names, String email, String phone, String nationalId, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.names = names;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.roles = roles;
    }
}
