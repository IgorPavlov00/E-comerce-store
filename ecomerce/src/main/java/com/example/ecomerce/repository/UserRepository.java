package com.example.ecomerce.repository;

import com.example.ecomerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmailIgnoreCase(String email);
}
