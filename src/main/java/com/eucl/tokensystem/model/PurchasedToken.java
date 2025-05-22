package com.eucl.tokensystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "purchased_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchasedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 11)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "meter_id", nullable = false)
    private Meter meter;

    @Column(name = "meter_number", length = 6)
    private String meterNumber;

    @NotBlank
    @Size(min = 16, max = 16)
    @Column(length = 16, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "token_status")
    private TokenStatus tokenStatus;

    @Column(name = "token_value_days", length = 11)
    private Integer tokenValueDays;

    @Column(name = "purchased_date")
    private LocalDateTime purchasedDate;

    @Column(length = 11)
    private Integer amount;

    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;

    public PurchasedToken(Meter meter, String token, TokenStatus tokenStatus, 
                         Integer tokenValueDays, Integer amount) {
        this.meter = meter;
        this.meterNumber = meter.getMeterNumber();
        this.token = token;
        this.tokenStatus = tokenStatus;
        this.tokenValueDays = tokenValueDays;
        this.purchasedDate = LocalDateTime.now();
        this.amount = amount;
        this.expiryDate = this.purchasedDate.plusDays(tokenValueDays);
    }
}
