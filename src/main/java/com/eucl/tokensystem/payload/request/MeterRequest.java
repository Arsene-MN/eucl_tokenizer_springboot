package com.eucl.tokensystem.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MeterRequest {
    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Meter number must be 6 alphanumeric characters")
    private String meterNumber;

    @NotBlank
    private String userEmail;
}
