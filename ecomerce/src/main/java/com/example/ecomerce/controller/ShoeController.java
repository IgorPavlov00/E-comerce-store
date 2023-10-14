package com.example.ecomerce.controller;

import com.example.ecomerce.model.Shoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ecomerce.service.ShoeService;

import java.util.List;

@RestController
@RequestMapping("/shoes")
public class ShoeController {

    @Autowired
    private ShoeService shoeService;

    @GetMapping("/")
    public List<Shoes> getAllShoes() {
        return shoeService.getAllShoes();
    }

    @GetMapping("/{id}")
    public Shoes getShoeById(@PathVariable Long id) {
        return shoeService.getShoeById(id);
    }

    // Add more endpoints as needed
}