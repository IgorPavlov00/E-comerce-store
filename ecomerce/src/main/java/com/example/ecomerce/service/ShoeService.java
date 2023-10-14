package com.example.ecomerce.service;

import com.example.ecomerce.model.Shoes;
import com.example.ecomerce.repository.ShoeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // Add more methods as needed
}