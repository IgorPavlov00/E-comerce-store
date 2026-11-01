package com.example.ecomerce.service;

import com.example.ecomerce.model.Product;
import com.example.ecomerce.repository.JeanRepository;
import com.example.ecomerce.repository.ShoeRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ShoeRepository shoes;
    private final JeanRepository jeans;

    public ProductService(ShoeRepository shoes, JeanRepository jeans) {
        this.shoes = shoes;
        this.jeans = jeans;
    }

    public List<Product> findAll() {
        List<Product> all = new ArrayList<>(shoes.findAll(Sort.by("id")));
        all.addAll(jeans.findAll(Sort.by("id")));
        return all;
    }

    public Product get(String type, Long id) {
        Optional<? extends Product> found = switch (type) {
            case "shoe" -> shoes.findById(id);
            case "jean" -> jeans.findById(id);
            default -> Optional.empty();
        };
        return found.orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "That product doesn't exist."));
    }
}
