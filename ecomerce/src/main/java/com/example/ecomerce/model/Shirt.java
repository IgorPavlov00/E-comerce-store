package com.example.ecomerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;



@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Shirt extends Product {

    private String brand;
    private String size;
    private String color;

    // Getters and Setters for brand, size, color

    // Additional properties/methods specific to shirts
}
