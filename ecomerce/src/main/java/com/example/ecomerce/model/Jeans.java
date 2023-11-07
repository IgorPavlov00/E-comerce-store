package com.example.ecomerce.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Jeans extends Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String size;
    private String color;

    // Getters and Setters for brand, size, color

    // Additional properties/methods specific to jeans
}

