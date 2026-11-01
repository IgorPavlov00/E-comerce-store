package com.example.ecomerce.repository;

import com.example.ecomerce.model.Jeans;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JeanRepository extends JpaRepository<Jeans, Long> {
}
