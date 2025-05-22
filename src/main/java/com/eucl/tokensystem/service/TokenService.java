package com.eucl.tokensystem.service;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.PurchasedToken;
import com.eucl.tokensystem.model.TokenStatus;
import com.eucl.tokensystem.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    private static final SecureRandom random = new SecureRandom();
    private static final String DIGITS = "0123456789";

    public PurchasedToken generateToken(Meter meter, int amount, int days) {
        String tokenString = generateRandomToken();

        PurchasedToken token = new PurchasedToken(
                meter,
                tokenString,
                TokenStatus.NEW,
                days,
                amount
        );

        return tokenRepository.save(token);
    }

    private String generateRandomToken() {
        StringBuilder sb = new StringBuilder(16);
        for (int i = 0; i < 16; i++) {
            sb.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        }
        return sb.toString();
    }

    public void checkExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveHoursLater = now.plusHours(5);

        List<PurchasedToken> tokensAboutToExpire = tokenRepository.findTokensAboutToExpire(
                TokenStatus.USED, now, fiveHoursLater);

        // Process tokens about to expire
        for (PurchasedToken token : tokensAboutToExpire) {
            // Create notification
            // This will be handled by the NotificationService
        }
    }
}
