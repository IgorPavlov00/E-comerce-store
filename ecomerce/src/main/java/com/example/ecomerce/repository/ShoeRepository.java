package com.example.ecomerce.repository;



import com.example.ecomerce.model.Shoes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoeRepository extends JpaRepository<Shoes, Long> {
    // Additional query methods can be defined here if needed
}
