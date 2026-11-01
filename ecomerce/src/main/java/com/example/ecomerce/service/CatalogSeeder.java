package com.example.ecomerce.service;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.Shoes;
import com.example.ecomerce.repository.JeanRepository;
import com.example.ecomerce.repository.ShoeRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Fills an empty catalog on first boot. Tables that already have rows are left alone. */
@Component
public class CatalogSeeder implements ApplicationRunner {

    private final ShoeRepository shoes;
    private final JeanRepository jeans;
    private final ObjectMapper mapper;

    public CatalogSeeder(ShoeRepository shoes, JeanRepository jeans, ObjectMapper mapper) {
        this.shoes = shoes;
        this.jeans = jeans;
        this.mapper = mapper;
    }

    @Override
    public void run(ApplicationArguments args) throws IOException {
        JsonNode catalog;
        try (InputStream in = new ClassPathResource("catalog.json").getInputStream()) {
            catalog = mapper.readTree(in);
        }
        if (shoes.count() == 0) {
            shoes.saveAll(read(catalog.get("shoes"), "shoe", Shoes::new));
        }
        if (jeans.count() == 0) {
            jeans.saveAll(read(catalog.get("jeans"), "jean", Jeans::new));
        }
    }

    private <T extends Product> List<T> read(JsonNode rows, String type, Supplier<T> factory) {
        List<T> products = new ArrayList<>();
        for (JsonNode row : rows) {
            T p = factory.get();
            p.setName(row.get("name").asText());
            p.setDescription(row.get("description").asText());
            p.setBrand(row.get("brand").asText());
            p.setSize(row.get("size").asText());
            p.setColor(row.get("color").asText());
            p.setPrice(row.get("price").asDouble());
            p.setImagePath("assets/images/" + row.get("image").asText());
            p.setType(type);
            p.setGender("male");
            products.add(p);
        }
        return products;
    }
}
