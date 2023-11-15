package com.example.ecomerce.repository;

import com.example.ecomerce.model.User;
import com.example.ecomerce.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<VerificationToken,Long>{
    VerificationToken findByToken(String token);
    VerificationToken findByUser(User user);

}