package com.example.ecomerce.repository;

import com.example.ecomerce.model.Jeans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JeanRepository extends JpaRepository<Jeans, Long>{

}
