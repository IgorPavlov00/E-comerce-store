package com.example.ecomerce.service;


import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.repository.JeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JeanService {

    @Autowired
    private JeanRepository jeanRepository;

    public List<Jeans> getAllJeans() {
        return jeanRepository.findAll();
    }

    public Optional<Jeans> findJeansById(String id) {
        return jeanRepository.findJeansById(id);
    }







    // Add more methods as needed
}
