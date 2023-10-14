package com.example.ecomerce.controller;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.service.JeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jeans")
public class JeanController {

    @Autowired
    private JeanService jeanService;

    @GetMapping("/alljeans")
    public List<Jeans> getAllJeans() {

        return jeanService.getAllJeans();
    }

    @GetMapping("/{id}")
    public Jeans getJeanById(@PathVariable Long id) {
        return jeanService.getJeanById(id);
    }

    // Add more endpoints as needed
}