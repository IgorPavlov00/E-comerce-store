package com.example.ecomerce.service;

import com.example.ecomerce.model.Order;
import com.example.ecomerce.model.OrderItem;
import com.example.ecomerce.model.Product;
import com.example.ecomerce.model.User;
import com.example.ecomerce.repository.OrderRepository;
import com.example.ecomerce.web.Dtos.CheckoutItem;
import com.example.ecomerce.web.Dtos.OrderResponse;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    /** Must match the rule shown in the cart (front/src/app/core/cart.service.ts). */
    static final long FREE_SHIPPING_FROM_CENTS = 150_00;
    static final long SHIPPING_CENTS = 20_00;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orders;
    private final ProductService products;
    private final AuthService auth;
    private final StripeGateway stripe;
    private final MailService mail;
    private final String baseUrl;

    public OrderService(OrderRepository orders, ProductService products, AuthService auth, StripeGateway stripe,
                        MailService mail, @Value("${app.base-url}") String baseUrl) {
        this.orders = orders;
        this.products = products;
        this.auth = auth;
        this.stripe = stripe;
        this.mail = mail;
        this.baseUrl = baseUrl;
    }

    /** Prices always come from the database; the browser only says what and how many. */
    @Transactional
    public String startCheckout(List<CheckoutItem> items, Long userId) {
        if (!stripe.isConfigured()) {
            throw new ApiException(HttpStatus.SERVICE_UNAVAILABLE, "Checkout isn't available right now.");
        }
        User user = userId == null ? null : auth.get(userId);

        Order order = new Order();
        order.setUser(user);
        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(baseUrl + "/order/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(baseUrl + "/cart");
        if (user != null) {
            params.setCustomerEmail(user.getEmail());
        }

        long subtotal = 0;
        for (CheckoutItem item : items) {
            Product product = products.get(item.type(), item.id());
            subtotal += product.priceCents() * item.quantity();
            order.addItem(toOrderItem(product, item.quantity()));
            params.addLineItem(lineItem(product.getName() + " - " + product.getDescription(),
                    product.getImagePath(), product.priceCents(), item.quantity()));
        }
        long shipping = subtotal >= FREE_SHIPPING_FROM_CENTS ? 0 : SHIPPING_CENTS;
        if (shipping > 0) {
            params.addLineItem(lineItem("Shipping", null, shipping, 1));
        }
        order.setSubtotalCents(subtotal);
        order.setShippingCents(shipping);

        Session session = createSession(params.build());
        order.setStripeSessionId(session.getId());
        orders.save(order);
        return session.getUrl();
    }

    /**
     * Called when the customer returns from Stripe. Stripe is asked directly whether the
     * session was paid, and the conditional update makes reloading the page harmless.
     */
    @Transactional
    public OrderResponse confirm(String sessionId) {
        Order order = find(sessionId);
        if (order.getStatus() == Order.Status.PENDING) {
            Session session = retrieveSession(sessionId);
            if ("paid".equals(session.getPaymentStatus())) {
                String email = session.getCustomerDetails() != null
                        ? session.getCustomerDetails().getEmail() : session.getCustomerEmail();
                boolean firstToConfirm = orders.markPaid(order.getId(), email) == 1;
                // markPaid clears the persistence context, so load the order again.
                order = find(sessionId);
                if (firstToConfirm && email != null) {
                    mail.sendOrderConfirmation(email, order.getId(), MailService.linesOf(order),
                            order.getShippingCents(), order.totalCents());
                }
            }
        }
        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> paidOrdersOf(Long userId) {
        return orders.findByUserIdAndStatusOrderByCreatedAtDesc(userId, Order.Status.PAID).stream()
                .map(OrderResponse::from)
                .toList();
    }

    private Order find(String sessionId) {
        return orders.findByStripeSessionId(sessionId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "We couldn't find that order."));
    }

    private Session createSession(SessionCreateParams params) {
        try {
            return stripe.create(params);
        } catch (StripeException e) {
            log.error("Stripe rejected the checkout session: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_GATEWAY, "We couldn't reach the payment provider. Please try again.");
        }
    }

    private Session retrieveSession(String sessionId) {
        try {
            return stripe.retrieve(sessionId);
        } catch (StripeException e) {
            log.error("Could not read Stripe session: {}", e.getMessage());
            throw new ApiException(HttpStatus.BAD_GATEWAY, "We couldn't confirm your payment yet. Please reload this page.");
        }
    }

    private static OrderItem toOrderItem(Product product, int quantity) {
        OrderItem line = new OrderItem();
        line.setProductType(product.getType());
        line.setProductId(product.getId());
        line.setName(product.getName());
        line.setDescription(product.getDescription());
        line.setImagePath(product.getImagePath());
        line.setUnitPriceCents(product.priceCents());
        line.setQuantity(quantity);
        return line;
    }

    private SessionCreateParams.LineItem lineItem(String name, String imagePath, long cents, int quantity) {
        SessionCreateParams.LineItem.PriceData.ProductData.Builder product =
                SessionCreateParams.LineItem.PriceData.ProductData.builder().setName(name);
        // Stripe has to fetch images itself, so localhost URLs would be useless to it.
        if (imagePath != null && baseUrl.startsWith("https://")) {
            product.addImage(baseUrl + "/" + imagePath);
        }
        return SessionCreateParams.LineItem.builder()
                .setQuantity((long) quantity)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency("usd")
                        .setUnitAmount(cents)
                        .setProductData(product.build())
                        .build())
                .build();
    }
}
