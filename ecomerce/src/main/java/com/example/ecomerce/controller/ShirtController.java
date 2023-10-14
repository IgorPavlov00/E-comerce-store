package com.example.ecomerce.controller;

import com.example.ecomerce.model.Shirt;
import com.example.ecomerce.service.ShirtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shirts")
public class ShirtController {

    @Autowired
    private ShirtService shirtService;

    @GetMapping("/")
    public List<Shirt> getAllShirts() {
        return shirtService.getAllShirts();
    }

    @GetMapping("/{id}")
    public Shirt getShirtById(@PathVariable Long id) {
        return shirtService.getShirtById(id);
    }

    // Add more endpoints as needed
}
