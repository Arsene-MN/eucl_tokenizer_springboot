package com.eucl.tokensystem.controller;

import com.eucl.tokensystem.model.Notification;
import com.eucl.tokensystem.model.User;
import com.eucl.tokensystem.repository.NotificationRepository;
import com.eucl.tokensystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<?> getUserNotifications(@PathVariable String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        List<Notification> notifications = notificationRepository.findByUserOrderByIssuedDateDesc(user);
        return ResponseEntity.ok(notifications);
    }
}
