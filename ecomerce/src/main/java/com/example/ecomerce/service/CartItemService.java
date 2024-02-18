package com.example.ecomerce.service;
import com.example.ecomerce.model.CartItem;
import com.example.ecomerce.model.Shoes;
import com.example.ecomerce.repository.CartItemRepository;
import com.example.ecomerce.repository.ShoeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    public CartItem getShoeById(Long id) {
        return cartItemRepository.findById(id).orElse(null);
    }

    public Optional<CartItem> findById(String id) {
        return cartItemRepository.findById(id);
    }

    @Transactional
    public void saveProducts(List<CartItem> products) {
        // Save the list of products to the database
        cartItemRepository.saveAll(products);
    }
}
