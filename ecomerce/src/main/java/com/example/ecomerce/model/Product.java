package com.example.ecomerce.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String imagePath;
    private String gender;
    private double price;
    /** "shoe" or "jean". Identifies the catalog table together with the id. */
    private String type;
    private String brand;
    private String size;
    private String color;

    public long priceCents() {
        return Math.round(price * 100);
    }
}
