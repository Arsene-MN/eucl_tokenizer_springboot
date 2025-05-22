package com.eucl.tokensystem.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String formattedToken;
    private String meterNumber;
    private Integer days;
    private Integer amount;
    private String purchaseDate;
    private String expiryDate;
    private String status;
}
