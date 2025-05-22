package com.eucl.tokensystem.controller;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.PurchasedToken;
import com.eucl.tokensystem.model.TokenStatus;
import com.eucl.tokensystem.payload.request.TokenPurchaseRequest;
import com.eucl.tokensystem.payload.request.TokenValidationRequest;
import com.eucl.tokensystem.payload.response.MessageResponse;
import com.eucl.tokensystem.payload.response.TokenResponse;
import com.eucl.tokensystem.repository.MeterRepository;
import com.eucl.tokensystem.repository.TokenRepository;
import com.eucl.tokensystem.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tokens")
public class TokenController {
    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    MeterRepository meterRepository;

    @Autowired
    TokenService tokenService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @PostMapping("/purchase")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> purchaseToken(@Valid @RequestBody TokenPurchaseRequest request) {
        // Validate amount
        if (request.getAmount() < 100) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Amount must be at least 100 RWF"));
        }

        if (request.getAmount() % 100 != 0) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Amount must be a multiple of 100 RWF"));
        }

        // Calculate days
        int days = request.getAmount() / 100;
        
        // Check if days exceed 5 years (1825 days)
        if (days > 1825) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Token cannot exceed 5 years (1825 days)"));
        }

        // Find meter
        Meter meter = meterRepository.findByMeterNumber(request.getMeterNumber())
                .orElseThrow(() -> new RuntimeException("Error: Meter not found."));

        // Generate token
        PurchasedToken token = tokenService.generateToken(meter, request.getAmount(), days);

        // Format token for response
        String formattedToken = formatToken(token.getToken());

        TokenResponse response = new TokenResponse(
                token.getToken(),
                formattedToken,
                token.getMeterNumber(),
                token.getTokenValueDays(),
                token.getAmount(),
                token.getPurchasedDate().format(DATE_FORMATTER),
                token.getExpiryDate().format(DATE_FORMATTER),
                token.getTokenStatus().toString()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/validate")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> validateToken(@Valid @RequestBody TokenValidationRequest request) {
        String token = request.getToken().replace("-", "");
        
        PurchasedToken purchasedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Error: Token not found."));

        String formattedToken = formatToken(purchasedToken.getToken());

        TokenResponse response = new TokenResponse(
                purchasedToken.getToken(),
                formattedToken,
                purchasedToken.getMeterNumber(),
                purchasedToken.getTokenValueDays(),
                purchasedToken.getAmount(),
                purchasedToken.getPurchasedDate().format(DATE_FORMATTER),
                purchasedToken.getExpiryDate().format(DATE_FORMATTER),
                purchasedToken.getTokenStatus().toString()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/meter/{meterNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> getTokensByMeterNumber(@PathVariable String meterNumber) {
        List<PurchasedToken> tokens = tokenRepository.findByMeterNumberOrderByPurchasedDateDesc(meterNumber);
        
        List<TokenResponse> tokenResponses = tokens.stream()
                .map(token -> new TokenResponse(
                        token.getToken(),
                        formatToken(token.getToken()),
                        token.getMeterNumber(),
                        token.getTokenValueDays(),
                        token.getAmount(),
                        token.getPurchasedDate().format(DATE_FORMATTER),
                        token.getExpiryDate().format(DATE_FORMATTER),
                        token.getTokenStatus().toString()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(tokenResponses);
    }

    private String formatToken(String token) {
        // Format token as 4321-8765-9865-5468
        return token.substring(0, 4) + "-" + 
               token.substring(4, 8) + "-" + 
               token.substring(8, 12) + "-" + 
               token.substring(12, 16);
    }
}
