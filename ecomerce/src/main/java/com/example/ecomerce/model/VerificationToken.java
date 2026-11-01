package com.example.ecomerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "verification_token")
public class VerificationToken {

    private static final Duration LIFETIME = Duration.ofHours(24);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public VerificationToken(String token, User user) {
        this.user = user;
        renew(token);
    }

    public final void renew(String newToken) {
        this.token = newToken;
        this.expiryDate = Date.from(Instant.now().plus(LIFETIME));
    }

    public boolean isExpired() {
        return new Date().after(expiryDate);
    }
}
