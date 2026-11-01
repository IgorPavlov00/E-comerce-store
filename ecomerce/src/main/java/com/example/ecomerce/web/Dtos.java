package com.example.ecomerce.web;

import com.example.ecomerce.model.Order;
import com.example.ecomerce.model.OrderItem;
import com.example.ecomerce.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.List;

public final class Dtos {
    private Dtos() {
    }

    public record RegisterRequest(
            @NotBlank(message = "Please enter your first name.") @Size(max = 80) String name,
            @NotBlank(message = "Please enter your last name.") @Size(max = 80) String lastname,
            @NotBlank(message = "Please enter your email.") @Email(message = "Please enter a valid email.") @Size(max = 200) String email,
            @NotNull(message = "Please choose a password.")
            @Size(min = 8, max = 72, message = "Passwords need 8 to 72 characters.") String password) {
    }

    public record LoginRequest(@NotBlank String email, @NotBlank String password) {
    }

    public record ConfirmRequest(@NotBlank String token) {
    }

    public record UserResponse(Long id, String name, String lastname, String email) {
        public static UserResponse from(User u) {
            return new UserResponse(u.getId(), u.getName(), u.getLastname(), u.getEmail());
        }
    }

    /** token is null when the user must confirm their email before signing in. */
    public record AuthResponse(String token, UserResponse user, boolean verificationRequired) {
    }

    public record CheckoutItem(
            @NotBlank String type,
            @NotNull Long id,
            @Min(value = 1, message = "Quantity must be at least 1.")
            @Max(value = 20, message = "You can order up to 20 of an item.") int quantity) {
    }

    public record CheckoutRequest(
            @NotEmpty(message = "Your cart is empty.") @Size(max = 50) List<@Valid CheckoutItem> items) {
    }

    public record CheckoutResponse(String url) {
    }

    public record OrderItemResponse(String type, Long productId, String name, String description,
                                    String imagePath, long unitPriceCents, int quantity) {
        static OrderItemResponse from(OrderItem i) {
            return new OrderItemResponse(i.getProductType(), i.getProductId(), i.getName(), i.getDescription(),
                    i.getImagePath(), i.getUnitPriceCents(), i.getQuantity());
        }
    }

    public record OrderResponse(Long id, String status, Instant createdAt, long subtotalCents, long shippingCents,
                                long totalCents, List<OrderItemResponse> items) {
        public static OrderResponse from(Order o) {
            return new OrderResponse(o.getId(), o.getStatus().name(), o.getCreatedAt(), o.getSubtotalCents(),
                    o.getShippingCents(), o.totalCents(), o.getItems().stream().map(OrderItemResponse::from).toList());
        }
    }

    public record ErrorResponse(String message) {
    }
}
