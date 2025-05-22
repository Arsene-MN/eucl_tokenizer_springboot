package com.eucl.tokensystem.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TokenValidationRequest {
    @NotBlank
    @Size(min = 16, max = 16)
    private String token;
}
