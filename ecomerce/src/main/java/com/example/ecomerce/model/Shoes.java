package com.example.ecomerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;



@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString

public class Shoes extends Product {

    private String brand;
    private String size;
    private String color;

    // Getters and Setters for brand, size, color

    // Additional properties/methods specific to shoes
}

