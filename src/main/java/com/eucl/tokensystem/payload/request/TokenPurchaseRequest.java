package com.eucl.tokensystem.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TokenPurchaseRequest {
    @NotBlank
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[a-zA-Z0-9]{6}$", message = "Meter number must be 6 alphanumeric characters")
    private String meterNumber;

    @Min(value = 100, message = "Amount must be at least 100 RWF")
    private Integer amount;
}
