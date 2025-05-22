package com.eucl.tokensystem.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class SignupRequest {
    @NotBlank
    @Size(max = 100)
    private String names;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @NotBlank
    @Size(max = 15)
    private String phone;

    @NotBlank
    @Size(max = 20)
    private String nationalId;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
