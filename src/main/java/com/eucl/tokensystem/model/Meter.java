package com.eucl.tokensystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Meter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 6, max = 6)
    @Column(unique = true)
    private String meterNumber;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "meter", cascade = CascadeType.ALL)
    private Set<PurchasedToken> tokens = new HashSet<>();

    public Meter(String meterNumber, User owner) {
        this.meterNumber = meterNumber;
        this.owner = owner;
    }
}
