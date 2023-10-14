package com.example.ecomerce.service;

import com.example.ecomerce.model.Shirt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.ecomerce.repository.ShirtRepository;

import java.util.List;

@Service
public class ShirtService {

    @Autowired
    private ShirtRepository shirtRepository;

    public List<Shirt> getAllShirts() {
        return shirtRepository.findAll();
    }

    public Shirt getShirtById(Long id) {
        return shirtRepository.findById(id).orElse(null);
    }

    // Add more methods as needed
}