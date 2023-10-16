package com.example.ecomerce.controller;

import com.example.ecomerce.model.Shoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ecomerce.service.ShoeService;

import java.util.List;

@RestController
@RequestMapping("/shoes")
public class ShoeController {

    @Autowired
    private ShoeService shoeService;

    @GetMapping("/allshoes")
    public List<Shoes> getAllShoes() {
        return shoeService.getAllShoes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shoes> getShoesById(@PathVariable Long id) {
        Shoes shoes = shoeService.getShoeById(id);
        return ResponseEntity.ok(shoes);
    }
    // Add more endpoints as needed
}