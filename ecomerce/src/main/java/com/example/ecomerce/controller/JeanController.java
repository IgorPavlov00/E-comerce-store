package com.example.ecomerce.controller;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.service.JeanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Jeans> getJeansById(@PathVariable String id) {
        Optional<Jeans> jeans = jeanService.findJeansById(id);

        if (jeans.isPresent()) {
            return ResponseEntity.ok(jeans.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    // Add more endpoints as needed
}