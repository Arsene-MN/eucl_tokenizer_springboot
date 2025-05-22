package com.eucl.tokensystem.scheduler;

import com.eucl.tokensystem.model.PurchasedToken;
import com.eucl.tokensystem.model.TokenStatus;
import com.eucl.tokensystem.repository.TokenRepository;
import com.eucl.tokensystem.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TokenExpiryScheduler {
    @Autowired
    private TokenRepository tokenRepository;
    
    @Autowired
    private NotificationService notificationService;

    // Run every hour
    @Scheduled(fixedRate = 3600000)
    public void checkTokensAboutToExpire() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime fiveHoursLater = now.plusHours(5);
        
        List<PurchasedToken> tokensAboutToExpire = tokenRepository.findTokensAboutToExpire(
                TokenStatus.USED, now, fiveHoursLater);
        
        for (PurchasedToken token : tokensAboutToExpire) {
            notificationService.createExpiryNotification(token);
        }
    }
    
    // Run daily at midnight to check for expired tokens
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        
        List<PurchasedToken> tokens = tokenRepository.findAll();
        for (PurchasedToken token : tokens) {
            if (token.getTokenStatus() != TokenStatus.EXPIRED && 
                token.getExpiryDate().isBefore(now)) {
                token.setTokenStatus(TokenStatus.EXPIRED);
                tokenRepository.save(token);
            }
        }
    }
}
