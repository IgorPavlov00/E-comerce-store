package com.example.ecomerce.controller;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.model.Shoes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ecomerce.service.ShoeService;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Shoes> getJeansById(@PathVariable String id) {
        Optional<Shoes> shoes = shoeService.findById(id);

        if (shoes.isPresent()) {
            return ResponseEntity.ok(shoes.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Add more endpoints as needed
}