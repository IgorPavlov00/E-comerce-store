package com.example.ecomerce.repository;

import com.example.ecomerce.model.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoeRepository extends JpaRepository<Shoes, Long> {
}
