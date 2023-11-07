package com.example.ecomerce.repository;



import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.model.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoeRepository extends JpaRepository<Shoes, Long> {

    @Query("SELECT p FROM Shoes p WHERE p.id = :id")
    Optional<Shoes> findShoesById(@Param("id") String id);
}

