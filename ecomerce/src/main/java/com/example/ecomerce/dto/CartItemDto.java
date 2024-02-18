package com.example.ecomerce.dto;

public class CartItemDto {

    private String name;
    private String description;
    private String price;
    private String imagePath;

    public CartItemDto(){

    }

    public CartItemDto(String name, String description, String price, String imagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setimagePath(String picture) {
        this.imagePath = picture;
    }

    @Override
    public String toString() {
        return "CartItemDto{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", picture='" + imagePath + '\'' +
                '}';
    }
}
