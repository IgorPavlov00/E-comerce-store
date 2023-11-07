package com.example.ecomerce.service;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.model.Shoes;
import com.example.ecomerce.repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoeService {

    @Autowired
    private ShoeRepository shoeRepository;

    public List<Shoes> getAllShoes() {
        return shoeRepository.findAll();
    }

    public Shoes getShoeById(Long id) {
        return shoeRepository.findById(id).orElse(null);
    }

    public Optional<Shoes> findById(String id) {
        return shoeRepository.findShoesById(id);
    }

    // Add more methods as needed
}