package com.eucl.tokensystem.service;

import com.eucl.tokensystem.model.Meter;
import com.eucl.tokensystem.model.Notification;
import com.eucl.tokensystem.model.PurchasedToken;
import com.eucl.tokensystem.model.User;
import com.eucl.tokensystem.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private EmailService emailService;

    public void createExpiryNotification(PurchasedToken token) {
        Meter meter = token.getMeter();
        User user = meter.getOwner();
        
        String message = String.format(
                "Dear %s, EUCL is pleased to remind you that the token in the meter %s is going to expire in 5 hours. Please purchase a new token.",
                user.getNames(),
                meter.getMeterNumber()
        );
        
        Notification notification = new Notification(meter.getMeterNumber(), message, user);
        notificationRepository.save(notification);
        
        // Send email notification
        emailService.sendEmail(user.getEmail(), "Token Expiry Notification", message);
        
        // Mark notification as sent
        notification.setSent(true);
        notificationRepository.save(notification);
    }

    public void sendPendingNotifications() {
        // Get all unsent notifications
        notificationRepository.findBySentIsFalse().forEach(notification -> {
            // Send email
            emailService.sendEmail(
                    notification.getUser().getEmail(),
                    "EUCL Token Notification",
                    notification.getMessage()
            );
            
            // Mark as sent
            notification.setSent(true);
            notificationRepository.save(notification);
        });
    }
}
