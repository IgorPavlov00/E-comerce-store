package com.example.ecomerce.repository;



import com.example.ecomerce.model.Shirt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShirtRepository extends JpaRepository<Shirt, Long> {
    // Additional query methods can be defined here if needed
}
