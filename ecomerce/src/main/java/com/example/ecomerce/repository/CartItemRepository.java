package com.example.ecomerce.repository;


import com.example.ecomerce.model.CartItem;
import com.example.ecomerce.model.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT p FROM CartItem p WHERE p.id = :id")
    Optional<CartItem> findById(@Param("id") String id);
}
