package com.example.ecomerce.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
@ToString


public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imagePath;
    private String gender;
    private double price;
    private String type;




    // Getters and Setters for id, name, description, and price

    // You might also include additional properties like brand, color, size, etc.

    // Constructors, if needed
}
