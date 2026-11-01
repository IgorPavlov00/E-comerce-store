package com.example.ecomerce.web;

import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.User;
import com.example.ecomerce.service.AuthService;
import com.example.ecomerce.service.MailService;
import com.example.ecomerce.service.OrderService;
import com.example.ecomerce.service.ProductService;
import com.example.ecomerce.web.Dtos.AuthResponse;
import com.example.ecomerce.web.Dtos.CheckoutRequest;
import com.example.ecomerce.web.Dtos.CheckoutResponse;
import com.example.ecomerce.web.Dtos.ConfirmRequest;
import com.example.ecomerce.web.Dtos.LoginRequest;
import com.example.ecomerce.web.Dtos.OrderResponse;
import com.example.ecomerce.web.Dtos.RegisterRequest;
import com.example.ecomerce.web.Dtos.UserResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final ProductService products;
    private final AuthService auth;
    private final OrderService orders;
    private final MailService mail;

    public ApiController(ProductService products, AuthService auth, OrderService orders, MailService mail) {
        this.products = products;
        this.auth = auth;
        this.orders = orders;
        this.mail = mail;
    }

    @GetMapping("/health")
    Map<String, String> health() {
        return Map.of("status", "ok");
    }

    @GetMapping("/products")
    List<Product> products() {
        return products.findAll();
    }

    @GetMapping("/products/{type}/{id}")
    Product product(@PathVariable String type, @PathVariable Long id) {
        return products.get(type, id);
    }

    @PostMapping("/auth/register")
    AuthResponse register(@Valid @RequestBody RegisterRequest r) {
        User user = auth.register(r.name(), r.lastname(), r.email(), r.password());
        if (mail.isEnabled()) {
            return new AuthResponse(null, UserResponse.from(user), true);
        }
        return new AuthResponse(auth.createSessionToken(user), UserResponse.from(user), false);
    }

    @PostMapping("/auth/login")
    AuthResponse login(@Valid @RequestBody LoginRequest r) {
        User user = auth.login(r.email(), r.password());
        return new AuthResponse(auth.createSessionToken(user), UserResponse.from(user), false);
    }

    @PostMapping("/auth/confirm")
    Map<String, String> confirm(@Valid @RequestBody ConfirmRequest r) {
        auth.confirm(r.token());
        return Map.of("status", "confirmed");
    }

    @GetMapping("/auth/me")
    UserResponse me(@AuthenticationPrincipal Jwt jwt) {
        return UserResponse.from(auth.get(userId(jwt)));
    }

    @PostMapping("/checkout")
    CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest r, @AuthenticationPrincipal Jwt jwt) {
        return new CheckoutResponse(orders.startCheckout(r.items(), jwt == null ? null : userId(jwt)));
    }

    @GetMapping("/checkout/confirm")
    OrderResponse confirmCheckout(@RequestParam("session_id") String sessionId) {
        return orders.confirm(sessionId);
    }

    @GetMapping("/orders/mine")
    List<OrderResponse> myOrders(@AuthenticationPrincipal Jwt jwt) {
        return orders.paidOrdersOf(userId(jwt));
    }

    private static Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}
