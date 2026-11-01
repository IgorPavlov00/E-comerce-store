package com.example.ecomerce;

import com.example.ecomerce.service.MailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

class MailServiceTests {

    private HttpServer server;
    private String url;
    private final AtomicReference<String> authorization = new AtomicReference<>();
    private final AtomicReference<String> body = new AtomicReference<>();
    private final AtomicInteger status = new AtomicInteger(200);

    @BeforeEach
    void startFakeResend() throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/emails", exchange -> {
            authorization.set(exchange.getRequestHeaders().getFirst("Authorization"));
            body.set(new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8));
            exchange.sendResponseHeaders(status.get(), -1);
            exchange.close();
        });
        server.start();
        url = "http://localhost:" + server.getAddress().getPort() + "/emails";
    }

    @AfterEach
    void stop() {
        server.stop(0);
    }

    private MailService service(JavaMailSender smtp, String key) {
        return new MailService(smtp, false, key, url, "shop@example.com", "https://shop.test");
    }

    @Test
    void sendsConfirmationLinkThroughResend() throws Exception {
        JavaMailSender smtp = mock(JavaMailSender.class);
        MailService mail = service(smtp, "resend-key");
        assertThat(mail.isEnabled()).isTrue();

        mail.sendVerification("ana@example.com", "<Ana>", "token-123");

        assertThat(authorization.get()).isEqualTo("Bearer resend-key");
        JsonNode sent = new ObjectMapper().readTree(body.get());
        assertThat(sent.get("from").asText()).isEqualTo("Sole&Seam <shop@example.com>");
        assertThat(sent.at("/to/0").asText()).isEqualTo("ana@example.com");
        String html = sent.get("html").asText();
        assertThat(html).contains("https://shop.test/confirm?token=token-123");
        assertThat(html).contains("&lt;Ana&gt;").doesNotContain("<Ana>");
        verifyNoInteractions(smtp);
    }

    @Test
    void aRejectedSendDoesNotThrow() {
        status.set(403);
        service(mock(JavaMailSender.class), "bad-key").sendVerification("ana@example.com", "Ana", "token");
        assertThat(body.get()).isNotNull();
    }

    @Test
    void withoutAKeyMailIsOff() {
        MailService mail = service(mock(JavaMailSender.class), "");
        assertThat(mail.isEnabled()).isFalse();
        mail.sendVerification("ana@example.com", "Ana", "token");
        assertThat(body.get()).isNull();
    }
}
