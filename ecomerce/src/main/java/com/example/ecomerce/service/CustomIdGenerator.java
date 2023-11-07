package com.example.ecomerce.service;

import com.example.ecomerce.model.Jeans;
import com.example.ecomerce.model.Shoes;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class CustomIdGenerator implements IdentifierGenerator {

    private static int jeansCounter = 0;
    private static int shoesCounter = 0;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String prefix = "";
        if (object instanceof Jeans) {
            prefix = "J";
            jeansCounter++;
            return prefix + jeansCounter;
        } else if (object instanceof Shoes) {
            prefix = "SH";
            shoesCounter++;
            return prefix + shoesCounter;
        }
        return null;
    }
}
