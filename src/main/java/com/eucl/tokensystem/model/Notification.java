package com.eucl.tokensystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "meter_number", length = 6)
    private String meterNumber;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "issued_date")
    private LocalDateTime issuedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean sent;

    public Notification(String meterNumber, String message, User user) {
        this.meterNumber = meterNumber;
        this.message = message;
        this.issuedDate = LocalDateTime.now();
        this.user = user;
        this.sent = false;
    }
}
