package com.example.ecomerce;

import com.example.ecomerce.model.Order;
import com.example.ecomerce.model.User;
import com.example.ecomerce.repository.OrderRepository;
import com.example.ecomerce.repository.UserRepository;
import com.example.ecomerce.service.MailService;
import com.example.ecomerce.service.StripeGateway;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StoreApiTests {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired UserRepository users;
    @Autowired OrderRepository orders;
    @MockBean StripeGateway stripe;
    @MockBean MailService mail;

    @BeforeEach
    void stripeIsConfigured() {
        when(stripe.isConfigured()).thenReturn(true);
    }

    private ResultActions postJson(String url, String body) throws Exception {
        return mvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(body));
    }

    private String uniqueEmail() {
        return UUID.randomUUID() + "@example.com";
    }

    private ResultActions register(String email) throws Exception {
        return postJson("/api/auth/register", """
                {"name":"Ana","lastname":"Test","email":"%s","password":"correct-horse"}""".formatted(email));
    }

    private String stubCheckoutSession() throws Exception {
        String id = "cs_" + UUID.randomUUID();
        Session session = mock(Session.class);
        when(session.getId()).thenReturn(id);
        when(session.getUrl()).thenReturn("https://checkout.stripe.test/pay");
        when(stripe.create(any())).thenReturn(session);
        return id;
    }

    @Test
    void catalogIsSeededAndProductsResolveById() throws Exception {
        String body = mvc.perform(get("/api/products")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(17))).andReturn().getResponse().getContentAsString();
        JsonNode first = json.readTree(body).get(0);
        mvc.perform(get("/api/products/" + first.get("type").asText() + "/" + first.get("id").asLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(first.get("name").asText()));
        mvc.perform(get("/api/products/shoe/99999")).andExpect(status().isNotFound());
        mvc.perform(get("/api/products/shirt/1")).andExpect(status().isNotFound());
    }

    @Test
    void passwordsAreHashedAndNeverReturned() throws Exception {
        String email = uniqueEmail();
        String body = register(email).andExpect(status().isOk())
                .andExpect(jsonPath("$.user.password").doesNotExist())
                .andReturn().getResponse().getContentAsString();
        assertThat(body).doesNotContain("correct-horse");
        assertThat(users.findFirstByEmailIgnoreCase(email).orElseThrow().getPassword()).startsWith("$2");
    }

    @Test
    void duplicateAndInvalidRegistrationsAreRejected() throws Exception {
        String email = uniqueEmail();
        register(email).andExpect(status().isOk());
        register(email.toUpperCase()).andExpect(status().isConflict());
        postJson("/api/auth/register", """
                {"name":"A","lastname":"B","email":"not-an-email","password":"correct-horse"}""")
                .andExpect(status().isBadRequest());
        postJson("/api/auth/register", """
                {"name":"A","lastname":"B","email":"%s","password":"short"}""".formatted(uniqueEmail()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginIssuesATokenThatOpensProtectedRoutes() throws Exception {
        String email = uniqueEmail();
        register(email).andExpect(status().isOk());
        postJson("/api/auth/login", """
                {"email":"%s","password":"wrong-password"}""".formatted(email)).andExpect(status().isUnauthorized());

        String body = postJson("/api/auth/login", """
                {"email":"%s","password":"correct-horse"}""".formatted(email))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        String token = json.readTree(body).get("token").asText();

        mvc.perform(get("/api/auth/me")).andExpect(status().isUnauthorized());
        mvc.perform(get("/api/auth/me").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.email").value(email));
        mvc.perform(get("/api/orders/mine").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void whenMailIsOnAccountsMustBeConfirmedBeforeSigningIn() throws Exception {
        when(mail.isEnabled()).thenReturn(true);
        String email = uniqueEmail();
        register(email).andExpect(status().isOk())
                .andExpect(jsonPath("$.verificationRequired").value(true))
                .andExpect(jsonPath("$.token").doesNotExist());
        String login = """
                {"email":"%s","password":"correct-horse"}""".formatted(email);
        postJson("/api/auth/login", login).andExpect(status().isForbidden());

        // The refused sign-in sent a fresh link. The last token emailed must be the working one.
        ArgumentCaptor<String> token = ArgumentCaptor.forClass(String.class);
        verify(mail, times(2)).sendVerification(any(), any(), token.capture());
        assertThat(token.getAllValues().get(0)).isNotEqualTo(token.getValue());

        postJson("/api/auth/confirm", "{\"token\":\"" + token.getAllValues().get(0) + "\"}")
                .andExpect(status().isBadRequest());
        postJson("/api/auth/confirm", "{\"token\":\"" + token.getValue() + "\"}").andExpect(status().isOk());
        postJson("/api/auth/login", login).andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void passwordsStoredBeforeHashingStillWorkAndGetUpgraded() throws Exception {
        String email = uniqueEmail();
        User legacy = new User();
        legacy.setName("Old");
        legacy.setLastname("Account");
        legacy.setEmail(email);
        legacy.setPassword("plain-text-password");
        legacy.setAktivan(false);
        users.save(legacy);

        postJson("/api/auth/login", """
                {"email":"%s","password":"plain-text-password"}""".formatted(email)).andExpect(status().isOk());
        assertThat(users.findFirstByEmailIgnoreCase(email).orElseThrow().getPassword()).startsWith("$2");
    }

    @Test
    void userListingIsGone() throws Exception {
        mvc.perform(get("/api/users/allusers")).andExpect(status().isUnauthorized());
        // The old path now falls through to the web app (or a 404), never to user data.
        assertThat(mvc.perform(get("/users/allusers")).andReturn().getResponse().getContentAsString())
                .doesNotContain("@example.com");
    }

    @Test
    void checkoutChargesDatabasePricesAndAddsShippingBelowThreshold() throws Exception {
        stubCheckoutSession();
        JsonNode product = cheapestShoe();
        // A price sent by the browser must be ignored.
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":%d,"quantity":1,"price":0.01}]}""".formatted(product.get("id").asLong()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value("https://checkout.stripe.test/pay"));

        ArgumentCaptor<SessionCreateParams> params = ArgumentCaptor.forClass(SessionCreateParams.class);
        verify(stripe, times(1)).create(params.capture());
        var lines = params.getValue().getLineItems();
        assertThat(lines).hasSize(2);
        assertThat(lines.get(0).getPriceData().getUnitAmount()).isEqualTo(8000L);
        assertThat(lines.get(0).getPriceData().getProductData().getImages().get(0)).startsWith("https://shop.test/assets/");
        assertThat(lines.get(1).getPriceData().getProductData().getName()).isEqualTo("Shipping");
        assertThat(params.getValue().getSuccessUrl()).startsWith("https://shop.test/order/success");
    }

    @Test
    void shippingIsFreeFromTheThreshold() throws Exception {
        stubCheckoutSession();
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":%d,"quantity":2}]}""".formatted(cheapestShoe().get("id").asLong()))
                .andExpect(status().isOk());
        ArgumentCaptor<SessionCreateParams> params = ArgumentCaptor.forClass(SessionCreateParams.class);
        verify(stripe).create(params.capture());
        assertThat(params.getValue().getLineItems()).hasSize(1);
    }

    @Test
    void checkoutRejectsBadCarts() throws Exception {
        long id = cheapestShoe().get("id").asLong();
        postJson("/api/checkout", "{\"items\":[]}").andExpect(status().isBadRequest());
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":%d,"quantity":0}]}""".formatted(id)).andExpect(status().isBadRequest());
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":999999,"quantity":1}]}""").andExpect(status().isNotFound());
        when(stripe.isConfigured()).thenReturn(false);
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":%d,"quantity":1}]}""".formatted(id)).andExpect(status().isServiceUnavailable());
    }

    @Test
    void confirmingMarksPaidOnceAndEmailsOnce() throws Exception {
        String id = stubCheckoutSession();
        postJson("/api/checkout", """
                {"items":[{"type":"shoe","id":%d,"quantity":1}]}""".formatted(cheapestShoe().get("id").asLong()))
                .andExpect(status().isOk());
        Session.CustomerDetails customer = mock(Session.CustomerDetails.class);
        when(customer.getEmail()).thenReturn("buyer@example.com");
        Session retrieved = mock(Session.class);
        when(retrieved.getCustomerDetails()).thenReturn(customer);
        when(stripe.retrieve(id)).thenReturn(retrieved);

        when(retrieved.getPaymentStatus()).thenReturn("unpaid");
        mvc.perform(get("/api/checkout/confirm").param("session_id", id))
                .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("PENDING"));
        verify(mail, never()).sendOrderConfirmation(anyString(), anyLong(), any(), anyLong(), anyLong());

        when(retrieved.getPaymentStatus()).thenReturn("paid");
        for (int reload = 0; reload < 2; reload++) {
            mvc.perform(get("/api/checkout/confirm").param("session_id", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("PAID"))
                    .andExpect(jsonPath("$.totalCents").value(10000))
                    .andExpect(jsonPath("$.items", hasSize(1)));
        }
        verify(mail, times(1)).sendOrderConfirmation(anyString(), anyLong(), any(), anyLong(), anyLong());
        assertThat(orders.findByStripeSessionId(id).orElseThrow().getStatus()).isEqualTo(Order.Status.PAID);

        mvc.perform(get("/api/checkout/confirm").param("session_id", "cs_missing")).andExpect(status().isNotFound());
    }

    @Test
    void healthIsOpenAndMissingFilesAre404s() throws Exception {
        mvc.perform(get("/api/health")).andExpect(status().isOk());
        mvc.perform(get("/assets/nope.png")).andExpect(status().isNotFound());
    }

    private JsonNode cheapestShoe() throws Exception {
        String body = mvc.perform(get("/api/products")).andReturn().getResponse().getContentAsString();
        JsonNode best = null;
        for (JsonNode p : json.readTree(body)) {
            if (p.get("type").asText().equals("shoe")
                    && (best == null || p.get("price").asDouble() < best.get("price").asDouble())) {
                best = p;
            }
        }
        return best;
    }
}
