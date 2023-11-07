package com.example.ecomerce.repository;

import com.example.ecomerce.model.Jeans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JeanRepository extends JpaRepository<Jeans, Long>{

    @Query("SELECT p FROM Jeans p WHERE p.id = :id")
    Optional<Jeans> findJeansById(@Param("id") String id);
}
