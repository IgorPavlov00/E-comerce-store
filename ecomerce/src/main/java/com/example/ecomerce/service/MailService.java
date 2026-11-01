package com.example.ecomerce.service;

import com.example.ecomerce.model.Order;
import com.example.ecomerce.model.OrderItem;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

import static org.springframework.web.util.HtmlUtils.htmlEscape;

/** Sends mail off the request thread. A mail failure is logged and never fails the request. */
@Service
public class MailService {

    /** What the order email needs, copied out so it can be used outside the transaction. */
    public record OrderLine(String name, String description, int quantity, long lineCents) {
    }

    private static final Logger log = LoggerFactory.getLogger(MailService.class);
    private static final String INK = "#17140f";
    private static final String ACCENT = "#d9400f";

    private final JavaMailSender sender;
    private final boolean enabled;
    private final String from;
    private final String baseUrl;

    public MailService(JavaMailSender sender,
                       @Value("${app.mail.enabled}") boolean enabled,
                       @Value("${app.mail.from}") String from,
                       @Value("${app.base-url}") String baseUrl) {
        this.sender = sender;
        this.enabled = enabled;
        this.from = from;
        this.baseUrl = baseUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public static List<OrderLine> linesOf(Order order) {
        return order.getItems().stream().map(MailService::lineOf).toList();
    }

    private static OrderLine lineOf(OrderItem i) {
        return new OrderLine(i.getName(), i.getDescription(), i.getQuantity(), i.getUnitPriceCents() * i.getQuantity());
    }

    @Async
    public void sendVerification(String to, String firstName, String token) {
        String link = baseUrl + "/confirm?token=" + token;
        send(to, "Confirm your Sole&Seam account", layout(
                "<h1 style='margin:0 0 12px;font-size:24px'>Welcome, " + htmlEscape(firstName) + ".</h1>"
                        + "<p style='margin:0 0 24px'>Confirm your email to finish creating your account. This link works for 24 hours.</p>"
                        + "<a href='" + htmlEscape(link) + "' style='display:inline-block;padding:14px 24px;background:" + ACCENT
                        + ";color:#fff;font-weight:700;text-decoration:none'>Confirm my email</a>"));
    }

    @Async
    public void sendOrderConfirmation(String to, long orderId, List<OrderLine> lines, long shippingCents, long totalCents) {
        StringBuilder rows = new StringBuilder();
        for (OrderLine l : lines) {
            rows.append("<tr><td style='padding:10px 0;border-bottom:1px solid #ddd'><b>").append(htmlEscape(l.name()))
                    .append("</b><br>").append(htmlEscape(l.description())).append(" &times; ").append(l.quantity())
                    .append("</td><td align='right' style='padding:10px 0;border-bottom:1px solid #ddd'>")
                    .append(money(l.lineCents())).append("</td></tr>");
        }
        rows.append("<tr><td style='padding:10px 0'>Shipping</td><td align='right'>")
                .append(shippingCents == 0 ? "Free" : money(shippingCents)).append("</td></tr>");
        rows.append("<tr><td style='padding:10px 0'><b>Total</b></td><td align='right'><b>")
                .append(money(totalCents)).append("</b></td></tr>");
        send(to, "Your Sole&Seam order #" + orderId, layout(
                "<h1 style='margin:0 0 12px;font-size:24px'>Thanks for your order.</h1>"
                        + "<p style='margin:0 0 16px'>Order #" + orderId + " is confirmed.</p>"
                        + "<table width='100%' cellspacing='0' cellpadding='0'>" + rows + "</table>"));
    }

    private static String money(long cents) {
        return String.format(Locale.US, "$%.2f", cents / 100.0);
    }

    private static String layout(String body) {
        return "<div style='background:#f4f1ea;padding:32px;font-family:Arial,sans-serif;color:" + INK + "'>"
                + "<div style='max-width:520px;margin:auto;background:#fff;padding:32px'>"
                + "<p style='margin:0 0 24px;font-weight:700;letter-spacing:1px'>SOLE&amp;SEAM</p>" + body + "</div></div>";
    }

    private void send(String to, String subject, String html) {
        if (!enabled) {
            log.info("Mail is disabled, skipping \"{}\"", subject);
            return;
        }
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            sender.send(message);
        } catch (MessagingException | MailException e) {
            log.error("Could not send \"{}\": {}", subject, e.getMessage());
        }
    }
}
