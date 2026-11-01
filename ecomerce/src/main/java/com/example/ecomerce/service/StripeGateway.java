package com.example.ecomerce.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** The only class that talks to Stripe. */
@Component
public class StripeGateway {

    private final String secretKey;

    public StripeGateway(@Value("${app.stripe.secret-key:}") String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isConfigured() {
        return !secretKey.isBlank();
    }

    public Session create(SessionCreateParams params) throws StripeException {
        return Session.create(params, options());
    }

    public Session retrieve(String sessionId) throws StripeException {
        return Session.retrieve(sessionId, options());
    }

    private RequestOptions options() {
        return RequestOptions.builder()
                .setApiKey(secretKey)
                .setConnectTimeout(5_000)
                .setReadTimeout(20_000)
                .setMaxNetworkRetries(1)
                .build();
    }
}
