package com.example.ecomerce.repository;

import com.example.ecomerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByStripeSessionId(String stripeSessionId);

    List<Order> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, Order.Status status);

    /** Returns 1 only for the caller that actually moved the order to PAID. */
    @Modifying(clearAutomatically = true)
    @Query("update Order o set o.status = com.example.ecomerce.model.Order$Status.PAID, o.email = :email "
            + "where o.id = :id and o.status = com.example.ecomerce.model.Order$Status.PENDING")
    int markPaid(@Param("id") Long id, @Param("email") String email);
}
