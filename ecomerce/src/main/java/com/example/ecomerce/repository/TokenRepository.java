package com.example.ecomerce.repository;

import com.example.ecomerce.model.User;
import com.example.ecomerce.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findByUser(User user);
}
